package org.eclipse.epp.mpc.core.model;

import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * Branding information for a marketplace catalog entry.
 *
 * @see Catalog
 * @author Benjamin Muskalla
 * @author Carsten Reckord
 * @noextend This class is not intended to be extended by clients.
 * @noimplement This class is not intended to be implemented by clients.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Wizard
{
   @XmlAttribute
   public String title;
   public URL icon;

   public Tab searchtab;
   public Tab populartab;
   public Tab recenttab;
   @XmlElements({
         @XmlElement(name = "relatedtab"),
         @XmlElement(name = "recommendationtab")
   })
   public Tab relatedtab;
   public Tab featuredmarkettab;
   public FavoritesTab favoritestab;

   public News news;
}
