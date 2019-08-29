package org.eclipse.epp.mpc.core.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TokensAdapter extends XmlAdapter<String, List<String>> {

   @Override
   public List<String> unmarshal(String string) {
       List<String> tokens = new ArrayList<>();

       for (String s : string.split("[, ]+")) {
           tokens.add(s.trim());
       }

       return tokens;
   }

   @Override
   public String marshal(List<String> strings) {
       StringBuilder sb = new StringBuilder();

       for (String string : strings) {
           if (sb.length() > 0) {
               sb.append(", ");
           }

           sb.append(string);
       }

       return sb.toString();
   }
}