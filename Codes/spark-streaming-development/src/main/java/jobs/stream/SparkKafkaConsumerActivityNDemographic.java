package jobs.stream;

import jobs.TopicDetails;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import za.co.absa.abris.avro.functions;
import za.co.absa.abris.config.AbrisConfig;
import za.co.absa.abris.config.FromAvroConfig;
import za.co.absa.abris.config.ToAvroConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.struct;
import static org.apache.spark.sql.functions.map_concat;
import static org.apache.spark.sql.functions.map;
import static org.apache.spark.sql.functions.lit;


public class SparkKafkaConsumerActivityNDemographic {

    public static void main(String[] args) throws StreamingQueryException, IOException, TimeoutException {

        SparkSession spark = SparkSession.builder().
                master("local").
                appName("realtime spark kafka consumer").
                getOrCreate();

        spark.sparkContext().setLogLevel("ERROR");

        Dataset<Row> userActivityStream = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "http://localhost:9092")
                .option("subscribe", TopicDetails.consumer_activity.name())
                //.option("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
                //.option("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer")
                .option("group.id", "user_activity_grp")
                .option("schema.registry.url", "http://localhost:8081")
                // .option("specific.avro.reader", true)
                .load();

        String jsonFormatSchema_useractivity = "{\"name\":\"ConsumerActivity\",\"type\":\"record\",\"fields\":[{\"name\":\"id\",\"type\":\"int\"},{\"name\":\"campaignid\",\"type\":\"int\"},{\"name\":\"orderid\",\"type\":\"int\"},{\"name\":\"total_amount\",\"type\":\"int\"},{\"name\":\"units\",\"type\":\"int\"},{\"name\":\"tags\",\"type\":{\"type\":\"map\",\"values\":\"string\"}}]}";

        String jsonFormatSchema_useractivityNDemoG = "{\"name\":\"UserActivity\",\"type\":\"record\",\"fields\":[{\"name\":\"id\",\"type\":\"int\"},{\"name\":\"orderid\",\"type\":\"int\"},{\"name\":\"campaignid\",\"type\":\"int\"},{\"name\":\"total_amount\",\"type\":\"int\"},{\"name\":\"units\",\"type\":\"int\"},{\"name\":\"age\",\"type\":\"int\"},{\"name\":\"tags\",\"type\":{\"type\":\"map\",\"values\":\"string\"}}]}";

        FromAvroConfig confluentAvroConfig = AbrisConfig.fromConfluentAvro().
                provideReaderSchema(jsonFormatSchema_useractivity).
                usingSchemaRegistry("http://localhost:8081");

        Dataset<Row> userActivityStreamDes = userActivityStream.
                select(functions.from_avro(col("value"), confluentAvroConfig).as("activity"));

        Dataset<Row> demoGraphicData = spark.read().format("jdbc").
                option("url", "jdbc:mysql://localhost:3306")
                .option("dbtable", "users.userdemographics")
                .option("user", "root")
                .option("password", "example").load();

        demoGraphicData.show();

        Dataset<Row> demoGraphicData2 = demoGraphicData.
                withColumn("country_l", lit("country")).
                withColumn("gender_l", lit("gender")).
                withColumn("state_l", lit("state")).
                withColumn("tags", map(col("country_l"), col("country"),
                        col("gender_l"), col("gender"),
                        col("state_l"), col("state"))).
                select(col("i"), col("age"), col("tags"));

        demoGraphicData2.show();


        Dataset<Row> joinedStream = userActivityStreamDes.
                join(demoGraphicData2, userActivityStreamDes.col("activity.id").
                        equalTo(demoGraphicData2.col("i")));


        Dataset<Row> joinedStream2 = joinedStream.
                withColumn("tags", map_concat(col("activity.tags"), col("tags")));

        Dataset<Row> df4 = joinedStream2.select(col("activity.id").as("id"),
                col("activity.orderid").as("orderid"),
                col("activity.campaignid").as("campaignid"),
                col("activity.total_amount").as("total_amount"),
                col("activity.units").as("units"),
                col("tags").as("tags"),
                col("age").as("age")
        );

        df4.printSchema();

        ToAvroConfig toConfluentAvro = AbrisConfig.
                toConfluentAvro().
                provideAndRegisterSchema(jsonFormatSchema_useractivityNDemoG).
                usingTopicNameStrategy("user_activity_demographics", false).
                usingSchemaRegistry("http://localhost:8081");

        Column allfields = struct(df4.columns()[0],
                Arrays.asList(df4.columns()).subList(1, df4.columns().length).
                toArray(new String[0]));

        Dataset<Row> output = df4.select(functions.to_avro(allfields, toConfluentAvro).as("value"));


        StreamingQuery query = output.writeStream().
                format("kafka").
                option("kafka.bootstrap.servers", "http://localhost:9092").
                option("topic", TopicDetails.user_activity_demographics.name()).
                option("key.serializer", "org.apache.kafka.common.serialization.StringSerializer").
                option("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer").
                option("schema.registry.url", "http://localhost:8081").
                option("checkpointLocation", "/tmp/").
                start();

        //StreamingQuery consoleQuery = df5.writeStream().format("console").start();

        //demoGraphicData.show(100, false);
        //join stream and batch
        //consoleQuery.awaitTermination();
        query.awaitTermination();
        //query.awaitTermination();
    }
}
