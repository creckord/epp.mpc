package org.eclipse.epp.mpc.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class CategoryInfo extends Category
{
   /**
    * @return the number of {@link #getNode() nodes} in this category
    */
   private Integer count;

   public Integer getCount() {
      return count;
   }

   public void setCount(Integer value) {
      this.count = value;
   }
}