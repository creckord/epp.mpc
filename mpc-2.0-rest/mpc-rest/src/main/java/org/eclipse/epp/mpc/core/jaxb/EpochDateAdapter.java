package org.eclipse.epp.mpc.core.jaxb;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class EpochDateAdapter extends XmlAdapter<Long, Date>
{
   @Override
   public Date unmarshal( Long s )
   {
       return s == null ? null : new Date(s);
   }

   @Override
   public Long marshal( Date c )
   {
       return c == null ? null : c.getTime();
   }
}