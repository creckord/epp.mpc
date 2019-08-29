package org.eclipse.epp.mpc.core.model;

import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * Information about a published news item for a catalog. This can be used to e.g. integrate a regularly published
 * newsletter with the marketplace wizard.
 *
 * @author Carsten Reckord
 * @noextend This class is not intended to be extended by clients.
 * @noimplement This class is not intended to be implemented by clients.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class News
{
   /**
    * @return a short news title suitable to be presented before the actual news is opened.
    */
   @XmlAttribute
   public String shorttitle;
   /**
    * @return a timestamp for the last update to the news. Any change to the published news should result in an updated
    *         timestamp.
    */
   @XmlAttribute
   public Long timestamp;
   /**
    * @return the URL of the published news item
    */
   @XmlValue
   public URL url;
}