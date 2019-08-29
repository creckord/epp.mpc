package org.eclipse.epp.mpc.core.model;

import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class FavoritesTab extends Tab
{
   @XmlAttribute
   public URL apiserver;
   @XmlAttribute
   public String apikey;
}