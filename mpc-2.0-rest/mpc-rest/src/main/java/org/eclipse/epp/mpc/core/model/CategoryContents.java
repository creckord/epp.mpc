package org.eclipse.epp.mpc.core.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class CategoryContents extends Category
{
   @XmlAttribute
   public String marketid;
   
   /**
    * A list of nodes for this category. Entries in this list are typically not fully realized. They will only have
    * their {@link Node#getId() ids} and {@link Node#getName() names} set. Use
    * {@link MarketplaceService#getNode(Node, org.eclipse.core.runtime.ProgressMonitor) the marketplace service} to
    * retrieve a fully realized node instance from the marketplace server.
    *
    * @return the list of nodes in this category.
    */
   @XmlElement(name="node")
   private List<Node> nodes;

   public List<Node> getNodes() {
      return nodes;
   }

   public void setNodes(List<Node> value) {
      this.nodes = value;
   }

}