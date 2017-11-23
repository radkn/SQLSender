package DAO;

import Main.ISendable;


public class Line implements ISendable {
    private String id;
    private String scene_id;
    private String lineTitle;
    private String uid;
    private String dataTime;
    private String status;
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

    public String getLineTitle() {
        return lineTitle;
    }

    public void setLineTitle(String lineTitle) {
        this.lineTitle = lineTitle;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        /**обрезаем два лишних символа с базы (".0")*/
        this.dataTime = dataTime.substring(0, dataTime.length()-2);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String toJSON() {
        return "{\"lineTitle\":\"" + lineTitle
                + "\",\"uid\":\"" + uid
                + "\",\"dataTime\":\"" + dataTime
                + "\",\"status\":\"" + status
                + "\",\"type\":\"" + type
                + "\"}";
    }
}
