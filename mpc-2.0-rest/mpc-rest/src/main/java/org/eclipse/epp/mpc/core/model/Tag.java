/*******************************************************************************
 * Copyright (c) 2014, 2018 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     The Eclipse Foundation - initial API and implementation
 *     Yatta Solutions - bug 432803: public API
 *******************************************************************************/
package org.eclipse.epp.mpc.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * A simple tag that can be associated with nodes to give additional information about related topics.
 *
 * @author Benjamin Muskalla
 * @author Carsten Reckord
 * @noextend This class is not intended to be extended by clients.
 * @noimplement This class is not intended to be implemented by clients.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Tag extends Named {

}