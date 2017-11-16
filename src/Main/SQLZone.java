package Main;

public class SQLZone implements ISendable{
    private String zoneTitle;
    private String uid;
    private String datatime_start;
    private String datatime_end;
    private String datatime_seconds;
    private String type;

    SQLZone(String zoneTitle, String uid, String datatime_start, String datatime_end, String datatime_seconds, String type){
        this.zoneTitle = zoneTitle;
        this.uid = uid;
        this.datatime_start = datatime_start;
        this.datatime_end = datatime_end;
        this.datatime_seconds = datatime_seconds;
        this.type = type;
    }

    @Override
    public String toJSON() {
        return "{\"zoneTitle\":\"" + zoneTitle
                + "\",\"uid\":\"" + uid
                + "\",\"datatime_start\":\"" + datatime_start
                + "\",\"datatime_end\":\"" + datatime_end
                + "\",\"datatime_seconds\":\"" + datatime_seconds
                + "\",\"type\":\"" + type
                + "\"}";
    }
}
