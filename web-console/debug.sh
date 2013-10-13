export MAVEN_OPTS=-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n

# For A CharacterEncodingFilter sets the body encoding, but not the URI encoding.
You need to set URIEncoding="UTF-8" as an attribute in all your connectors in your Tomcat server.xml. 
See here: http://tomcat.apache.org/tomcat-6.0-doc/config/ajp.html

Or, alternatively, you can set useBodyEncodingForURI="True".

If you're using the maven tomcat plugin, just add this parameter:
mvn -Dmaven.tomcat.uriEncoding=UTF-8 tomcat7:run
