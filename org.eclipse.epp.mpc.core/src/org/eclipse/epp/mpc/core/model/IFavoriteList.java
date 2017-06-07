/*******************************************************************************
 * Copyright (c) 2010 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package org.eclipse.epp.mpc.core.model;

import java.util.List;

public interface IFavoriteList extends IIdentifiable {
	String getOwner();

	String getOwnerProfileUrl();

	String getIcon();

	List<INode> getNodes();
}
