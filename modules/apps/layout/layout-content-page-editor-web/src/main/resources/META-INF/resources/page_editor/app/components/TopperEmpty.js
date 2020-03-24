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

import classNames from 'classnames';
import React, {useContext, useRef} from 'react';

import {
	LayoutDataPropTypes,
	getLayoutDataItemPropTypes,
} from '../../prop-types/index';
import {useSelector} from '../store/index';
import useDragAndDrop, {
	DragDropManagerImpl,
	TARGET_POSITION,
} from '../utils/useDragAndDrop';
import getLabelName from './layout-data-items/getLabelName';

export default function TopperEmpty({children, item, layoutData}) {
	const {
		store: {dropItem, dropTargetItemId, droppable, targetPosition},
	} = useContext(DragDropManagerImpl);
	const containerRef = useRef(null);
	const store = useSelector(state => state);
	const fragmentEntryLinks = store.fragmentEntryLinks;

	const {canDrop, drop, isDragging, isOver} = useDragAndDrop({
		containerRef,
		item,
		layoutData,
	});

	const childrenElement = children({canDrop, isOver});

	const isFragment = childrenElement.type === React.Fragment;
	const realChildren = isFragment
		? childrenElement.props.children
		: childrenElement;

	const isDraggableInPosition = position =>
		targetPosition === position && dropTargetItemId === item.itemId;
	const dataAdvice =
		!droppable && isDraggableInPosition(TARGET_POSITION.MIDDLE)
			? Liferay.Util.sub(
					Liferay.Language.get('a-x-can-not-be-dropped-inside-a-x'),
					[
						getLabelName(dropItem, fragmentEntryLinks),
						getLabelName(item, fragmentEntryLinks),
					]
			  )
			: null;

	return React.Children.map(realChildren, child => {
		if (!child) {
			return child;
		}

		return (
			<>
				{React.cloneElement(child, {
					...child.props,
					className: classNames(child.props.className, {
						'drag-over-bottom': isDraggableInPosition(
							TARGET_POSITION.BOTTOM
						),
						'drag-over-middle': isDraggableInPosition(
							TARGET_POSITION.MIDDLE
						),
						'drag-over-top': isDraggableInPosition(
							TARGET_POSITION.TOP
						),
						dragged: isDragging,
						'not-droppable':
							!droppable &&
							isDraggableInPosition(TARGET_POSITION.MIDDLE),
						'page-editor__topper': true,
					}),
					'data-advice': dataAdvice,
					ref: node => {
						containerRef.current = node;
						drop(node);

						// Call the original ref, if any.
						if (typeof child.ref === 'function') {
							child.ref(node);
						}
					},
				})}
			</>
		);
	});
}

TopperEmpty.propTypes = {
	item: getLayoutDataItemPropTypes().isRequired,
	layoutData: LayoutDataPropTypes.isRequired,
};
