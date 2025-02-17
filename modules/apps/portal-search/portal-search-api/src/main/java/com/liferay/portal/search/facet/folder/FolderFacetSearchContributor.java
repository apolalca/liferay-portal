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

package com.liferay.portal.search.facet.folder;

import com.liferay.portal.search.searcher.SearchRequestBuilder;

import java.util.function.Consumer;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author André de Oliveira
 */
@ProviderType
public interface FolderFacetSearchContributor {

	public void contribute(
		SearchRequestBuilder searchRequestBuilder,
		Consumer<FolderFacetBuilder> folderFacetBuilderConsumer);

	@ProviderType
	public interface FolderFacetBuilder {

		public FolderFacetBuilder aggregationName(String aggregationName);

		public FolderFacetBuilder frequencyThreshold(int frequencyThreshold);

		public FolderFacetBuilder maxTerms(int maxTerms);

		public FolderFacetBuilder order(String order);

		public FolderFacetBuilder selectedFolderIds(long... selectedFolderIds);

	}

}