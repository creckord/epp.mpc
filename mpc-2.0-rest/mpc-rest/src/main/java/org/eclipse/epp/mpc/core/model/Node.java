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

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.epp.mpc.core.jaxb.CDataAdapter;
import org.eclipse.epp.mpc.core.jaxb.TokensAdapter;
import org.eclipse.epp.mpc.core.jaxb.EpochDateAdapter;
import org.eclipse.epp.mpc.core.jaxb.FlagToBooleanAdapter;

/**
 * A node represents an entry on a marketplace. It is associated with one or more categories, under which it is listed.
 * Additionally, tags can be specified to define related technologies.
 * <p>
 * A node contains all information about a marketplace entry necessary to present it to users, including a
 * {@link #getName()}, {@link #getOwner() owner}, {@link #getShortdescription() short} and {@link #getBody() long}
 * description, {@link #getImage() icon} and optional {@link #getScreenshot()}. Some social feedback on the node entry
 * is provided by means of its {@link #getInstallsTotal() total} and {@link #getInstallsRecent() recent} installation
 * counts, as well as the number of {@link #getFavorited() favorite votes} it has received.
 * <p>
 * Nodes can describe different kinds of {@link #getType() contributions}, like installable Eclipse plug-ins, consulting
 * services and so on. In case of installable Eclipse plug-ins, the {@link #getIus() installable units} are provided for
 * installation from the node's {@link #getUpdateurl() update site}.
 *
 * @author David Green
 * @author Benjamin Muskalla
 * @author Carsten Reckord
 * @noextend This class is not intended to be extended by clients.
 * @noimplement This class is not intended to be implemented by clients.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Node extends Named {

	/**
	 * The number of times this node has been favorited.
	 */
	private Integer favorited;

	public Integer getFavorited() {
	   return favorited;
	}

	public void setFavorited(Integer value) {
	   this.favorited = value;
	}

	/**
	 * The number of times this node has been installed.
	 */
	@XmlElement(name="installstotal")
	private Integer installsTotal;

	public Integer getInstallsTotal() {
	   return installsTotal;
	}

	public void setInstallsTotal(Integer value) {
	   this.installsTotal = value;
	}

	/**
	 * The number of times this node has been installed recently (last 30 days).
	 */
   @XmlElement(name="installsrecent")
	private Integer installsRecent;

	public Integer getInstallsRecent() {
	   return installsRecent;
	}

	public void setInstallsRecent(Integer value) {
	   this.installsRecent = value;
	}

	/**
	 * The type of listing, for example 'resource' or 'training'.
	 */
	private String type;

	public String getType() {
	   return type;
	}

	public void setType(String value) {
	   this.type = value;
	}

	/**
	 * the categories of this listing.
	 */
	@XmlElementWrapper
	@XmlElement(name="category")
	private List<Category> categories;

	public List<Category> getCategories() {
	   return categories;
	}

	public void setCategories(List<Category> value) {
	   this.categories = value;
	}

	@XmlElementWrapper
	@XmlElement(name="tag")
	private List<Tag> tags;

	public List<Tag> getTags() {
	   return tags;
	}

	public void setTags(List<Tag> value) {
	   this.tags = value;
	}

	private String owner;

	public String getOwner() {
	   return owner;
	}

	public void setOwner(String value) {
	   this.owner = value;
	}

	/**
	 * The short description of this listing, may include HTML markup (escaped). Note that the short description may or
	 * may not be shorter than the body.
	 */
   @XmlJavaTypeAdapter(CDataAdapter.class)
	private String shortdescription;

	public String getShortdescription() {
	   return shortdescription;
	}

	public void setShortdescription(String value) {
	   this.shortdescription = value;
	}

	/**
	 * The description of this listing, may include HTML markup (escaped).
	 */
   @XmlJavaTypeAdapter(CDataAdapter.class)
	private String body;

	public String getBody() {
	   return body;
	}

	public void setBody(String value) {
	   this.body = value;
	}

	/**
	 * The time of creation for this entry.
	 */
   @XmlJavaTypeAdapter(EpochDateAdapter.class)
	private Date created;

	public Date getCreated() {
	   return created;
	}

	public void setCreated(Date value) {
	   this.created = value;
	}

	/**
	 * The last change time for this entry.
	 */
   @XmlJavaTypeAdapter(EpochDateAdapter.class)
	private Date changed;

	public Date getChanged() {
	   return changed;
	}

	public void setChanged(Date value) {
	   this.changed = value;
	}

	/**
	 * @return true if this node's owner is a foundation member, false otherwise, null if unknown.
	 */
   @XmlJavaTypeAdapter(FlagToBooleanAdapter.class)
	private Boolean foundationmember;

	public Boolean getFoundationmember() {
	   return foundationmember;
	}

	public void setFoundationmember(Boolean value) {
	   this.foundationmember = value;
	}

	/**
	 * An URL for the homepage for this entry or its owner.
	 */
	private String homepageurl;

	public String getHomepageurl() {
	   return homepageurl;
	}

	public void setHomepageurl(String value) {
	   this.homepageurl = value;
	}

	/**
	 * The image used as this entry's logo
	 */
   @XmlJavaTypeAdapter(CDataAdapter.class)
	private String image;

	public String getImage() {
	   return image;
	}

	public void setImage(String value) {
	   this.image = value;
	}

	/**
	 * An optional screenshot used in conjunction with the {@link #getBody() full description}.
	 */
	private String screenshot;

	public String getScreenshot() {
	   return screenshot;
	}

	public void setScreenshot(String value) {
	   this.screenshot = value;
	}

	/**
	 * The version of the solution represented by this node. It is encouraged to use a valid OSGi version, but this
	 * isn't guaranteed.
	 */
	private String version;

	public String getVersion() {
	   return version;
	}

	public void setVersion(String value) {
	   this.version = value;
	}

	/**
	 * The license for the plug-in represented by this node, e.g. 'EPL'.
	 */
	private String license;

	public String getLicense() {
	   return license;
	}

	public void setLicense(String value) {
	   this.license = value;
	}

	/**
	 * The owner's company name
	 */
   @XmlJavaTypeAdapter(CDataAdapter.class)
	private String companyname;

	public String getCompanyname() {
	   return companyname;
	}

	public void setCompanyname(String value) {
	   this.companyname = value;
	}

	/**
	 * The development status of this plug-in entry, e.g. "Production/Stable"
	 */
	private String status;

	public String getStatus() {
	   return status;
	}

	public void setStatus(String value) {
	   this.status = value;
	}

	/**
	 * A comma-separated list of supported Eclipse versions. Currently, this can take any form, although it is
	 * encouraged to use a comma-separated list of Eclipse versions like "3.6-3.8, 4.2.1, 4.2.2, 4.3".
	 * <p>
	 * This might get more standardized in the future.
	 */
	@XmlElement
   @XmlJavaTypeAdapter(TokensAdapter.class)
	private List<String> eclipseversion;

	public List<String> getEclipseversion() {
	   return eclipseversion;
	}

	public void setEclipseversion(List<String> value) {
	   this.eclipseversion = value;
	}
	
	@XmlElement(name="min_java_version")
	public String minJavaVersion;

	/**
	 * @return a contact URL to get support for this entry
	 */
   @XmlJavaTypeAdapter(CDataAdapter.class)
	private String supporturl;

	public String getSupporturl() {
	   return supporturl;
	}

	public void setSupporturl(String value) {
	   this.supporturl = value;
	}

	/**
	 * The URL of an Eclipse update site containing this entry's {@link #getIus() installable units}.
	 */
	private String updateurl;

	public String getUpdateurl() {
	   return updateurl;
	}

	public void setUpdateurl(String value) {
	   this.updateurl = value;
	}

	/**
	 * The installable units that will be installed for this node.
	 */
	@XmlElementWrapper
	@XmlElement(name="iu")
	private List<Iu> ius;

	public List<Iu> getIus() {
	   return ius;
	}

	public void setIus(List<Iu> value) {
	   this.ius = value;
	}

	/**
	 * The supported platforms under which this plug-in node can be installed.
	 */
	@XmlElementWrapper
	@XmlElement(name="platform")
	private List<String> platforms;

	public List<String> getPlatforms() {
	   return platforms;
	}

	public void setPlatforms(List<String> value) {
	   this.platforms = value;
	}

}