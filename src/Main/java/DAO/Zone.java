package DAO;

import Main.ISendable;

public class Zone implements ISendable {
    private String id;
    private String scene_id;
    private String zoneTitle;
    private String uid;
    private String datatime_start;
    private String datatime_end;
    private String datatime_delay;
    private String type;
    private String time_stamp;
    private String transmitted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScene_id() {
        return scene_id;
    }

    public void setScene_id(String scene_id) {
        this.scene_id = scene_id;
    }

    public String getZoneTitle() {
        return zoneTitle;
    }

    public void setZoneTitle(String zoneTitle) {
        this.zoneTitle = zoneTitle;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDatatime_start() {
        return datatime_start;
    }

    public void setDatatime_start(String datatime_start) {
        /**обрезаем два лишних символа с базы (".0")*/
        this.datatime_start = datatime_start.substring(0, datatime_start.length()-2);
    }

    public String getDatatime_end() {
        return datatime_end;
    }

    public void setDatatime_end(String datatime_end) {
        /**обрезаем два лишних символа с базы (".0")*/
        this.datatime_end = datatime_end.substring(0, datatime_end.length()-2);
    }

    public String getDatatime_delay() {
        return datatime_delay;
    }

    public void setDatatime_delay(String datatime_delay) {
        this.datatime_delay = datatime_delay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getTransmitted() {
        return transmitted;
    }

    public void setTransmitted(String transmitted) {
        this.transmitted = transmitted;
    }


    @Override
    public String toJSON() {
        return "{\"zoneTitle\":\"" + zoneTitle
                + "\",\"uid\":\"" + uid
                + "\",\"datatime_start\":\"" + datatime_start
                + "\",\"datatime_end\":\"" + datatime_end
                + "\",\"datatime_delay\":\"" + datatime_delay
                + "\",\"type\":\"" + type
                + "\"}";
    }
}
