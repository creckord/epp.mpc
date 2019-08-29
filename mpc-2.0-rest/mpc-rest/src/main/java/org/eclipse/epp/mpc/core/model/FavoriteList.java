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
 *     The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package org.eclipse.epp.mpc.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class FavoriteList extends Named {
	private String owner;

	public String getOwner() {
	   return owner;
	}

	public void setOwner(String value) {
	   this.owner = value;
	}

	private String ownerProfileUrl;

	public String getOwnerProfileUrl() {
	   return ownerProfileUrl;
	}

	public void setOwnerProfileUrl(String value) {
	   this.ownerProfileUrl = value;
	}

	private String icon;

	public String getIcon() {
	   return icon;
	}

	public void setIcon(String value) {
	   this.icon = value;
	}

	private List<Node> nodes;

	public List<Node> getNodes() {
	   return nodes;
	}

	public void setNodes(List<Node> value) {
	   this.nodes = value;
	}
}
