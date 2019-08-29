package org.eclipse.epp.mpc.core.jaxb;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SafeURIAdapter extends XmlAdapter<String, URI>
{

   @Override
   public URI unmarshal(String v) throws Exception
   {
      if (v == null || v.isEmpty())
      {
         return null;
      }
      try
      {
         return toURI(v);
      }
      catch (URISyntaxException ex)
      {
         int fragment = v.indexOf("#");
         if (fragment != -1) {
            return new URI("invalid", v.substring(0, fragment), v.substring(fragment+1));
         }
         return new URI("invalid", v, null);
      }
   }

   @Override
   public String marshal(URI v) throws Exception
   {
      return stringValueOf(v);
   }

   public static boolean isInvalid(URI v)
   {
      return "invalid".equals(v.getScheme());
   }

   public static String stringValueOf(URI v)
   {
      if (v == null)
      {
         return null;
      }
      String stringValue;
      if (isInvalid(v))
      {
         stringValue = v.getSchemeSpecificPart();
         String fragment = v.getFragment();
         if (fragment != null) {
            stringValue += "#"+fragment;
         }
      }
      else
      {
         stringValue = v.toString();
      }
      return stringValue;
   }

   private static URI toURI(String s) throws URISyntaxException
   {
      try
      {
         return new URI(s);
      }
      catch (URISyntaxException e)
      {
         int fragment = s.lastIndexOf("#");
         if (fragment != -1) {
            return new URI(null, s.substring(0, fragment), s.substring(fragment+1));
         }
         return new URI(null, s, null);
      }
   }

   private static String encodeQuery(String query)
   {
      return query == null ? null : query.replace(" ", "+"); //$NON-NLS-1$//$NON-NLS-2$
   }

   private static String urlDecode(String path)
   {
      try
      {
         return path == null ? null : URLDecoder.decode(path, "UTF-8"); //$NON-NLS-1$
      }
      catch (UnsupportedEncodingException e)
      {
         //should not be possible
         return path;
      }
   }
}
