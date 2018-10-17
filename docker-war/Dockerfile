FROM payara/micro

COPY ojdbc6-12.1.0.2.jar /opt/payara5/glassfish/domains/domain1/lib/
COPY javaee-cafe.war $DEPLOY_DIR
COPY prepare-jdbc.asadmin /tmp/