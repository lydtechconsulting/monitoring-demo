# Monitoring Kafka and Postgres using Prometheus and Grafana

Demonstrating monitoring Kafka and Postgres using Prometheus and Grafana.

A Spring Boot application is used to generate Kafka events and perform Postgres operations to demonstrate the metrics capture.

## Monitoring Demo

### Start Containers

Start Kafka, Zookeeper, Kafka exporter, Postgres, Postgres exporter, Prometheus and Grafana:

```
docker-compose up -d
```

The exporters export metrics from Kafka and Postgres respectively for consumption by Prometheus.

### Prometheus

View the exported targets in Prometheus:

1) Navigate to http://localhost:9090/
2) Select the Status dropdown and then Targets

### Grafana

Connect Grafana to the Prometheus data source:

1) Navigate to http://localhost:3000/
2) Login with the default credentials that have autocompleted.
3) Get to Connections / Data sources / Add data source - select Prometheus.
4) Enter the Prometheus URL: http://prometheus:9090 as the Connection URL.
5) Click 'Save & test'.

Import example Kafka dashboard:

1) Navigate to https://grafana.com/grafana/dashboards/7589-kafka-exporter-overview/
2) Download JSON.  (Also available at ./resources/kafka_7589_rev5.json).
3) Under 'Dashboards' select 'Create Dashboard' / 'Import dashboard'.
4) Select 'prometheus' as the datasource and import.

Import example Postgres dashboard:

1) Navigate to https://grafana.com/grafana/dashboards/9628-postgresql-database/
2) Download JSON.  (Also available at ./resources/postgres_9628_rev7.json)
3) Import as above.

### Run Spring Boot Application

The Spring Boot application connects to the Kafka and Postgres instances.  A REST endpoint is provided that causes the application to produce events, consume these events, and write records to the database.

1) Build the application:  `mvn clean install`
2) Run the application: `java -jar target/monitoring-demo-1.0.0.jar`
3) Hit the REST endpoint to generate events, specifying the period to send events for, and the delay in milliseconds between each send:
```
 curl -v -d '{"periodToSendSeconds":60, "delayMilliseconds":100}' -H "Content-Type: application/json" -X POST http://localhost:9001/v1/trigger
``` 

Application logs show:
```
INFO  d.s.TriggerService - Sending events for 60 seconds
INFO  d.s.TriggerService - Total events sent: 224
```

Confirm the total events sent has resulted in this many item's being persisted:
```
curl http://localhost:9001/v1/items/count
```
Example response:
```
{"count":224}
```

Note that the producer `linger.ms` is configured as 3 milliseconds, so applying a delay shorter than this will result in batches of events being produced.
