package org.eclipse.epp.mpc.core.jaxb;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SafeURIAdapterTest
{
   private SafeURIAdapter adapter;
   
   @BeforeEach
   public void initAdapter() {
      adapter = new SafeURIAdapter();
   }
   
   @Test
   public void testValidURI() throws Exception {
      URI unmarshalled = adapter.unmarshal("http://example.org/foobar");
      assertEquals(URI.create("http://example.org/foobar"), unmarshalled);
      String marshalled = adapter.marshal(unmarshalled);
      assertEquals("http://example.org/foobar", marshalled);
   }

   @Test
   public void testConvertedURI() throws Exception {
      URI unmarshalled = adapter.unmarshal("http://example.org/foo bar");
      assertEquals(URI.create("http://example.org/foo%20bar"), unmarshalled);
      String marshalled = adapter.marshal(unmarshalled);
      assertEquals("http://example.org/foo%20bar", marshalled);
   }

   @Test
   public void testURIWithEncodingIssues() throws Exception {
      URI unmarshalled = adapter.unmarshal("custom:// .example.org/foo bar?bla=bla bla#123");
      assertEquals(new URI("custom", " .example.org", "/foo bar", "bla=bla bla", "123"), unmarshalled);
      String marshalled = adapter.marshal(unmarshalled);
      assertEquals("custom://%20.example.org/foo%20bar?bla=bla%20bla#123", marshalled);
   }

   @Test
   public void testInvalidURI() throws Exception {
      URI unmarshalled = adapter.unmarshal("://.#aaa#xxx");
      assertEquals(new URI("invalid", "://.", "aaa#xxx"), unmarshalled);
      String marshalled = adapter.marshal(unmarshalled);
      assertEquals("://.#aaa#xxx", marshalled);
   }
   
   @Test
   public void testRelativeURI() throws Exception {
      URI unmarshalled = adapter.unmarshal("/foo/bar");
      assertEquals(new URI(null, null, "/foo/bar", null), unmarshalled);
      String marshalled = adapter.marshal(unmarshalled);
      assertEquals("/foo/bar", marshalled);
   }

   @Test
   public void testRelativeURIWithWrongEncoding() throws Exception {
      URI unmarshalled = adapter.unmarshal("/foo/bar baz/x");
      assertEquals(new URI(null, null, "/foo/bar baz/x", null), unmarshalled);
      String marshalled = adapter.marshal(unmarshalled);
      assertEquals("/foo/bar%20baz/x", marshalled);
   }
}
