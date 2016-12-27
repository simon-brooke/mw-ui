FROM tomcat:alpine
COPY target/microworld.war $CATALINA_HOME/webapps/

