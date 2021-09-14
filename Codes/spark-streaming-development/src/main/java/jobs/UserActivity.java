package jobs;

public class UserActivity {

    private int id;
    private int campaignId;
    private int orderId;
    private int amount;
    private int units;
    private String activity;

    public UserActivity(int id, int campaignId, int orderId, int amount, int units, String activity) {
        this.id = id;
        this.campaignId = campaignId;
        this.orderId = orderId;
        this.amount = amount;
        this.units = units;
        this.activity = activity;
    }

    public UserActivity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
