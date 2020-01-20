/*******************************************************************************
 * Copyright (c) 2018 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package org.eclipse.epp.mpc.rest.client.compatibility.mapping;

import org.eclipse.epp.mpc.core.model.IMarket;
import org.eclipse.epp.mpc.rest.model.Market;
import org.mapstruct.Mapper;

@Mapper
public interface IMarketMapper {

	IMarket restToLegacyMarket(Market market);

	Market legacyToRestMarket(IMarket market);
}
