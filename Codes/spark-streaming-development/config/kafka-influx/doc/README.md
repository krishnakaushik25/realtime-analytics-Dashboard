
# Introduction

This project provides connectors for Kafka Connect to read and write data to Influx DB.

# Running Locally

1. Ensure that you have confluent platform installed. Also ensure that confluent bin directory is in your path
2. Clone or download the project from the following link https://github.com/confluentinc/kafka-connect-influxdb
3. Run mvn clean install

# Running Individual IntegrationTest Locally

1. Ensure that you have confluent platform installed. Also ensure that confluent bin directory is in your path
2. Ensure that you have `InfluxDB` running locally. We can use [docker](https://hub.docker.com/_/influxdb) to bring `InfluxDB` setup locally
3. Clone or download the project from the following link https://github.com/confluentinc/kafka-connect-influxdb
4. Run integration test

# Documentation 

Documentation on the connector is hosted on Confluent's
[docs site](https://docs.confluent.io/current/connect/kafka-connect-influxdb/).

## InfluxDBSinkConnector

The InfluxDBSinkConnector is used to write data from a kafka topic to an InfluxDB host. When there are more than one record in a batch that have the same measurement, time, and tags, they will be combined to a single point an written to InfluxDB in a batch.

## InfluxDBSourceConnector

The InfluxDBSourceConnector is used to write data from an InfluxDB host to kafka topics .
