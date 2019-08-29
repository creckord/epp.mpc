package org.eclipse.epp.mpc.core.jaxb;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.epp.mpc.regenerated.ObjectFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.gravitee.maven.plugins.json.schema.generator.mojo.JSONSchemaGeneratorMojo;

public class GenerateJsonSchemaTest
{
   private JSONSchemaGeneratorMojo generator;
   private String buildDirectory;
   private String outputDirectory;
   private List<String> includes;
   private List<String> excludes;
   
   @BeforeEach
   public void initGenerator() {
      generator = new JSONSchemaGeneratorMojo() {
         @Override
         public String getBuildDirectory()
         {
            return buildDirectory;
         }
         
         @Override
         public List<String> getExcludes()
         {
            return excludes;
         }
         
         @Override
         public List<String> getIncludes()
         {
            return includes;
         }
         
         @Override
         public String getOutputDirectory()
         {
            return outputDirectory;
         }
      };
   }
   
   @BeforeEach
   public void initDirectories() throws URISyntaxException {
      String classFilePath = ObjectFactory.class.getName().replace('.', '/')+".class";
      Path p = Paths.get(classFilePath);
      int packageSegments = p.getNameCount() - 1;
      URL resource = this.getClass().getClassLoader().getResource(classFilePath);
      assertEquals("file", resource.getProtocol());
      File classFile = new File(resource.toURI());
      File dir = classFile.getParentFile();

      for (int i=0; i < packageSegments; i++) {
         dir = dir.getParentFile();
         assertNotNull(dir);
      }
      File buildDir = dir;
      File baseDir = null;
      
      for (;dir != null;dir = dir.getParentFile()) {
         if (new File(dir, "pom.xml").exists()) {
            baseDir = dir;
            break;
         }
      }
      assertNotNull(baseDir);
      File outDir = new File(baseDir, "target/generated-resources/json");
      assertTrue(outDir.mkdirs() || outDir.isDirectory());
      
      this.buildDirectory = buildDir.getAbsolutePath();
      this.outputDirectory = outDir.getAbsolutePath();
      
      includes = Collections.singletonList(p.subpath(0, packageSegments).toString().replace(File.separatorChar, '/')+"/*.class");
      excludes = Arrays.asList("*$Builder.class", "**/package-info.class", "**/ObjectFactory.class");
   }

   @Test
   public void generateJsonSchema() throws MojoExecutionException, MojoFailureException {
      generator.execute();
   }
}
