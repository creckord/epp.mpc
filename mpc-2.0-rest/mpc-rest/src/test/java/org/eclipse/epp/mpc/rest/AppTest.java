package org.eclipse.epp.mpc.rest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.epp.mpc.regenerated.Catalog;
import org.eclipse.epp.mpc.regenerated.ObjectFactory;
import org.junit.jupiter.api.Test;

import com.sun.xml.bind.api.JAXBRIContext;

public class AppTest 
{
   @Test
   public void testJAXBContext() throws Exception {
      JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
      String doc = "<catalog title='my-title' selfContained='1'>"
            + "<description>A description</description>"
            + "<icon>http://example.org/icon.png</icon>"
            + "</catalog>";
      JAXBElement<Catalog> unmarshaled = context.createUnmarshaller().unmarshal(new StreamSource(new ByteArrayInputStream(doc.getBytes(StandardCharsets.UTF_8))), Catalog.class);
      assertNotNull(unmarshaled);
      Catalog catalog = unmarshaled.getValue();
      assertNotNull(catalog);
      assertEquals("my-title", catalog.getTitle());
      assertEquals(true, catalog.isSelfContained());
      assertEquals("A description", catalog.getDescription());
      assertEquals(new URI("http://example.org/icon.png"), catalog.getIcon());
   }
}
