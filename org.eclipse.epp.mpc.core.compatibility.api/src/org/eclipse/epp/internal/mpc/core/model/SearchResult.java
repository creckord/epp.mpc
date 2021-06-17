/*******************************************************************************
 * Copyright (c) 2010, 2018 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      The Eclipse Foundation - initial API and implementation
 *      Yatta Solutions - bug 432803: public API
 *******************************************************************************/
package org.eclipse.epp.internal.mpc.core.model;

import java.util.List;

import org.eclipse.epp.mpc.core.model.ISearchResult;

/**
 * @author David Green
 */
public class SearchResult implements ISearchResult {

	private Integer matchCount;

	private List<Node> nodes;

	/**
	 * The number of matches that matched the query, which may not be equal to the number of nodes returned.
	 */
	@Override
	public Integer getMatchCount() {
		return matchCount;
	}

	/**
	 * The number of matches that matched the query, which may not be equal to the number of nodes returned.
	 */
	public void setMatchCount(Integer matchCount) {
		this.matchCount = matchCount;
	}

	/**
	 * The nodes that were matched by the query
	 */
	@Override
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * The nodes that were matched by the query
	 */
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

}
