package org.eclipse.epp.mpc.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.epp.mpc.core.jaxb.FlagToBooleanAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Tab
{
   @XmlAttribute
   @XmlJavaTypeAdapter(FlagToBooleanAdapter.class)
   public Boolean enabled;

   @XmlValue
   public String name;
}