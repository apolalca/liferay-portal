/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.remote.cors.internal.servlet.filter;

import com.liferay.oauth2.provider.scope.liferay.OAuth2ProviderScopeLiferayAccessControlContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.remote.cors.configuration.PortalCORSConfiguration;
import com.liferay.portal.remote.cors.internal.CORSSupport;
import com.liferay.portal.remote.cors.internal.url.pattern.matcher.URLPatternMatcher;
import com.liferay.portal.remote.cors.internal.url.pattern.matcher.URLPatternMatcherFactory;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = {
		Constants.SERVICE_PID + "=com.liferay.portal.remote.cors.configuration.PortalCORSConfiguration",
		"before-filter=Upload Servlet Request Filter", "dispatcher=FORWARD",
		"dispatcher=REQUEST", "servlet-context-name=",
		"servlet-filter-name=Portal CORS Servlet Filter", "url-pattern=/*"
	},
	service = {Filter.class, ManagedServiceFactory.class}
)
public class PortalCORSServletFilter
	extends BaseFilter implements ManagedServiceFactory {

	@Override
	public void deleted(String pid) {
		Dictionary<String, ?> properties = _configurationPidsProperties.remove(
			pid);

		long companyId = GetterUtil.getLong(properties.get("companyId"));

		if (companyId == CompanyConstants.SYSTEM) {
			_rebuild();
		}
		else {
			_rebuild(companyId);
		}
	}

	@Override
	public String getName() {
		return StringPool.BLANK;
	}

	@Override
	public void init(FilterConfig filterConfig) {
		ServletContext servletContext = filterConfig.getServletContext();

		_contextPath = servletContext.getContextPath();
	}

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		if (CORSSupport.isCORSRequest(httpServletRequest::getHeader)) {
			return true;
		}

		return false;
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties)
		throws ConfigurationException {

		Dictionary<String, ?> oldProperties = _configurationPidsProperties.put(
			pid, properties);

		long companyId = GetterUtil.getLong(
			properties.get("companyId"), CompanyConstants.SYSTEM);

		if (companyId == CompanyConstants.SYSTEM) {
			_rebuild();

			return;
		}

		if (oldProperties != null) {
			long oldCompanyId = GetterUtil.getLong(
				oldProperties.get("companyId"));

			if (oldCompanyId == CompanyConstants.SYSTEM) {
				_rebuild();

				return;
			}

			if (oldCompanyId != companyId) {
				_rebuild(oldCompanyId);
			}
		}

		_rebuild(companyId);
	}

	public class PortalCORSConfigurationModelListener
		implements ConfigurationModelListener {

		@Override
		public void onBeforeSave(
				String pid, Dictionary<String, Object> newProperties)
			throws ConfigurationModelListenerException {

			Set<String> duplicateURLPatterns = new HashSet<>();
			Set<String> urlPatterns = new HashSet<>();

			PortalCORSConfiguration portalCORSConfiguration =
				ConfigurableUtil.createConfigurable(
					PortalCORSConfiguration.class, newProperties);

			for (String urlPattern :
					portalCORSConfiguration.filterMappingURLPatterns()) {

				if (urlPatterns.contains(urlPattern)) {
					throw new ConfigurationModelListenerException(
						"Duplicate URL pattern " + urlPattern,
						PortalCORSConfiguration.class,
						PortalCORSConfigurationModelListener.class,
						newProperties);
				}

				urlPatterns.add(urlPattern);
			}

			for (Map.Entry<String, Dictionary<String, ?>> entry :
					_configurationPidsProperties.entrySet()) {

				if (StringUtil.equals(pid, entry.getKey())) {
					continue;
				}

				Dictionary<String, ?> properties = entry.getValue();

				if (Objects.equals(
						GetterUtil.getLong(newProperties.get("companyId")),
						GetterUtil.getLong(
							properties.get("companyId")))) {

					continue;
				}

				portalCORSConfiguration = ConfigurableUtil.createConfigurable(
					PortalCORSConfiguration.class, properties);

				for (String urlPattern :
						portalCORSConfiguration.filterMappingURLPatterns()) {

					if (!urlPatterns.add(urlPattern)) {
						duplicateURLPatterns.add(urlPattern);
					}
				}
			}

			if (!duplicateURLPatterns.isEmpty()) {
				throw new ConfigurationModelListenerException(
					"Duplicate URL pattern " + duplicateURLPatterns,
					PortalCORSConfiguration.class,
					PortalCORSConfigurationModelListener.class, newProperties);
			}
		}

	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_defaultURLPatternMatcher = _buildDefaultURLPatternMatcher();

		_serviceRegistration = bundleContext.registerService(
			ConfigurationModelListener.class,
			new PortalCORSConfigurationModelListener(),
			new HashMapDictionary<>(
				HashMapBuilder.putAll(
					properties
				).put(
					"model.class.name",
					"com.liferay.portal.remote.cors.configuration." +
						"PortalCORSConfiguration"
				).build()));
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	protected String getURI(HttpServletRequest httpServletRequest) {
		String uri = httpServletRequest.getRequestURI();

		if (Validator.isNotNull(_contextPath) &&
			!_contextPath.equals(StringPool.SLASH) &&
			uri.startsWith(_contextPath)) {

			uri = uri.substring(_contextPath.length());
		}

		return _http.normalizePath(uri);
	}

	@Override
	protected void processFilter(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws Exception {

		long companyId = _portal.getCompanyId(httpServletRequest);

		if (companyId == CompanyConstants.SYSTEM) {
			return;
		}

		URLPatternMatcher<CORSSupport> urlPatternMatcher =
			_getURLPatternMatcher(companyId);

		CORSSupport corsSupport = urlPatternMatcher.getValue(
			getURI(httpServletRequest));

		if (corsSupport != null) {
			if (StringUtil.equals(
					HttpMethods.OPTIONS, httpServletRequest.getMethod())) {

				if (corsSupport.isValidCORSPreflightRequest(
						httpServletRequest::getHeader)) {

					corsSupport.writeResponseHeaders(
						httpServletRequest::getHeader,
						httpServletResponse::setHeader);
				}

				return;
			}

			if (corsSupport.isValidCORSRequest(
					httpServletRequest.getMethod(),
					httpServletRequest::getHeader) &&
				(OAuth2ProviderScopeLiferayAccessControlContext.
					isOAuth2AuthVerified() ||
				 _isGuest())) {

				corsSupport.writeResponseHeaders(
					httpServletRequest::getHeader,
					httpServletResponse::setHeader);
			}
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private void _buildCORSSupports(
		Map<String, CORSSupport> corsSupports,
		PortalCORSConfiguration portalCORSConfiguration) {

		Map<String, String> corsHeaders = CORSSupport.buildCORSHeaders(
			portalCORSConfiguration.headers());

		CORSSupport corsSupport = new CORSSupport();

		corsSupport.setCORSHeaders(corsHeaders);

		for (String urlPattern :
				portalCORSConfiguration.filterMappingURLPatterns()) {

			if (!corsSupports.containsKey(urlPattern)) {
				corsSupports.put(urlPattern, corsSupport);
			}
		}
	}

	private URLPatternMatcher<CORSSupport> _buildDefaultURLPatternMatcher() {
		Map<String, CORSSupport> corsSupports = new HashMap<>();

		_buildCORSSupports(
			corsSupports,
			ConfigurableUtil.createConfigurable(
				PortalCORSConfiguration.class, new HashMapDictionary<>()));

		return URLPatternMatcherFactory.create(corsSupports);
	}

	private URLPatternMatcher<CORSSupport> _getURLPatternMatcher(
		long companyId) {

		URLPatternMatcher<CORSSupport> urlPatternMatcher =
			_urlPatternMatchers.get(companyId);

		if (urlPatternMatcher != null) {
			return urlPatternMatcher;
		}

		urlPatternMatcher = _urlPatternMatchers.get(CompanyConstants.SYSTEM);

		if (urlPatternMatcher != null) {
			return urlPatternMatcher;
		}

		return _defaultURLPatternMatcher;
	}

	private boolean _isGuest() {
		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker == null) {
			return true;
		}

		User user = permissionChecker.getUser();

		return user.isDefaultUser();
	}

	private void _mergeCORSConfiguration(
		Map<String, CORSSupport> corsSupports, long companyId) {

		for (Dictionary<String, ?> properties :
				_configurationPidsProperties.values()) {

			if (companyId != GetterUtil.getLong(properties.get("companyId"))) {
				continue;
			}

			PortalCORSConfiguration portalCORSConfiguration =
				ConfigurableUtil.createConfigurable(
					PortalCORSConfiguration.class, properties);

			_buildCORSSupports(corsSupports, portalCORSConfiguration);
		}
	}

	private void _rebuild() {
		Map<String, CORSSupport> corsSupports = new HashMap<>();

		_mergeCORSConfiguration(corsSupports, CompanyConstants.SYSTEM);

		_urlPatternMatchers.put(
			CompanyConstants.SYSTEM,
			URLPatternMatcherFactory.create(corsSupports));

		for (long companyId : _urlPatternMatchers.keySet()) {
			if (companyId != CompanyConstants.SYSTEM) {
				_rebuild(companyId);
			}
		}
	}

	private void _rebuild(long companyId) {
		Map<String, CORSSupport> corsSupports = new HashMap<>();

		_mergeCORSConfiguration(corsSupports, companyId);

		if (corsSupports.isEmpty()) {
			_urlPatternMatchers.remove(companyId);

			return;
		}

		_mergeCORSConfiguration(corsSupports, CompanyConstants.SYSTEM);

		_urlPatternMatchers.put(
			companyId, URLPatternMatcherFactory.create(corsSupports));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortalCORSServletFilter.class);

	private final Map<String, Dictionary<String, ?>>
		_configurationPidsProperties = Collections.synchronizedMap(
			new LinkedHashMap<>());
	private String _contextPath;
	private URLPatternMatcher<CORSSupport> _defaultURLPatternMatcher;

	@Reference
	private Http _http;

	@Reference
	private Portal _portal;

	private ServiceRegistration<ConfigurationModelListener>
		_serviceRegistration;
	private final Map<Long, URLPatternMatcher<CORSSupport>>
		_urlPatternMatchers = Collections.synchronizedMap(
			new LinkedHashMap<>());

}