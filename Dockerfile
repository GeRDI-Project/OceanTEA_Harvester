# GeRDI Harvester Image for 'OCEANTEA'

FROM jetty:9.4.7-alpine

COPY \/target\/*.war $JETTY_BASE\/webapps\/oceantea.war

EXPOSE 8080
