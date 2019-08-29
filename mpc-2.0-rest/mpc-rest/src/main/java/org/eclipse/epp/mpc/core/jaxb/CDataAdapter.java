package org.eclipse.epp.mpc.core.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CDataAdapter extends XmlAdapter<String, String>
{
   @Override
   public String unmarshal( String s )
   {
       return s;
   }

   @Override
   public String marshal( String s )
   {
       return "<![CDATA[" + s + "]]>";
   }
}
