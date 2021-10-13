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

package com.liferay.dynamic.data.mapping.internal.io;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueJSONDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true, property = "ddm.form.values.deserializer.type=json",
	service = DDMFormValuesDeserializer.class
)
public class DDMFormValuesJSONDeserializer
	implements DDMFormValuesDeserializer {

	@Override
	public DDMFormValuesDeserializerDeserializeResponse deserialize(
		DDMFormValuesDeserializerDeserializeRequest
			ddmFormValuesDeserializerDeserializeRequest) {

		DDMForm ddmForm =
			ddmFormValuesDeserializerDeserializeRequest.getDDMForm();

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormValuesDeserializerDeserializeResponse.Builder builder =
			DDMFormValuesDeserializerDeserializeResponse.Builder.newBuilder(
				ddmFormValues);

		try {
			JSONObject jsonObject = _jsonFactory.createJSONObject(
				ddmFormValuesDeserializerDeserializeRequest.getContent());

			setDDMFormValuesAvailableLocales(
				jsonObject.getJSONArray("availableLanguageIds"), ddmFormValues);
			setDDMFormValuesDefaultLocale(
				jsonObject.getString("defaultLanguageId"), ddmFormValues);
			setDDMFormFieldValues(
				jsonObject.getJSONArray("fieldValues"), ddmForm, ddmFormValues);

			setDDMFormLocalizedValuesDefaultLocale(ddmFormValues);

			return builder.build();
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(exception, exception);
			}

			builder = builder.exception(exception);
		}

		return builder.build();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, DDMFormFieldValueJSONDeserializer.class,
			"ddm.form.field.type.name");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	protected Set<Locale> getAvailableLocales(JSONArray jsonArray) {
		Set<Locale> availableLocales = new HashSet<>();

		if (jsonArray == null) {
			return availableLocales;
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			Locale availableLocale = LocaleUtil.fromLanguageId(
				jsonArray.getString(i));

			availableLocales.add(availableLocale);
		}

		return availableLocales;
	}

	protected DDMFormFieldValue getDDMFormFieldValue(
		JSONObject jsonObject, Map<String, DDMFormField> ddmFormFieldsMap) {

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setFieldReference(
			jsonObject.getString("fieldReference"));

		String instanceId = jsonObject.getString("instanceId");

		if (instanceId.matches("[a-zA-Z0-9]*")) {
			ddmFormFieldValue.setInstanceId(instanceId);
		}

		ddmFormFieldValue.setName(jsonObject.getString("name"));

		setDDMFormFieldValueValue(
			jsonObject, ddmFormFieldsMap.get(jsonObject.getString("name")),
			ddmFormFieldValue);

		setNestedDDMFormFieldValues(
			jsonObject.getJSONArray("nestedFieldValues"), ddmFormFieldsMap,
			ddmFormFieldValue);

		return ddmFormFieldValue;
	}

	protected DDMFormFieldValueJSONDeserializer
		getDDMFormFieldValueJSONDeserializer(DDMFormField ddmFormField) {

		if (ddmFormField == null) {
			return null;
		}

		return _serviceTrackerMap.getService(ddmFormField.getType());
	}

	protected List<DDMFormFieldValue> getDDMFormFieldValues(
		JSONArray jsonArray, Map<String, DDMFormField> ddmFormFieldsMap) {

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			DDMFormFieldValue ddmFormFieldValue = getDDMFormFieldValue(
				jsonArray.getJSONObject(i), ddmFormFieldsMap);

			ddmFormFieldValues.add(ddmFormFieldValue);
		}

		return ddmFormFieldValues;
	}

	protected LocalizedValue getLocalizedValue(JSONObject jsonObject) {
		LocalizedValue localizedValue = new LocalizedValue();

		Iterator<String> iterator = jsonObject.keys();

		while (iterator.hasNext()) {
			String languageId = iterator.next();

			if (LanguageUtil.isAvailableLocale(languageId)) {
				localizedValue.addString(
					LocaleUtil.fromLanguageId(languageId),
					jsonObject.getString(languageId));
			}
		}

		return localizedValue;
	}

	protected Value getValue(DDMFormField ddmFormField, JSONObject jsonObject) {
		DDMFormFieldValueJSONDeserializer ddmFormFieldValueJSONDeserializer =
			getDDMFormFieldValueJSONDeserializer(ddmFormField);

		if (ddmFormFieldValueJSONDeserializer != null) {
			return ddmFormFieldValueJSONDeserializer.deserialize(
				ddmFormField, String.valueOf(jsonObject.get("value")));
		}

		JSONObject valueJSONObject = jsonObject.getJSONObject("value");

		if (isLocalized(valueJSONObject)) {
			return getLocalizedValue(valueJSONObject);
		}

		return new UnlocalizedValue(jsonObject.getString("value"));
	}

	protected boolean isInvalidLocale(String languageId) {
		if (LocaleUtil.fromLanguageId(languageId, true, false) == null) {
			return true;
		}

		return false;
	}

	protected boolean isLocalized(JSONObject jsonObject) {
		if (jsonObject == null) {
			return false;
		}

		Set<String> keys = jsonObject.keySet();

		Stream<String> stream = keys.stream();

		if (stream.anyMatch(this::isInvalidLocale)) {
			return false;
		}

		return true;
	}

	protected void setDDMFormFieldValueLocalizedValueDefaultLocale(
		DDMFormFieldValue ddmFormFieldValue, Locale defaultLocale) {

		Value value = ddmFormFieldValue.getValue();

		if ((value != null) && value.isLocalized()) {
			value.setDefaultLocale(defaultLocale);
		}

		for (DDMFormFieldValue nestedDDMFormFieldValue :
				ddmFormFieldValue.getNestedDDMFormFieldValues()) {

			setDDMFormFieldValueLocalizedValueDefaultLocale(
				nestedDDMFormFieldValue, defaultLocale);
		}
	}

	protected void setDDMFormFieldValues(
		JSONArray jsonArray, DDMForm ddmForm, DDMFormValues ddmFormValues) {

		List<DDMFormFieldValue> ddmFormFieldValues = getDDMFormFieldValues(
			jsonArray, ddmForm.getDDMFormFieldsMap(true));

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);
	}

	protected void setDDMFormFieldValueValue(
		JSONObject jsonObject, DDMFormField ddmFormField,
		DDMFormFieldValue ddmFormFieldValue) {

		String valueString = jsonObject.getString("value", null);

		if (valueString == null) {
			return;
		}

		ddmFormFieldValue.setValue(getValue(ddmFormField, jsonObject));
	}

	protected void setDDMFormLocalizedValuesDefaultLocale(
		DDMFormValues ddmFormValues) {

		for (DDMFormFieldValue ddmFormFieldValue :
				ddmFormValues.getDDMFormFieldValues()) {

			setDDMFormFieldValueLocalizedValueDefaultLocale(
				ddmFormFieldValue, ddmFormValues.getDefaultLocale());
		}
	}

	protected void setDDMFormValuesAvailableLocales(
		JSONArray jsonArray, DDMFormValues ddmFormValues) {

		ddmFormValues.setAvailableLocales(getAvailableLocales(jsonArray));
	}

	protected void setDDMFormValuesDefaultLocale(
		String defaultLanguageId, DDMFormValues ddmFormValues) {

		Locale defaultLocale = LocaleUtil.fromLanguageId(defaultLanguageId);

		ddmFormValues.setDefaultLocale(defaultLocale);

		Set<Locale> availableLocales = ddmFormValues.getAvailableLocales();

		if ((availableLocales != null) &&
			!availableLocales.contains(defaultLocale)) {

			availableLocales.add(defaultLocale);
		}
	}

	@Reference(unbind = "-")
	protected void setJSONFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	protected void setNestedDDMFormFieldValues(
		JSONArray jsonArray, Map<String, DDMFormField> ddmFormFieldsMap,
		DDMFormFieldValue ddmFormFieldValue) {

		if ((jsonArray == null) || (jsonArray.length() == 0)) {
			return;
		}

		List<DDMFormFieldValue> nestedDDMFormFieldValues =
			getDDMFormFieldValues(jsonArray, ddmFormFieldsMap);

		ddmFormFieldValue.setNestedDDMFormFields(nestedDDMFormFieldValues);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormValuesJSONDeserializer.class);

	private JSONFactory _jsonFactory;
	private ServiceTrackerMap<String, DDMFormFieldValueJSONDeserializer>
		_serviceTrackerMap;

}