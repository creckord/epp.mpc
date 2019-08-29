package org.eclipse.epp.mpc.core.jaxb;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class FlagToBooleanAdapter extends XmlAdapter<String, Boolean>
{
   private enum FlagValue {
      ZERO("0", false),
      ONE("1", true),
      TRUE("true", true),
      FALSE("false", false),
      YES("yes", true),
      NO("no", false);
      
      private static final FlagValue DEFAULT_TRUE_VALUE = ONE;
      private static final FlagValue DEFAULT_FALSE_VALUE = ZERO;
      private static final Map<String, FlagValue> FLAG_VALUES_BY_KEY = initFlagValuesByKey();
      
      private final String key;
      private final boolean value;
      
      private FlagValue(String key, boolean value)
      {
         this.key = key;
         this.value = value;
      }
      
      private static Map<String, FlagValue> initFlagValuesByKey()
      {
         FlagValue[] values = values();
         Map<String, FlagValue> flagValuesByKey = new HashMap<>(values.length, 1);
         for (FlagValue flagValue : values)
         {
            String key = flagValue.key;
            flagValuesByKey.put(key, flagValue);
         }
         return flagValuesByKey;
      }

      public String key() {
         return key;
      }
      
      public boolean value() {
         return value;
      }
      
      public static FlagValue of(String key) {
         if (key == null || key.isEmpty()) {
            return null;
         }
         FlagValue value = FLAG_VALUES_BY_KEY.get(key);
         if (value != null) {
            return value;
         }
         String normalized = key.toLowerCase().trim();
         value = FLAG_VALUES_BY_KEY.get(normalized);
         if (value == null) {
            throw new IllegalArgumentException("Unknown flag value '"+key+"'");
         }
         return value;
      }

      public static FlagValue of(Boolean value) {
         return value == null ? null : value ? DEFAULT_TRUE_VALUE : DEFAULT_FALSE_VALUE;
      }
      
      public static String toString(Boolean value) {
         FlagValue flagValue = of(value);
         return flagValue == null ? null : flagValue.key();
      }
      
      public static Boolean toBoolean(String value) {
         FlagValue flagValue = of(value);
         return flagValue == null ? null : flagValue.value();
      }
   }
   
   @Override
   public Boolean unmarshal(String s)
   {
      return FlagValue.toBoolean(s);
   }

   @Override
   public String marshal(Boolean c)
   {
      return FlagValue.toString(c);
   }
}