[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.sabomichal/immutable-xjc-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.sabomichal/immutable-xjc-plugin)
## immutable-xjc
IMMUTABLE-XJC is a JAXB 2.0 XJC plugin for making schema derived classes immutable:

* removes all setter methods
* marks class final
* creates a public constructor with all fields as parameters
* creates a protected no-arg constructor
* marks all fields within a class as final
* wraps all collection like parameters with Collections.unmodifiable or Collections.empty if null (unless -imm-leavecollections option is used)
* optionally creates builder pattern utility classes

Note: Derived classes can be further made serializable using these xjc [customizations](http://docs.oracle.com/cd/E17802_01/webservices/webservices/docs/1.6/jaxb/vendorCustomizations.html#serializable).

### Release notes
#### 1.5
* added an option to leave collections mutable
* added an option to generate public constructors only up to n arguments when builder is used

#### 1.4
* added an option to generate non-public constructors
* added an option to generate additional *withAIfNotNull(A a)* builder methods 

#### 1.3
* builder class copy constructor added

#### 1.2
* builder class now contains initialised collection fields
* added generated 'add' methods to incrementally build up the builder collection fields

#### 1.1.1
* various abstract class compile problems fixed
* same class name builder compile problem fixed

#### 1.1
* complex xsd scenarios fixed
* boolean type default values fixed

#### 1.0.5
* xsd polymorphism compilation problems fixed

### XJC options provided by the plugin
The plugin provides an '-immutable' option which is enabled by adding its jar file to the XJC classpath. When enabled,  one additional option can be used to control the behavior of the plugin. See the examples for further information.

#### -immutable
The '-immutable' option enables the plugin making the XJC generated classes immutable.

#### -imm-builder
The '-imm-builder' option can be used to generate builder like pattern utils for each schema derived class.

#### -imm-inheritbuilder
The '-imm-inheritbuilder' option can be used to generate builder classes that follow the same inheritance hierarchy as their subject classes. In addition to using inheritance, the generated builders follow a simpler naming scheme, using Foo.builder() and Foo.Builder instead of Foo.fooBuilder() and Foo.FooBuilder.

#### -imm-cc
The '-imm-cc' option can only be used together with '-imm-builder' option and it is used to generate builder class copy construstructor, initialising builder with object of given class.

#### -imm-ifnotnull
The '-imm-ifnotnull' option can only be used together with '-imm-builder' option and it is used to add an additional withAIfNotNull(A a) method for all non-primitive fields A in the generated builders.

#### -imm-nopubconstructor
The '-imm-nopubconstructor' option is used to make the constructors of the generated classes non-public.

#### -imm-pubconstructormaxargs
The '-imm-pubconstructormaxargs=n' option is used to generate public constructors with up to n arguments, when -imm-builder is used 

#### -imm-skipcollections
The '-imm-skipcollections' option is used to leave collections mutable

### Usage
#### JAXB-RI CLI
To use the JAXB-RI XJC command line interface simply add the corresponding java archives to the classpath and execute the XJC main class 'com.sun.tools.xjc.Driver'. The following example demonstrates a working command line for use with JDK 1.5 (assuming the needed dependencies are found in the current working directory).
```bash
java -cp activation-1.1.jar:\
           jaxb-api-2.0.jar:\
           stax-api-1.0.jar:\
           jaxb-impl-2.0.5.jar:\
           jaxb-xjc-2.0.5.jar:\
           immutable-xjc-plugin-1.4.jar\
           com.sun.tools.xjc.Driver -d /tmp/src -immutable <schema files>
```
#### Maven
Maven users simply add the IMMUTABLE-XJC plugin as a dependency to a JAXB plugin of choice. The following example demonstrates the use of the IMMUTABLE-XJC plugin with the mojo *jaxb2-maven-plugin*.
```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>jaxb2-maven-plugin</artifactId>
    <dependencies>
        <dependency>
            <groupId>com.github.sabomichal</groupId>
            <artifactId>immutable-xjc-plugin</artifactId>
            <version>1.4</version>
        </dependency>
    </dependencies>
    <executions>
        <execution>
            <phase>generate-sources</phase>
            <goals>
                <goal>xjc</goal>
            </goals>
            <configuration>
                <arguments>
                      <argument>-immutable</argument>
                      <argument>-imm-builder</argument>
                </arguments>
            </configuration>
        </execution>
    </executions>
</plugin>
```

IMMUTABLE-XJC can be used also in contract-first webservice client scenarios with wsimport. The following example demonstrates the usage of the plugin with *jaxws-maven-plugin* mojo.
```xml
<plugin>
    <groupId>org.jvnet.jax-ws-commons</groupId>
    <artifactId>jaxws-maven-plugin</artifactId>
    <dependencies>
        <dependency>
            <groupId>com.github.sabomichal</groupId>
            <artifactId>immutable-xjc-plugin</artifactId>
            <version>1.4</version>
        </dependency>
    </dependencies>
    <executions>
        <execution>
            <phase>generate-sources</phase>
            <goals>
                <goal>wsimport</goal>
            </goals>
            <configuration>
                <wsdlFiles>
                    <wsdlFile>test.wsdl</wsdlFile>
                </wsdlFiles>
                <args>
                    <arg>-B-immutable -B-imm-builder</arg>
                </args>
            </configuration>
        </execution>
    </executions>
</plugin>
```
Next two examples demonstrates the usage of the plugin with CXF *cxf-codegen-plugin* and *cxf-xjc-plugin* mojo.
```xml
<plugin>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-codegen-plugin</artifactId>
    <dependencies>
        <dependency>
            <groupId>com.github.sabomichal</groupId>
            <artifactId>immutable-xjc-plugin</artifactId>
            <version>1.4</version>
        </dependency>
    </dependencies>
    <executions>
        <execution>
            <phase>generate-sources</phase>
            <goals>
                <goal>wsdl2java</goal>
            </goals>
            <configuration>
                <wsdlOptions>
                    <wsdlOption>
                        <wsdl>${basedir}/wsdl/test.wsdl</wsdl>
                        <extraargs>
                            <extraarg>-xjc-immutable</extraarg>
                            <extraarg>-xjc-imm-builder</extraarg>
                        </extraargs>
                    </wsdlOption>
                </wsdlOptions>
            </configuration>
        </execution>
    </executions>
</plugin>
```
```xml
<plugin>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-xjc-plugin</artifactId>
    <dependencies>
        <dependency>
            <groupId>com.github.sabomichal</groupId>
            <artifactId>immutable-xjc-plugin</artifactId>
            <version>1.4</version>
        </dependency>
    </dependencies>
    <executions>
        <execution>
            <phase>generate-sources</phase>
            <goals>
                <goal>xsd2java</goal>
            </goals>
            <configuration>
                <xsdOptions>
                    <xsdOption>
                        <xsd>${basedir}/wsdl/test.xsd</xsd>
                        <extensionArgs>
                            <arg>-immutable</arg>
                            <arg>-imm-builder</arg>
                        </extensionArgs>
                    </xsdOption>
                </xsdOptions>
            </configuration>
        </execution>
    </executions>
</plugin>
```

#### Gradle
The following example demonstrates the use of the IMMUTABLE-XJC plugin with the plugin [gradle-jaxb-plugin](https://plugins.gradle.org/plugin/com.github.jacobono.jaxb).
```groovy
plugins {
    id 'com.github.jacobono.jaxb' version '1.3.5'
}

dependencies {
    jaxb 'com.github.sabomichal:immutable-xjc-plugin:1.4'
    jaxb 'com.sun.xml.bind:jaxb-xjc:2.2.7-b41'
    jaxb 'com.sun.xml.bind:jaxb-impl:2.2.7-b41'
    jaxb 'org.jvnet.jaxb2_commons:jaxb2-basics-ant:1.11.1'
    jaxb 'org.jvnet.jaxb2_commons:jaxb2-basics:1.11.1'
    jaxb 'org.jvnet.jaxb2_commons:jaxb2-basics-annotate:1.0.2'
}

jaxb {
    xsdDir = 'src/main/xsd'
    xjc {
        taskClassname = 'org.jvnet.jaxb2_commons.xjc.XJC2Task'
        generatePackage = 'com.example'
        destinationDir = 'src/main/generated-sources'
        args = ['-Xinheritance', '-Xannotate', '-immutable']
    }
}
```


If you like it, give it a star, if you don't, write an issue.
