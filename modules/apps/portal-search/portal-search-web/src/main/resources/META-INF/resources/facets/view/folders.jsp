<%--
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
--%>

<%@ include file="/facets/init.jsp" %>

<%
FolderSearchFacetDisplayContextBuilder folderSearchFacetDisplayContextBuilder = new FolderSearchFacetDisplayContextBuilder(renderRequest);

folderSearchFacetDisplayContextBuilder.setFacet(facet);
folderSearchFacetDisplayContextBuilder.setFolderTitleLookup(new FolderTitleLookupImpl(new FolderSearcher(), request));
folderSearchFacetDisplayContextBuilder.setFrequenciesVisible(dataJSONObject.getBoolean("showAssetCount", true));
folderSearchFacetDisplayContextBuilder.setFrequencyThreshold(dataJSONObject.getInt("frequencyThreshold"));
folderSearchFacetDisplayContextBuilder.setMaxTerms(dataJSONObject.getInt("maxTerms", 10));
folderSearchFacetDisplayContextBuilder.setOrder(dataJSONObject.getString("order"));
folderSearchFacetDisplayContextBuilder.setParameterName(facet.getFieldId());
folderSearchFacetDisplayContextBuilder.setParameterValue(fieldParam);

FolderSearchFacetDisplayContext folderSearchFacetDisplayContext = folderSearchFacetDisplayContextBuilder.build();
%>

<c:choose>
	<c:when test="<%= folderSearchFacetDisplayContext.isRenderNothing() %>">
		<c:if test="<%= folderSearchFacetDisplayContext.getParameterValue() != null %>">
			<aui:input autocomplete="off" name="<%= HtmlUtil.escapeAttribute(folderSearchFacetDisplayContext.getParameterName()) %>" type="hidden" value="<%= folderSearchFacetDisplayContext.getParameterValue() %>" />
		</c:if>
	</c:when>
	<c:otherwise>
		<div class="panel panel-secondary">
			<div class="panel-heading">
				<div class="panel-title">
					<liferay-ui:message key="folders" />
				</div>
			</div>

			<div class="panel-body">
				<div class="<%= cssClass %>" data-facetFieldName="<%= HtmlUtil.escapeAttribute(facet.getFieldId()) %>" id="<%= randomNamespace %>facet">
					<aui:input autocomplete="off" name="<%= HtmlUtil.escapeAttribute(folderSearchFacetDisplayContext.getParameterName()) %>" type="hidden" value="<%= folderSearchFacetDisplayContext.getParameterValue() %>" />

					<ul class="folders list-unstyled">
						<li class="default facet-value">
							<a class="<%= folderSearchFacetDisplayContext.isNothingSelected() ? "facet-term-selected" : "facet-term-unselected" %>" data-value="" href="javascript:void(0);"><liferay-ui:message key="<%= HtmlUtil.escape(facetConfiguration.getLabel()) %>" /></a>
						</li>

						<%
						java.util.List<FolderSearchFacetTermDisplayContext> folderSearchFacetTermDisplayContexts = folderSearchFacetDisplayContext.getFolderSearchFacetTermDisplayContexts();

						for (FolderSearchFacetTermDisplayContext folderSearchFacetTermDisplayContext : folderSearchFacetTermDisplayContexts) {
						%>

							<li class="facet-value">
								<a class="<%= folderSearchFacetTermDisplayContext.isSelected() ? "facet-term-selected" : "facet-term-unselected" %>" data-value="<%= folderSearchFacetTermDisplayContext.getFolderId() %>" href="javascript:void(0);">
									<%= HtmlUtil.escape(folderSearchFacetTermDisplayContext.getDisplayName()) %>

									<c:if test="<%= folderSearchFacetTermDisplayContext.isFrequencyVisible() %>">
										<span class="frequency">(<%= folderSearchFacetTermDisplayContext.getFrequency() %>)</span>
									</c:if>
								</a>
							</li>

						<%
						}
						%>

					</ul>
				</div>
			</div>
		</div>
	</c:otherwise>
</c:choose>