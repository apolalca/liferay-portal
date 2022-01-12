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

package com.liferay.commerce.price.list.service.base;

import com.liferay.commerce.price.list.model.CommerceTierPriceEntry;
import com.liferay.commerce.price.list.service.CommerceTierPriceEntryLocalService;
import com.liferay.commerce.price.list.service.CommerceTierPriceEntryLocalServiceUtil;
import com.liferay.commerce.price.list.service.persistence.CommercePriceEntryPersistence;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListAccountRelFinder;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListAccountRelPersistence;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListChannelRelFinder;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListChannelRelPersistence;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListCommerceAccountGroupRelFinder;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListCommerceAccountGroupRelPersistence;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListDiscountRelPersistence;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListFinder;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListOrderTypeRelPersistence;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListPersistence;
import com.liferay.commerce.price.list.service.persistence.CommerceTierPriceEntryPersistence;
import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandler;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerRegistryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.change.tracking.CTService;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the commerce tier price entry local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.commerce.price.list.service.impl.CommerceTierPriceEntryLocalServiceImpl}.
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see com.liferay.commerce.price.list.service.impl.CommerceTierPriceEntryLocalServiceImpl
 * @generated
 */
public abstract class CommerceTierPriceEntryLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements CommerceTierPriceEntryLocalService,
			   CTService<CommerceTierPriceEntry>, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>CommerceTierPriceEntryLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>CommerceTierPriceEntryLocalServiceUtil</code>.
	 */

	/**
	 * Adds the commerce tier price entry to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceTierPriceEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceTierPriceEntry the commerce tier price entry
	 * @return the commerce tier price entry that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceTierPriceEntry addCommerceTierPriceEntry(
		CommerceTierPriceEntry commerceTierPriceEntry) {

		commerceTierPriceEntry.setNew(true);

		return commerceTierPriceEntryPersistence.update(commerceTierPriceEntry);
	}

	/**
	 * Creates a new commerce tier price entry with the primary key. Does not add the commerce tier price entry to the database.
	 *
	 * @param commerceTierPriceEntryId the primary key for the new commerce tier price entry
	 * @return the new commerce tier price entry
	 */
	@Override
	@Transactional(enabled = false)
	public CommerceTierPriceEntry createCommerceTierPriceEntry(
		long commerceTierPriceEntryId) {

		return commerceTierPriceEntryPersistence.create(
			commerceTierPriceEntryId);
	}

	/**
	 * Deletes the commerce tier price entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceTierPriceEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceTierPriceEntryId the primary key of the commerce tier price entry
	 * @return the commerce tier price entry that was removed
	 * @throws PortalException if a commerce tier price entry with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CommerceTierPriceEntry deleteCommerceTierPriceEntry(
			long commerceTierPriceEntryId)
		throws PortalException {

		return commerceTierPriceEntryPersistence.remove(
			commerceTierPriceEntryId);
	}

	/**
	 * Deletes the commerce tier price entry from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceTierPriceEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceTierPriceEntry the commerce tier price entry
	 * @return the commerce tier price entry that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CommerceTierPriceEntry deleteCommerceTierPriceEntry(
			CommerceTierPriceEntry commerceTierPriceEntry)
		throws PortalException {

		return commerceTierPriceEntryPersistence.remove(commerceTierPriceEntry);
	}

	@Override
	public <T> T dslQuery(DSLQuery dslQuery) {
		return commerceTierPriceEntryPersistence.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(DSLQuery dslQuery) {
		Long count = dslQuery(dslQuery);

		return count.intValue();
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			CommerceTierPriceEntry.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return commerceTierPriceEntryPersistence.findWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.price.list.model.impl.CommerceTierPriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return commerceTierPriceEntryPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.price.list.model.impl.CommerceTierPriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return commerceTierPriceEntryPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return commerceTierPriceEntryPersistence.countWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection) {

		return commerceTierPriceEntryPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public CommerceTierPriceEntry fetchCommerceTierPriceEntry(
		long commerceTierPriceEntryId) {

		return commerceTierPriceEntryPersistence.fetchByPrimaryKey(
			commerceTierPriceEntryId);
	}

	/**
	 * Returns the commerce tier price entry with the matching UUID and company.
	 *
	 * @param uuid the commerce tier price entry's UUID
	 * @param companyId the primary key of the company
	 * @return the matching commerce tier price entry, or <code>null</code> if a matching commerce tier price entry could not be found
	 */
	@Override
	public CommerceTierPriceEntry fetchCommerceTierPriceEntryByUuidAndCompanyId(
		String uuid, long companyId) {

		return commerceTierPriceEntryPersistence.fetchByUuid_C_First(
			uuid, companyId, null);
	}

	/**
	 * Returns the commerce tier price entry with the matching external reference code and company.
	 *
	 * @param companyId the primary key of the company
	 * @param externalReferenceCode the commerce tier price entry's external reference code
	 * @return the matching commerce tier price entry, or <code>null</code> if a matching commerce tier price entry could not be found
	 */
	@Override
	public CommerceTierPriceEntry
		fetchCommerceTierPriceEntryByExternalReferenceCode(
			long companyId, String externalReferenceCode) {

		return commerceTierPriceEntryPersistence.fetchByC_ERC(
			companyId, externalReferenceCode);
	}

	/**
	 * @deprecated As of Cavanaugh (7.4.x), replaced by {@link #fetchCommerceTierPriceEntryByExternalReferenceCode(long, String)}
	 */
	@Deprecated
	@Override
	public CommerceTierPriceEntry fetchCommerceTierPriceEntryByReferenceCode(
		long companyId, String externalReferenceCode) {

		return fetchCommerceTierPriceEntryByExternalReferenceCode(
			companyId, externalReferenceCode);
	}

	/**
	 * Returns the commerce tier price entry with the matching external reference code and company.
	 *
	 * @param companyId the primary key of the company
	 * @param externalReferenceCode the commerce tier price entry's external reference code
	 * @return the matching commerce tier price entry
	 * @throws PortalException if a matching commerce tier price entry could not be found
	 */
	@Override
	public CommerceTierPriceEntry
			getCommerceTierPriceEntryByExternalReferenceCode(
				long companyId, String externalReferenceCode)
		throws PortalException {

		return commerceTierPriceEntryPersistence.findByC_ERC(
			companyId, externalReferenceCode);
	}

	/**
	 * Returns the commerce tier price entry with the primary key.
	 *
	 * @param commerceTierPriceEntryId the primary key of the commerce tier price entry
	 * @return the commerce tier price entry
	 * @throws PortalException if a commerce tier price entry with the primary key could not be found
	 */
	@Override
	public CommerceTierPriceEntry getCommerceTierPriceEntry(
			long commerceTierPriceEntryId)
		throws PortalException {

		return commerceTierPriceEntryPersistence.findByPrimaryKey(
			commerceTierPriceEntryId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			commerceTierPriceEntryLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(CommerceTierPriceEntry.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceTierPriceEntryId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			commerceTierPriceEntryLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(
			CommerceTierPriceEntry.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceTierPriceEntryId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			commerceTierPriceEntryLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(CommerceTierPriceEntry.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceTierPriceEntryId");
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		final PortletDataContext portletDataContext) {

		final ExportActionableDynamicQuery exportActionableDynamicQuery =
			new ExportActionableDynamicQuery() {

				@Override
				public long performCount() throws PortalException {
					ManifestSummary manifestSummary =
						portletDataContext.getManifestSummary();

					StagedModelType stagedModelType = getStagedModelType();

					long modelAdditionCount = super.performCount();

					manifestSummary.addModelAdditionCount(
						stagedModelType, modelAdditionCount);

					long modelDeletionCount =
						ExportImportHelperUtil.getModelDeletionCount(
							portletDataContext, stagedModelType);

					manifestSummary.addModelDeletionCount(
						stagedModelType, modelDeletionCount);

					return modelAdditionCount;
				}

			};

		initActionableDynamicQuery(exportActionableDynamicQuery);

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					Criterion modifiedDateCriterion =
						portletDataContext.getDateRangeCriteria("modifiedDate");

					Criterion statusDateCriterion =
						portletDataContext.getDateRangeCriteria("statusDate");

					if ((modifiedDateCriterion != null) &&
						(statusDateCriterion != null)) {

						Disjunction disjunction =
							RestrictionsFactoryUtil.disjunction();

						disjunction.add(modifiedDateCriterion);
						disjunction.add(statusDateCriterion);

						dynamicQuery.add(disjunction);
					}

					Property workflowStatusProperty =
						PropertyFactoryUtil.forName("status");

					if (portletDataContext.isInitialPublication()) {
						dynamicQuery.add(
							workflowStatusProperty.ne(
								WorkflowConstants.STATUS_IN_TRASH));
					}
					else {
						StagedModelDataHandler<?> stagedModelDataHandler =
							StagedModelDataHandlerRegistryUtil.
								getStagedModelDataHandler(
									CommerceTierPriceEntry.class.getName());

						dynamicQuery.add(
							workflowStatusProperty.in(
								stagedModelDataHandler.
									getExportableStatuses()));
					}
				}

			});

		exportActionableDynamicQuery.setCompanyId(
			portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<CommerceTierPriceEntry>() {

				@Override
				public void performAction(
						CommerceTierPriceEntry commerceTierPriceEntry)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, commerceTierPriceEntry);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(
					CommerceTierPriceEntry.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return commerceTierPriceEntryPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return commerceTierPriceEntryLocalService.deleteCommerceTierPriceEntry(
			(CommerceTierPriceEntry)persistedModel);
	}

	@Override
	public BasePersistence<CommerceTierPriceEntry> getBasePersistence() {
		return commerceTierPriceEntryPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return commerceTierPriceEntryPersistence.findByPrimaryKey(
			primaryKeyObj);
	}

	/**
	 * Returns the commerce tier price entry with the matching UUID and company.
	 *
	 * @param uuid the commerce tier price entry's UUID
	 * @param companyId the primary key of the company
	 * @return the matching commerce tier price entry
	 * @throws PortalException if a matching commerce tier price entry could not be found
	 */
	@Override
	public CommerceTierPriceEntry getCommerceTierPriceEntryByUuidAndCompanyId(
			String uuid, long companyId)
		throws PortalException {

		return commerceTierPriceEntryPersistence.findByUuid_C_First(
			uuid, companyId, null);
	}

	/**
	 * Returns a range of all the commerce tier price entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.price.list.model.impl.CommerceTierPriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce tier price entries
	 * @param end the upper bound of the range of commerce tier price entries (not inclusive)
	 * @return the range of commerce tier price entries
	 */
	@Override
	public List<CommerceTierPriceEntry> getCommerceTierPriceEntries(
		int start, int end) {

		return commerceTierPriceEntryPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of commerce tier price entries.
	 *
	 * @return the number of commerce tier price entries
	 */
	@Override
	public int getCommerceTierPriceEntriesCount() {
		return commerceTierPriceEntryPersistence.countAll();
	}

	/**
	 * Updates the commerce tier price entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceTierPriceEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceTierPriceEntry the commerce tier price entry
	 * @return the commerce tier price entry that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceTierPriceEntry updateCommerceTierPriceEntry(
		CommerceTierPriceEntry commerceTierPriceEntry) {

		return commerceTierPriceEntryPersistence.update(commerceTierPriceEntry);
	}

	/**
	 * Returns the commerce price entry local service.
	 *
	 * @return the commerce price entry local service
	 */
	public
		com.liferay.commerce.price.list.service.CommercePriceEntryLocalService
			getCommercePriceEntryLocalService() {

		return commercePriceEntryLocalService;
	}

	/**
	 * Sets the commerce price entry local service.
	 *
	 * @param commercePriceEntryLocalService the commerce price entry local service
	 */
	public void setCommercePriceEntryLocalService(
		com.liferay.commerce.price.list.service.CommercePriceEntryLocalService
			commercePriceEntryLocalService) {

		this.commercePriceEntryLocalService = commercePriceEntryLocalService;
	}

	/**
	 * Returns the commerce price entry persistence.
	 *
	 * @return the commerce price entry persistence
	 */
	public CommercePriceEntryPersistence getCommercePriceEntryPersistence() {
		return commercePriceEntryPersistence;
	}

	/**
	 * Sets the commerce price entry persistence.
	 *
	 * @param commercePriceEntryPersistence the commerce price entry persistence
	 */
	public void setCommercePriceEntryPersistence(
		CommercePriceEntryPersistence commercePriceEntryPersistence) {

		this.commercePriceEntryPersistence = commercePriceEntryPersistence;
	}

	/**
	 * Returns the commerce price list local service.
	 *
	 * @return the commerce price list local service
	 */
	public com.liferay.commerce.price.list.service.CommercePriceListLocalService
		getCommercePriceListLocalService() {

		return commercePriceListLocalService;
	}

	/**
	 * Sets the commerce price list local service.
	 *
	 * @param commercePriceListLocalService the commerce price list local service
	 */
	public void setCommercePriceListLocalService(
		com.liferay.commerce.price.list.service.CommercePriceListLocalService
			commercePriceListLocalService) {

		this.commercePriceListLocalService = commercePriceListLocalService;
	}

	/**
	 * Returns the commerce price list persistence.
	 *
	 * @return the commerce price list persistence
	 */
	public CommercePriceListPersistence getCommercePriceListPersistence() {
		return commercePriceListPersistence;
	}

	/**
	 * Sets the commerce price list persistence.
	 *
	 * @param commercePriceListPersistence the commerce price list persistence
	 */
	public void setCommercePriceListPersistence(
		CommercePriceListPersistence commercePriceListPersistence) {

		this.commercePriceListPersistence = commercePriceListPersistence;
	}

	/**
	 * Returns the commerce price list finder.
	 *
	 * @return the commerce price list finder
	 */
	public CommercePriceListFinder getCommercePriceListFinder() {
		return commercePriceListFinder;
	}

	/**
	 * Sets the commerce price list finder.
	 *
	 * @param commercePriceListFinder the commerce price list finder
	 */
	public void setCommercePriceListFinder(
		CommercePriceListFinder commercePriceListFinder) {

		this.commercePriceListFinder = commercePriceListFinder;
	}

	/**
	 * Returns the commerce price list account rel local service.
	 *
	 * @return the commerce price list account rel local service
	 */
	public com.liferay.commerce.price.list.service.
		CommercePriceListAccountRelLocalService
			getCommercePriceListAccountRelLocalService() {

		return commercePriceListAccountRelLocalService;
	}

	/**
	 * Sets the commerce price list account rel local service.
	 *
	 * @param commercePriceListAccountRelLocalService the commerce price list account rel local service
	 */
	public void setCommercePriceListAccountRelLocalService(
		com.liferay.commerce.price.list.service.
			CommercePriceListAccountRelLocalService
				commercePriceListAccountRelLocalService) {

		this.commercePriceListAccountRelLocalService =
			commercePriceListAccountRelLocalService;
	}

	/**
	 * Returns the commerce price list account rel persistence.
	 *
	 * @return the commerce price list account rel persistence
	 */
	public CommercePriceListAccountRelPersistence
		getCommercePriceListAccountRelPersistence() {

		return commercePriceListAccountRelPersistence;
	}

	/**
	 * Sets the commerce price list account rel persistence.
	 *
	 * @param commercePriceListAccountRelPersistence the commerce price list account rel persistence
	 */
	public void setCommercePriceListAccountRelPersistence(
		CommercePriceListAccountRelPersistence
			commercePriceListAccountRelPersistence) {

		this.commercePriceListAccountRelPersistence =
			commercePriceListAccountRelPersistence;
	}

	/**
	 * Returns the commerce price list account rel finder.
	 *
	 * @return the commerce price list account rel finder
	 */
	public CommercePriceListAccountRelFinder
		getCommercePriceListAccountRelFinder() {

		return commercePriceListAccountRelFinder;
	}

	/**
	 * Sets the commerce price list account rel finder.
	 *
	 * @param commercePriceListAccountRelFinder the commerce price list account rel finder
	 */
	public void setCommercePriceListAccountRelFinder(
		CommercePriceListAccountRelFinder commercePriceListAccountRelFinder) {

		this.commercePriceListAccountRelFinder =
			commercePriceListAccountRelFinder;
	}

	/**
	 * Returns the commerce price list channel rel local service.
	 *
	 * @return the commerce price list channel rel local service
	 */
	public com.liferay.commerce.price.list.service.
		CommercePriceListChannelRelLocalService
			getCommercePriceListChannelRelLocalService() {

		return commercePriceListChannelRelLocalService;
	}

	/**
	 * Sets the commerce price list channel rel local service.
	 *
	 * @param commercePriceListChannelRelLocalService the commerce price list channel rel local service
	 */
	public void setCommercePriceListChannelRelLocalService(
		com.liferay.commerce.price.list.service.
			CommercePriceListChannelRelLocalService
				commercePriceListChannelRelLocalService) {

		this.commercePriceListChannelRelLocalService =
			commercePriceListChannelRelLocalService;
	}

	/**
	 * Returns the commerce price list channel rel persistence.
	 *
	 * @return the commerce price list channel rel persistence
	 */
	public CommercePriceListChannelRelPersistence
		getCommercePriceListChannelRelPersistence() {

		return commercePriceListChannelRelPersistence;
	}

	/**
	 * Sets the commerce price list channel rel persistence.
	 *
	 * @param commercePriceListChannelRelPersistence the commerce price list channel rel persistence
	 */
	public void setCommercePriceListChannelRelPersistence(
		CommercePriceListChannelRelPersistence
			commercePriceListChannelRelPersistence) {

		this.commercePriceListChannelRelPersistence =
			commercePriceListChannelRelPersistence;
	}

	/**
	 * Returns the commerce price list channel rel finder.
	 *
	 * @return the commerce price list channel rel finder
	 */
	public CommercePriceListChannelRelFinder
		getCommercePriceListChannelRelFinder() {

		return commercePriceListChannelRelFinder;
	}

	/**
	 * Sets the commerce price list channel rel finder.
	 *
	 * @param commercePriceListChannelRelFinder the commerce price list channel rel finder
	 */
	public void setCommercePriceListChannelRelFinder(
		CommercePriceListChannelRelFinder commercePriceListChannelRelFinder) {

		this.commercePriceListChannelRelFinder =
			commercePriceListChannelRelFinder;
	}

	/**
	 * Returns the commerce price list commerce account group rel local service.
	 *
	 * @return the commerce price list commerce account group rel local service
	 */
	public com.liferay.commerce.price.list.service.
		CommercePriceListCommerceAccountGroupRelLocalService
			getCommercePriceListCommerceAccountGroupRelLocalService() {

		return commercePriceListCommerceAccountGroupRelLocalService;
	}

	/**
	 * Sets the commerce price list commerce account group rel local service.
	 *
	 * @param commercePriceListCommerceAccountGroupRelLocalService the commerce price list commerce account group rel local service
	 */
	public void setCommercePriceListCommerceAccountGroupRelLocalService(
		com.liferay.commerce.price.list.service.
			CommercePriceListCommerceAccountGroupRelLocalService
				commercePriceListCommerceAccountGroupRelLocalService) {

		this.commercePriceListCommerceAccountGroupRelLocalService =
			commercePriceListCommerceAccountGroupRelLocalService;
	}

	/**
	 * Returns the commerce price list commerce account group rel persistence.
	 *
	 * @return the commerce price list commerce account group rel persistence
	 */
	public CommercePriceListCommerceAccountGroupRelPersistence
		getCommercePriceListCommerceAccountGroupRelPersistence() {

		return commercePriceListCommerceAccountGroupRelPersistence;
	}

	/**
	 * Sets the commerce price list commerce account group rel persistence.
	 *
	 * @param commercePriceListCommerceAccountGroupRelPersistence the commerce price list commerce account group rel persistence
	 */
	public void setCommercePriceListCommerceAccountGroupRelPersistence(
		CommercePriceListCommerceAccountGroupRelPersistence
			commercePriceListCommerceAccountGroupRelPersistence) {

		this.commercePriceListCommerceAccountGroupRelPersistence =
			commercePriceListCommerceAccountGroupRelPersistence;
	}

	/**
	 * Returns the commerce price list commerce account group rel finder.
	 *
	 * @return the commerce price list commerce account group rel finder
	 */
	public CommercePriceListCommerceAccountGroupRelFinder
		getCommercePriceListCommerceAccountGroupRelFinder() {

		return commercePriceListCommerceAccountGroupRelFinder;
	}

	/**
	 * Sets the commerce price list commerce account group rel finder.
	 *
	 * @param commercePriceListCommerceAccountGroupRelFinder the commerce price list commerce account group rel finder
	 */
	public void setCommercePriceListCommerceAccountGroupRelFinder(
		CommercePriceListCommerceAccountGroupRelFinder
			commercePriceListCommerceAccountGroupRelFinder) {

		this.commercePriceListCommerceAccountGroupRelFinder =
			commercePriceListCommerceAccountGroupRelFinder;
	}

	/**
	 * Returns the commerce price list discount rel local service.
	 *
	 * @return the commerce price list discount rel local service
	 */
	public com.liferay.commerce.price.list.service.
		CommercePriceListDiscountRelLocalService
			getCommercePriceListDiscountRelLocalService() {

		return commercePriceListDiscountRelLocalService;
	}

	/**
	 * Sets the commerce price list discount rel local service.
	 *
	 * @param commercePriceListDiscountRelLocalService the commerce price list discount rel local service
	 */
	public void setCommercePriceListDiscountRelLocalService(
		com.liferay.commerce.price.list.service.
			CommercePriceListDiscountRelLocalService
				commercePriceListDiscountRelLocalService) {

		this.commercePriceListDiscountRelLocalService =
			commercePriceListDiscountRelLocalService;
	}

	/**
	 * Returns the commerce price list discount rel persistence.
	 *
	 * @return the commerce price list discount rel persistence
	 */
	public CommercePriceListDiscountRelPersistence
		getCommercePriceListDiscountRelPersistence() {

		return commercePriceListDiscountRelPersistence;
	}

	/**
	 * Sets the commerce price list discount rel persistence.
	 *
	 * @param commercePriceListDiscountRelPersistence the commerce price list discount rel persistence
	 */
	public void setCommercePriceListDiscountRelPersistence(
		CommercePriceListDiscountRelPersistence
			commercePriceListDiscountRelPersistence) {

		this.commercePriceListDiscountRelPersistence =
			commercePriceListDiscountRelPersistence;
	}

	/**
	 * Returns the commerce price list order type rel local service.
	 *
	 * @return the commerce price list order type rel local service
	 */
	public com.liferay.commerce.price.list.service.
		CommercePriceListOrderTypeRelLocalService
			getCommercePriceListOrderTypeRelLocalService() {

		return commercePriceListOrderTypeRelLocalService;
	}

	/**
	 * Sets the commerce price list order type rel local service.
	 *
	 * @param commercePriceListOrderTypeRelLocalService the commerce price list order type rel local service
	 */
	public void setCommercePriceListOrderTypeRelLocalService(
		com.liferay.commerce.price.list.service.
			CommercePriceListOrderTypeRelLocalService
				commercePriceListOrderTypeRelLocalService) {

		this.commercePriceListOrderTypeRelLocalService =
			commercePriceListOrderTypeRelLocalService;
	}

	/**
	 * Returns the commerce price list order type rel persistence.
	 *
	 * @return the commerce price list order type rel persistence
	 */
	public CommercePriceListOrderTypeRelPersistence
		getCommercePriceListOrderTypeRelPersistence() {

		return commercePriceListOrderTypeRelPersistence;
	}

	/**
	 * Sets the commerce price list order type rel persistence.
	 *
	 * @param commercePriceListOrderTypeRelPersistence the commerce price list order type rel persistence
	 */
	public void setCommercePriceListOrderTypeRelPersistence(
		CommercePriceListOrderTypeRelPersistence
			commercePriceListOrderTypeRelPersistence) {

		this.commercePriceListOrderTypeRelPersistence =
			commercePriceListOrderTypeRelPersistence;
	}

	/**
	 * Returns the commerce tier price entry local service.
	 *
	 * @return the commerce tier price entry local service
	 */
	public CommerceTierPriceEntryLocalService
		getCommerceTierPriceEntryLocalService() {

		return commerceTierPriceEntryLocalService;
	}

	/**
	 * Sets the commerce tier price entry local service.
	 *
	 * @param commerceTierPriceEntryLocalService the commerce tier price entry local service
	 */
	public void setCommerceTierPriceEntryLocalService(
		CommerceTierPriceEntryLocalService commerceTierPriceEntryLocalService) {

		this.commerceTierPriceEntryLocalService =
			commerceTierPriceEntryLocalService;
	}

	/**
	 * Returns the commerce tier price entry persistence.
	 *
	 * @return the commerce tier price entry persistence
	 */
	public CommerceTierPriceEntryPersistence
		getCommerceTierPriceEntryPersistence() {

		return commerceTierPriceEntryPersistence;
	}

	/**
	 * Sets the commerce tier price entry persistence.
	 *
	 * @param commerceTierPriceEntryPersistence the commerce tier price entry persistence
	 */
	public void setCommerceTierPriceEntryPersistence(
		CommerceTierPriceEntryPersistence commerceTierPriceEntryPersistence) {

		this.commerceTierPriceEntryPersistence =
			commerceTierPriceEntryPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService
		getCounterLocalService() {

		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService
			counterLocalService) {

		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.kernel.service.ClassNameLocalService
		getClassNameLocalService() {

		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.kernel.service.ClassNameLocalService
			classNameLocalService) {

		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {

		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService
		getResourceLocalService() {

		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService
			resourceLocalService) {

		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService
		getUserLocalService() {

		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {

		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register(
			"com.liferay.commerce.price.list.model.CommerceTierPriceEntry",
			commerceTierPriceEntryLocalService);

		_setLocalServiceUtilService(commerceTierPriceEntryLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.commerce.price.list.model.CommerceTierPriceEntry");

		_setLocalServiceUtilService(null);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CommerceTierPriceEntryLocalService.class.getName();
	}

	@Override
	public CTPersistence<CommerceTierPriceEntry> getCTPersistence() {
		return commerceTierPriceEntryPersistence;
	}

	@Override
	public Class<CommerceTierPriceEntry> getModelClass() {
		return CommerceTierPriceEntry.class;
	}

	@Override
	public <R, E extends Throwable> R updateWithUnsafeFunction(
			UnsafeFunction<CTPersistence<CommerceTierPriceEntry>, R, E>
				updateUnsafeFunction)
		throws E {

		return updateUnsafeFunction.apply(commerceTierPriceEntryPersistence);
	}

	protected String getModelClassName() {
		return CommerceTierPriceEntry.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				commerceTierPriceEntryPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	private void _setLocalServiceUtilService(
		CommerceTierPriceEntryLocalService commerceTierPriceEntryLocalService) {

		try {
			Field field =
				CommerceTierPriceEntryLocalServiceUtil.class.getDeclaredField(
					"_service");

			field.setAccessible(true);

			field.set(null, commerceTierPriceEntryLocalService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@BeanReference(
		type = com.liferay.commerce.price.list.service.CommercePriceEntryLocalService.class
	)
	protected
		com.liferay.commerce.price.list.service.CommercePriceEntryLocalService
			commercePriceEntryLocalService;

	@BeanReference(type = CommercePriceEntryPersistence.class)
	protected CommercePriceEntryPersistence commercePriceEntryPersistence;

	@BeanReference(
		type = com.liferay.commerce.price.list.service.CommercePriceListLocalService.class
	)
	protected
		com.liferay.commerce.price.list.service.CommercePriceListLocalService
			commercePriceListLocalService;

	@BeanReference(type = CommercePriceListPersistence.class)
	protected CommercePriceListPersistence commercePriceListPersistence;

	@BeanReference(type = CommercePriceListFinder.class)
	protected CommercePriceListFinder commercePriceListFinder;

	@BeanReference(
		type = com.liferay.commerce.price.list.service.CommercePriceListAccountRelLocalService.class
	)
	protected com.liferay.commerce.price.list.service.
		CommercePriceListAccountRelLocalService
			commercePriceListAccountRelLocalService;

	@BeanReference(type = CommercePriceListAccountRelPersistence.class)
	protected CommercePriceListAccountRelPersistence
		commercePriceListAccountRelPersistence;

	@BeanReference(type = CommercePriceListAccountRelFinder.class)
	protected CommercePriceListAccountRelFinder
		commercePriceListAccountRelFinder;

	@BeanReference(
		type = com.liferay.commerce.price.list.service.CommercePriceListChannelRelLocalService.class
	)
	protected com.liferay.commerce.price.list.service.
		CommercePriceListChannelRelLocalService
			commercePriceListChannelRelLocalService;

	@BeanReference(type = CommercePriceListChannelRelPersistence.class)
	protected CommercePriceListChannelRelPersistence
		commercePriceListChannelRelPersistence;

	@BeanReference(type = CommercePriceListChannelRelFinder.class)
	protected CommercePriceListChannelRelFinder
		commercePriceListChannelRelFinder;

	@BeanReference(
		type = com.liferay.commerce.price.list.service.CommercePriceListCommerceAccountGroupRelLocalService.class
	)
	protected com.liferay.commerce.price.list.service.
		CommercePriceListCommerceAccountGroupRelLocalService
			commercePriceListCommerceAccountGroupRelLocalService;

	@BeanReference(
		type = CommercePriceListCommerceAccountGroupRelPersistence.class
	)
	protected CommercePriceListCommerceAccountGroupRelPersistence
		commercePriceListCommerceAccountGroupRelPersistence;

	@BeanReference(type = CommercePriceListCommerceAccountGroupRelFinder.class)
	protected CommercePriceListCommerceAccountGroupRelFinder
		commercePriceListCommerceAccountGroupRelFinder;

	@BeanReference(
		type = com.liferay.commerce.price.list.service.CommercePriceListDiscountRelLocalService.class
	)
	protected com.liferay.commerce.price.list.service.
		CommercePriceListDiscountRelLocalService
			commercePriceListDiscountRelLocalService;

	@BeanReference(type = CommercePriceListDiscountRelPersistence.class)
	protected CommercePriceListDiscountRelPersistence
		commercePriceListDiscountRelPersistence;

	@BeanReference(
		type = com.liferay.commerce.price.list.service.CommercePriceListOrderTypeRelLocalService.class
	)
	protected com.liferay.commerce.price.list.service.
		CommercePriceListOrderTypeRelLocalService
			commercePriceListOrderTypeRelLocalService;

	@BeanReference(type = CommercePriceListOrderTypeRelPersistence.class)
	protected CommercePriceListOrderTypeRelPersistence
		commercePriceListOrderTypeRelPersistence;

	@BeanReference(type = CommerceTierPriceEntryLocalService.class)
	protected CommerceTierPriceEntryLocalService
		commerceTierPriceEntryLocalService;

	@BeanReference(type = CommerceTierPriceEntryPersistence.class)
	protected CommerceTierPriceEntryPersistence
		commerceTierPriceEntryPersistence;

	@ServiceReference(
		type = com.liferay.counter.kernel.service.CounterLocalService.class
	)
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.ClassNameLocalService.class
	)
	protected com.liferay.portal.kernel.service.ClassNameLocalService
		classNameLocalService;

	@ServiceReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.ResourceLocalService.class
	)
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.UserLocalService.class
	)
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;

	@ServiceReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry
		persistedModelLocalServiceRegistry;

}