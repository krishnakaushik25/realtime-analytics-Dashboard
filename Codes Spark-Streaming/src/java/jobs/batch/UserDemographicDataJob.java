package jobs.batch;

import jobs.UserDemoGraphicData;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import util.DemoGraphicDataUtil;

import java.util.ArrayList;
import java.util.List;

public class UserDemographicDataJob {

    public static void main(String[] args) {

        System.out.println("Creating User Demographic data using mysql and Spark");
        SparkSession spark = SparkSession.builder().
                appName("Batch User Demographic Data").
                master("local").
                getOrCreate();

        List<UserDemoGraphicData> users = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            String country = DemoGraphicDataUtil.getCountry();
            UserDemoGraphicData userDemoGraphicData = new UserDemoGraphicData(
                    i,
                    DemoGraphicDataUtil.getAge(),
                    DemoGraphicDataUtil.getGender(),
                    DemoGraphicDataUtil.getState(country),
                    country
            );
            users.add(userDemoGraphicData);
        }
        Encoder<UserDemoGraphicData> personEncoder = Encoders.bean(UserDemoGraphicData.class);
        Dataset<UserDemoGraphicData> userDemoGraphDataSet = spark.createDataset(users, personEncoder);

        userDemoGraphDataSet.write().format("jdbc").
                option("url", "jdbc:mysql://localhost:3306")
                .option("dbtable", "users.userdemographics")
                .option("user", "root")
                .option("password", "example").mode("overwrite")
                .save();


        System.out.println("Data Creation is completed");
    }

}
