package jobs.stream;

import jobs.TopicDetails;
import jobs.UserActivity;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import util.UserActivityUtil;

import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UserActivityEventProducer {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "http://localhost:9092");
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put("schema.registry.url", "http://localhost:8081");

        String topic = TopicDetails.consumer_activity.name();

        String userSchema = "{\"name\":\"ConsumerActivity\",\"type\":\"record\",\"fields\":[{\"name\":\"id\",\"type\":\"int\"},{\"name\":\"campaignid\",\"type\":\"int\"},{\"name\":\"orderid\",\"type\":\"int\"},{\"name\":\"total_amount\",\"type\":\"int\"},{\"name\":\"units\",\"type\":\"int\"},{\"name\":\"tags\",\"type\":{\"type\":\"map\",\"values\":\"string\"}}]}";
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(userSchema);


        Producer producer = new KafkaProducer(props);

        while (true) {
            UserActivity activity = UserActivityUtil.generateUserActivity();
            GenericRecord avroRecord = new GenericData.Record(schema);
            avroRecord.put("id", activity.getId());
            avroRecord.put("campaignid", activity.getCampaignId());
            avroRecord.put("orderid", activity.getOrderId());
            avroRecord.put("total_amount", activity.getAmount());
            avroRecord.put("units", activity.getUnits());
            avroRecord.put("tags", Collections.singletonMap("activity", activity.getActivity()));

            System.out.println("Generated customer " + activity.toString());
            long key = new Random().nextInt(10000);

            System.out.println(String.valueOf(key));
            ProducerRecord<Object, Object> record = new ProducerRecord<>(topic, String.valueOf(key), avroRecord);
            Future send = producer.send(record);
            System.out.println(send.get());
            Thread.sleep(1000);
        }
    }
}
