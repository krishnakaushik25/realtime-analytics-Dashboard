package util;

import jobs.UserActivity;
import util.DemoGraphicDataUtil;
import java.util.*;

public class UserActivityUtil {
    static List<String> activites = new ArrayList<>(Arrays.asList("Click", "purchase"));
    static Random random = new Random();

    public static UserActivity generateUserActivity() {
        return new UserActivity(DemoGraphicDataUtil.getUserId(),
                random.nextInt(20),
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(100),
                activites.get(random.nextInt(activites.size()))
        );
    }

    private static Map<String, String> tags() {
        Map<String, String> tags = new HashMap<>();
        tags.put("activity", activites.get(random.nextInt(activites.size())));
        return tags;
    }
}
