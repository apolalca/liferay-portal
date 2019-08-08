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

package com.liferay.segments.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;
import com.liferay.segments.model.SegmentsExperimentRel;
import com.liferay.segments.service.SegmentsExperimentRelLocalService;
import com.liferay.segments.service.persistence.SegmentsEntryPersistence;
import com.liferay.segments.service.persistence.SegmentsEntryRelPersistence;
import com.liferay.segments.service.persistence.SegmentsExperiencePersistence;
import com.liferay.segments.service.persistence.SegmentsExperimentPersistence;
import com.liferay.segments.service.persistence.SegmentsExperimentRelPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the base implementation for the segments experiment rel local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.segments.service.impl.SegmentsExperimentRelLocalServiceImpl}.
 * </p>
 *
 * @author Eduardo Garcia
 * @see com.liferay.segments.service.impl.SegmentsExperimentRelLocalServiceImpl
 * @generated
 */
@ProviderType
public abstract class SegmentsExperimentRelLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements SegmentsExperimentRelLocalService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>SegmentsExperimentRelLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.segments.service.SegmentsExperimentRelLocalServiceUtil</code>.
	 */

	/**
	 * Adds the segments experiment rel to the database. Also notifies the appropriate model listeners.
	 *
	 * @param segmentsExperimentRel the segments experiment rel
	 * @return the segments experiment rel that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SegmentsExperimentRel addSegmentsExperimentRel(
		SegmentsExperimentRel segmentsExperimentRel) {

		segmentsExperimentRel.setNew(true);

		return segmentsExperimentRelPersistence.update(segmentsExperimentRel);
	}

	/**
	 * Creates a new segments experiment rel with the primary key. Does not add the segments experiment rel to the database.
	 *
	 * @param segmentsExperimentRelId the primary key for the new segments experiment rel
	 * @return the new segments experiment rel
	 */
	@Override
	@Transactional(enabled = false)
	public SegmentsExperimentRel createSegmentsExperimentRel(
		long segmentsExperimentRelId) {

		return segmentsExperimentRelPersistence.create(segmentsExperimentRelId);
	}

	/**
	 * Deletes the segments experiment rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param segmentsExperimentRelId the primary key of the segments experiment rel
	 * @return the segments experiment rel that was removed
	 * @throws PortalException if a segments experiment rel with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SegmentsExperimentRel deleteSegmentsExperimentRel(
			long segmentsExperimentRelId)
		throws PortalException {

		return segmentsExperimentRelPersistence.remove(segmentsExperimentRelId);
	}

	/**
	 * Deletes the segments experiment rel from the database. Also notifies the appropriate model listeners.
	 *
	 * @param segmentsExperimentRel the segments experiment rel
	 * @return the segments experiment rel that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SegmentsExperimentRel deleteSegmentsExperimentRel(
			SegmentsExperimentRel segmentsExperimentRel)
		throws PortalException {

		return segmentsExperimentRelPersistence.remove(segmentsExperimentRel);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			SegmentsExperimentRel.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return segmentsExperimentRelPersistence.findWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.segments.model.impl.SegmentsExperimentRelModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return segmentsExperimentRelPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.segments.model.impl.SegmentsExperimentRelModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return segmentsExperimentRelPersistence.findWithDynamicQuery(
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
		return segmentsExperimentRelPersistence.countWithDynamicQuery(
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

		return segmentsExperimentRelPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public SegmentsExperimentRel fetchSegmentsExperimentRel(
		long segmentsExperimentRelId) {

		return segmentsExperimentRelPersistence.fetchByPrimaryKey(
			segmentsExperimentRelId);
	}

	/**
	 * Returns the segments experiment rel with the primary key.
	 *
	 * @param segmentsExperimentRelId the primary key of the segments experiment rel
	 * @return the segments experiment rel
	 * @throws PortalException if a segments experiment rel with the primary key could not be found
	 */
	@Override
	public SegmentsExperimentRel getSegmentsExperimentRel(
			long segmentsExperimentRelId)
		throws PortalException {

		return segmentsExperimentRelPersistence.findByPrimaryKey(
			segmentsExperimentRelId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			segmentsExperimentRelLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(SegmentsExperimentRel.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"segmentsExperimentRelId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			segmentsExperimentRelLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(
			SegmentsExperimentRel.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"segmentsExperimentRelId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			segmentsExperimentRelLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(SegmentsExperimentRel.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"segmentsExperimentRelId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return segmentsExperimentRelLocalService.deleteSegmentsExperimentRel(
			(SegmentsExperimentRel)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return segmentsExperimentRelPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the segments experiment rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.segments.model.impl.SegmentsExperimentRelModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of segments experiment rels
	 * @param end the upper bound of the range of segments experiment rels (not inclusive)
	 * @return the range of segments experiment rels
	 */
	@Override
	public List<SegmentsExperimentRel> getSegmentsExperimentRels(
		int start, int end) {

		return segmentsExperimentRelPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of segments experiment rels.
	 *
	 * @return the number of segments experiment rels
	 */
	@Override
	public int getSegmentsExperimentRelsCount() {
		return segmentsExperimentRelPersistence.countAll();
	}

	/**
	 * Updates the segments experiment rel in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param segmentsExperimentRel the segments experiment rel
	 * @return the segments experiment rel that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SegmentsExperimentRel updateSegmentsExperimentRel(
		SegmentsExperimentRel segmentsExperimentRel) {

		return segmentsExperimentRelPersistence.update(segmentsExperimentRel);
	}

	/**
	 * Returns the segments entry local service.
	 *
	 * @return the segments entry local service
	 */
	public com.liferay.segments.service.SegmentsEntryLocalService
		getSegmentsEntryLocalService() {

		return segmentsEntryLocalService;
	}

	/**
	 * Sets the segments entry local service.
	 *
	 * @param segmentsEntryLocalService the segments entry local service
	 */
	public void setSegmentsEntryLocalService(
		com.liferay.segments.service.SegmentsEntryLocalService
			segmentsEntryLocalService) {

		this.segmentsEntryLocalService = segmentsEntryLocalService;
	}

	/**
	 * Returns the segments entry persistence.
	 *
	 * @return the segments entry persistence
	 */
	public SegmentsEntryPersistence getSegmentsEntryPersistence() {
		return segmentsEntryPersistence;
	}

	/**
	 * Sets the segments entry persistence.
	 *
	 * @param segmentsEntryPersistence the segments entry persistence
	 */
	public void setSegmentsEntryPersistence(
		SegmentsEntryPersistence segmentsEntryPersistence) {

		this.segmentsEntryPersistence = segmentsEntryPersistence;
	}

	/**
	 * Returns the segments entry rel local service.
	 *
	 * @return the segments entry rel local service
	 */
	public com.liferay.segments.service.SegmentsEntryRelLocalService
		getSegmentsEntryRelLocalService() {

		return segmentsEntryRelLocalService;
	}

	/**
	 * Sets the segments entry rel local service.
	 *
	 * @param segmentsEntryRelLocalService the segments entry rel local service
	 */
	public void setSegmentsEntryRelLocalService(
		com.liferay.segments.service.SegmentsEntryRelLocalService
			segmentsEntryRelLocalService) {

		this.segmentsEntryRelLocalService = segmentsEntryRelLocalService;
	}

	/**
	 * Returns the segments entry rel persistence.
	 *
	 * @return the segments entry rel persistence
	 */
	public SegmentsEntryRelPersistence getSegmentsEntryRelPersistence() {
		return segmentsEntryRelPersistence;
	}

	/**
	 * Sets the segments entry rel persistence.
	 *
	 * @param segmentsEntryRelPersistence the segments entry rel persistence
	 */
	public void setSegmentsEntryRelPersistence(
		SegmentsEntryRelPersistence segmentsEntryRelPersistence) {

		this.segmentsEntryRelPersistence = segmentsEntryRelPersistence;
	}

	/**
	 * Returns the segments experience local service.
	 *
	 * @return the segments experience local service
	 */
	public com.liferay.segments.service.SegmentsExperienceLocalService
		getSegmentsExperienceLocalService() {

		return segmentsExperienceLocalService;
	}

	/**
	 * Sets the segments experience local service.
	 *
	 * @param segmentsExperienceLocalService the segments experience local service
	 */
	public void setSegmentsExperienceLocalService(
		com.liferay.segments.service.SegmentsExperienceLocalService
			segmentsExperienceLocalService) {

		this.segmentsExperienceLocalService = segmentsExperienceLocalService;
	}

	/**
	 * Returns the segments experience persistence.
	 *
	 * @return the segments experience persistence
	 */
	public SegmentsExperiencePersistence getSegmentsExperiencePersistence() {
		return segmentsExperiencePersistence;
	}

	/**
	 * Sets the segments experience persistence.
	 *
	 * @param segmentsExperiencePersistence the segments experience persistence
	 */
	public void setSegmentsExperiencePersistence(
		SegmentsExperiencePersistence segmentsExperiencePersistence) {

		this.segmentsExperiencePersistence = segmentsExperiencePersistence;
	}

	/**
	 * Returns the segments experiment local service.
	 *
	 * @return the segments experiment local service
	 */
	public com.liferay.segments.service.SegmentsExperimentLocalService
		getSegmentsExperimentLocalService() {

		return segmentsExperimentLocalService;
	}

	/**
	 * Sets the segments experiment local service.
	 *
	 * @param segmentsExperimentLocalService the segments experiment local service
	 */
	public void setSegmentsExperimentLocalService(
		com.liferay.segments.service.SegmentsExperimentLocalService
			segmentsExperimentLocalService) {

		this.segmentsExperimentLocalService = segmentsExperimentLocalService;
	}

	/**
	 * Returns the segments experiment persistence.
	 *
	 * @return the segments experiment persistence
	 */
	public SegmentsExperimentPersistence getSegmentsExperimentPersistence() {
		return segmentsExperimentPersistence;
	}

	/**
	 * Sets the segments experiment persistence.
	 *
	 * @param segmentsExperimentPersistence the segments experiment persistence
	 */
	public void setSegmentsExperimentPersistence(
		SegmentsExperimentPersistence segmentsExperimentPersistence) {

		this.segmentsExperimentPersistence = segmentsExperimentPersistence;
	}

	/**
	 * Returns the segments experiment rel local service.
	 *
	 * @return the segments experiment rel local service
	 */
	public SegmentsExperimentRelLocalService
		getSegmentsExperimentRelLocalService() {

		return segmentsExperimentRelLocalService;
	}

	/**
	 * Sets the segments experiment rel local service.
	 *
	 * @param segmentsExperimentRelLocalService the segments experiment rel local service
	 */
	public void setSegmentsExperimentRelLocalService(
		SegmentsExperimentRelLocalService segmentsExperimentRelLocalService) {

		this.segmentsExperimentRelLocalService =
			segmentsExperimentRelLocalService;
	}

	/**
	 * Returns the segments experiment rel persistence.
	 *
	 * @return the segments experiment rel persistence
	 */
	public SegmentsExperimentRelPersistence
		getSegmentsExperimentRelPersistence() {

		return segmentsExperimentRelPersistence;
	}

	/**
	 * Sets the segments experiment rel persistence.
	 *
	 * @param segmentsExperimentRelPersistence the segments experiment rel persistence
	 */
	public void setSegmentsExperimentRelPersistence(
		SegmentsExperimentRelPersistence segmentsExperimentRelPersistence) {

		this.segmentsExperimentRelPersistence =
			segmentsExperimentRelPersistence;
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
			"com.liferay.segments.model.SegmentsExperimentRel",
			segmentsExperimentRelLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.segments.model.SegmentsExperimentRel");
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return SegmentsExperimentRelLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return SegmentsExperimentRel.class;
	}

	protected String getModelClassName() {
		return SegmentsExperimentRel.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				segmentsExperimentRelPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(
		type = com.liferay.segments.service.SegmentsEntryLocalService.class
	)
	protected com.liferay.segments.service.SegmentsEntryLocalService
		segmentsEntryLocalService;

	@BeanReference(type = SegmentsEntryPersistence.class)
	protected SegmentsEntryPersistence segmentsEntryPersistence;

	@BeanReference(
		type = com.liferay.segments.service.SegmentsEntryRelLocalService.class
	)
	protected com.liferay.segments.service.SegmentsEntryRelLocalService
		segmentsEntryRelLocalService;

	@BeanReference(type = SegmentsEntryRelPersistence.class)
	protected SegmentsEntryRelPersistence segmentsEntryRelPersistence;

	@BeanReference(
		type = com.liferay.segments.service.SegmentsExperienceLocalService.class
	)
	protected com.liferay.segments.service.SegmentsExperienceLocalService
		segmentsExperienceLocalService;

	@BeanReference(type = SegmentsExperiencePersistence.class)
	protected SegmentsExperiencePersistence segmentsExperiencePersistence;

	@BeanReference(
		type = com.liferay.segments.service.SegmentsExperimentLocalService.class
	)
	protected com.liferay.segments.service.SegmentsExperimentLocalService
		segmentsExperimentLocalService;

	@BeanReference(type = SegmentsExperimentPersistence.class)
	protected SegmentsExperimentPersistence segmentsExperimentPersistence;

	@BeanReference(type = SegmentsExperimentRelLocalService.class)
	protected SegmentsExperimentRelLocalService
		segmentsExperimentRelLocalService;

	@BeanReference(type = SegmentsExperimentRelPersistence.class)
	protected SegmentsExperimentRelPersistence segmentsExperimentRelPersistence;

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