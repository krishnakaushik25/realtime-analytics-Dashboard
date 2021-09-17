package jobs;

public class UserDemoGraphicData {

    private int i;
    private int age;
    private String gender;
    private String state;
    private String country;

    public UserDemoGraphicData(int i, int age, String gender, String state, String country) {
        this.i = i;
        this.age = age;
        this.gender = gender;
        this.state = state;
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UserDemoGraphicData() {
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
