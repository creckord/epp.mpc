package org.eclipse.epp.mpc.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public abstract class Identifiable
{

   /**
    * @return this element's unique id in its {@link Catalog catalog}
    */
   @XmlAttribute
   private String id;
   /**
    * @return the URL from which this element can be re-retrieved from the marketplace server
    */
   @XmlAttribute
   private String url;

   public String getId()
   {
      return id;
   }

   public void setId(String value)
   {
      this.id = value;
   }

   public Identifiable()
   {
      super();
   }

   public String getUrl()
   {
      return url;
   }

   public void setUrl(String value)
   {
      this.url = value;
   }

}