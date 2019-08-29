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

import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.epp.mpc.core.jaxb.CDataAdapter;
import org.eclipse.epp.mpc.core.jaxb.FlagToBooleanAdapter;

/**
 * A catalog describes an entry point to a marketplace server. Its {@link #getUrl() url} is the base of the Marketplace
 * REST API. It has a description, image and optional additional provider branding information, which can be used to
 * present the marketplace to users, e.g. in the Marketplace Wizard.
 *
 * @see CatalogService
 * @see https://wiki.eclipse.org/Marketplace/REST
 * @author Benjamin Muskalla
 * @author Carsten Reckord
 * @noextend This class is not intended to be extended by clients.
 * @noimplement This class is not intended to be implemented by clients.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Catalog extends Identifiable {

   /**
    * @return this element's name
    */
   @XmlAttribute
   private String title;

   public String getTitle() {
      return title;
   }

   public void setTitle(String value) {
      this.title = value;
   }
   
	/**
	 * If the catalog is self-contained, then a {@link Node node} installation will only use the node's
	 * {@link Node#getUpdateurl() update url} and the catalog's {@link #getDependenciesRepository() dependency
	 * repository}. Otherwise all known repositories are consulted.
	 *
	 * @return true if this catalog is self-contained, false if all known repositories should be used during
	 *         installation
	 */
	@XmlAttribute
	@XmlJavaTypeAdapter(FlagToBooleanAdapter.class)
	private Boolean selfContained;

	public boolean isSelfContained() {
	   return selfContained;
	}

	public void setSelfContained(boolean value) {
	   this.selfContained = value;
	}

	/**
	 * @return this catalog's description suitable for presentation to the user.
	 */
	@XmlJavaTypeAdapter(CDataAdapter.class)
	private String description;

	public String getDescription() {
	   return description;
	}

	public void setDescription(String value) {
	   this.description = value;
	}

	/**
	 * @return a URL to an image resource used to present this catalog in a catalog chooser. May be null.
	 */
	private URL icon;

	public URL getIcon() {
	   return icon;
	}

	public void setIcon(URL value) {
	   this.icon = value;
	}

	/**
	 * @return additional branding information
	 */
	private Wizard wizard;

	public Wizard getWizard() {
	   return wizard;
	}

	public void setBranding(Wizard value) {
	   this.wizard = value;
	}

	/**
	 * An optional URI to a repository from which dependencies may be installed, may be null.
	 *
	 * @return the URI to use for dependency resolution, or null.
	 */
	private String dependenciesRepository;

	public String getDependenciesRepository() {
	   return dependenciesRepository;
	}

	public void setDependenciesRepository(String value) {
	   this.dependenciesRepository = value;
	}
}