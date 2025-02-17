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

package com.liferay.dynamic.data.mapping.internal.security.permission.resource;

import com.liferay.dynamic.data.mapping.constants.DDMConstants;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceLocalService;
import com.liferay.portal.kernel.security.permission.resource.BaseModelResourcePermissionWrapper;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(
	property = "model.class.name=com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance",
	service = ModelResourcePermission.class
)
public class DDMDataProviderInstanceModelResourcePermissionWrapper
	extends BaseModelResourcePermissionWrapper<DDMDataProviderInstance> {

	@Override
	protected ModelResourcePermission<DDMDataProviderInstance>
		doGetModelResourcePermission() {

		return ModelResourcePermissionFactory.create(
			DDMDataProviderInstance.class,
			DDMDataProviderInstance::getDataProviderInstanceId,
			_ddmDataProviderInstanceLocalService::getDDMDataProviderInstance,
			_portletResourcePermission,
			(modelResourcePermission, consumer) -> {
			});
	}

	@Reference
	private DDMDataProviderInstanceLocalService
		_ddmDataProviderInstanceLocalService;

	@Reference(target = "(resource.name=" + DDMConstants.RESOURCE_NAME + ")")
	private PortletResourcePermission _portletResourcePermission;

}