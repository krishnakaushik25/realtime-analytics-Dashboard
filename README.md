
# realtime-analytics-Dashboard

## Project Use Case

Build a realtime user friendly e-commerce users analytics Dashboard. By consuming different events such as user clicks, orders, demographics create a dashboard which gives a wholistic view of insights such as , how a campaign is performing country level, gender basis orders count, realtime purchase insights. 

## Approach
- User purchase events in Avro format is produced via Kafka.
- Spark Streaming Framework does join operations on batch and real-time events
of user purchase and demographic type.
- MySql Holds the demographic data such as age, gender, country, etc.
- Spark Streaming Framework consumes these events and generates a variety of
points suitable for time series and dashboarding.
- Kafka connect pushes the events from the Kafka streams to influxDB.
- Grafana connects to different sources like influxDB, MySQL and populates the
graphs.
