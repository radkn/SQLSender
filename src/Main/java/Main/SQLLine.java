package Main;

public class SQLLine implements ISendable {
    private String lineTitle;
    private String uid;
    private String dataTime;
    private String status;
    private String type;

    SQLLine(String lineTitle, String uid, String dataTime, String status, String type){
        this.lineTitle = lineTitle;
        this.uid = uid;
        this.dataTime = dataTime;
        this.status = status;
        this.type = type;
    }

    @Override
    public String toJSON() {
        return "{\"lineTitle\":\"" + lineTitle
                + "\",\"uid\":\"" + uid
                + "\",\"dataTime\":\"" + dataTime
                + "\",\"status\":\"" + status
                + "\",\"type\":\"" + type
                + "\"}";
    }
}
