package org.eclipse.epp.mpc.core.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class NodeList
{

   /**
    * The number of matches that matched the query, which may not be equal to the number of nodes returned.
    */
   private Integer count;
   /**
    * The nodes that were matched by the query
    */
   @XmlElement(name = "node")
   private List<Node> nodes;

   public Integer getCount()
   {
      return count;
   }

   public void setCount(Integer value)
   {
      this.count = value;
   }

   public NodeList()
   {
      super();
   }

   public List<Node> getNodes()
   {
      return nodes;
   }

   public void setNodes(List<Node> value)
   {
      this.nodes = value;
   }

}