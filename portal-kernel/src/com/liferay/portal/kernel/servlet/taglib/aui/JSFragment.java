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

package com.liferay.portal.kernel.servlet.taglib.aui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Iván Zaera Avellón
 */
public class JSFragment {

	public JSFragment(
		Collection<AMDRequire> amdRequires, Collection<ESImport> esImports,
		String code) {

		this(null, amdRequires, esImports, code);
	}

	public JSFragment(Collection<ESImport> esImports, String code) {
		this(null, null, esImports, code);
	}

	public JSFragment(
		Collection<String> auiUses, Collection<AMDRequire> amdRequires,
		Collection<ESImport> esImports, String code) {

		if (esImports != null) {
			_esImports.addAll(esImports);
		}

		if (amdRequires != null) {
			_amdRequires.addAll(amdRequires);
		}

		if (auiUses != null) {
			_auiUses.addAll(auiUses);
		}

		_code = code;
	}

	public JSFragment(String code) {
		this(null, null, null, code);
	}

	public List<AMDRequire> getAMDRequires() {
		return _amdRequires;
	}

	public List<String> getAUIUses() {
		return _auiUses;
	}

	public String getCode() {
		return _code;
	}

	public List<ESImport> getESImports() {
		return _esImports;
	}

	private final List<AMDRequire> _amdRequires = new ArrayList<>();
	private final List<String> _auiUses = new ArrayList<>();
	private final String _code;
	private final List<ESImport> _esImports = new ArrayList<>();

}