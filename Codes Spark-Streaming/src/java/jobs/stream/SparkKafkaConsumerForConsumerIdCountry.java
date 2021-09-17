package jobs.stream;

import jobs.TopicDetails;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;
import za.co.absa.abris.avro.functions;
import za.co.absa.abris.config.AbrisConfig;
import za.co.absa.abris.config.FromAvroConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import static org.apache.spark.sql.functions.col;


public class SparkKafkaConsumerForConsumerIdCountry {

    public static void main(String[] args) throws StreamingQueryException, IOException, TimeoutException {

        SparkSession spark = SparkSession.builder().
                master("local").
                appName("realtime spark kafka consumer").
                getOrCreate();

        spark.sparkContext().setLogLevel("ERROR");

        Dataset<Row> input = spark
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

        String jsonFormatSchema = new String(Files.readAllBytes(Paths.get("./src/main/resources/userActivity.avsc")));

        FromAvroConfig fromAvroConfig1 = AbrisConfig.fromConfluentAvro().
                provideReaderSchema(jsonFormatSchema).
                usingSchemaRegistry("http://localhost:8081");

        Dataset<Row> df2 = input.select(functions.from_avro(col("value"), fromAvroConfig1).as("activity"));

        /*
        here i will try integrating with mysql
         */
        Dataset<Row> demoGraphicData = spark.read().format("jdbc").
                option("url", "jdbc:mysql://localhost:3306")
                .option("dbtable", "users.userdemographics")
                .option("user", "root")
                .option("password", "example").load();

        demoGraphicData.show();

        Dataset<Row> df3 = df2.join(demoGraphicData, df2.col("activity.id").
                equalTo(demoGraphicData.col("i")));

        Dataset<Row> df4 = df3.groupBy("country").count();


        //select count(*),country from df4 group by country
        //japan 10
        //india 2
        //canada 3


        StreamingQuery query = df4.writeStream().
                outputMode("complete").
                format("console").
                start();


        StreamingQuery query2 = df4.writeStream().
                outputMode("complete").
                trigger(Trigger.ProcessingTime("10 seconds")).
                foreachBatch(new VoidFunction2<Dataset<Row>, Long>() {
                    @Override
                    public void call(Dataset<Row> rowDataset, Long aLong) throws Exception {
                        System.out.println("inside foreachbatch");
                        rowDataset.write().format("jdbc").
                                option("url", "jdbc:mysql://localhost:3306")
                                .option("dbtable", "users.countryAgg")
                                .option("user", "root")
                                .option("password", "example").mode("overwrite").save();
                    }
                }).start();

        //demoGraphicData.show(100, false);
        //join stream and batch
        query2.awaitTermination();
        //query.awaitTermination();
    }
}
