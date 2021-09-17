package util;

import java.util.*;

public class DemoGraphicDataUtil {

    static Random random = new Random();
    public static int getUserId() {
        return random.nextInt(100000);
    }

    public static String getCountry() {
        List<String> coutries = new ArrayList<>(Arrays.asList("USA",
                "JAPAN",
                "CANADA",
                "INDIA",
                "SOUTH_AFRICA"
        ));
        return coutries.get(random.nextInt(coutries.size()));
    }

    public static String getState(String country) {
        List<String> usaStates = Arrays.asList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri");
        List<String> indiaStates = Arrays.asList("Andhra", "Amaravati", "Kurnool", "Arunachal", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal", "Dharamshala", "Jharkhand", "Karnataka", "Kerala", "Madhya", "Maharashtra", "Nagpur", "Manipur");
        List<String> canadaStates = Arrays.asList("Ontario", "Nova_Scotia", "Manitoba", "Prince_Edward", "Newfoundland_Labrador");
        List<String> southAfricaStates = Arrays.asList("Eastern_Cape", "Free_State", "Gauteng", "KwaZulu-Natal", "Limpopo", "Mpumalanga", "Northern_Cape", "North_West");
        List<String> japanStates = Arrays.asList("Hokkaido", "Tohoku", "Kanto", "Chubu", "Kinki_Kansai", "Chugoku", "Shikoku", "Kyushu");

        Map<String, List<String>> countryDet = new HashMap<>();
        countryDet.put("USA", usaStates);
        countryDet.put("JAPAN", japanStates);
        countryDet.put("CANADA", canadaStates);
        countryDet.put("INDIA", indiaStates);
        countryDet.put("SOUTH_AFRICA", southAfricaStates);

        List<String> states = countryDet.get(country);
        return states.get(random.nextInt(states.size()));
    }

    public static int getAge() {
        return random.nextInt(60);
    }

    public static String getGender() {
        List<String> gender = new ArrayList<>(Arrays.asList(
                "MALE", "FEMALE"));
        return gender.get(random.nextInt(gender.size()));
    }
}
