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
 *     Yatta Solutions GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.epp.mpc.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * A installable unit that can be optional and/or preseleted on install
 *
 * @author Carsten Reckord
 * @noextend This class is not intended to be extended by clients.
 * @noimplement This class is not intended to be implemented by clients.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Iu {

   @XmlValue
	private String id;

	public String getId() {
	   return id;
	}

	public void setId(String value) {
	   this.id = value;
	}

	@XmlAttribute
	private boolean required;

	public boolean isRequired() {
	   return required;
	}

	public void setRequired(boolean value) {
	   this.required = value;
	}

   @XmlAttribute
	private boolean selected;

	public boolean isSelected() {
	   return selected;
	}

	public void setSelected(boolean value) {
	   this.selected = value;
	}

}
