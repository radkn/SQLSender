package DAO;

import Main.ISendable;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Object of this class corresponds to one record of table Line
 */
public class Line extends AbstractSendableRecord implements ISendable {
    private int id;
    private String scene_id;
    private String lineTitle;
    private String uid;
    private Timestamp dataTime;
    private int status;
    private int type;
    private Timestamp time_stamp;
    private boolean transmitted;
    private String nameOfTable = "Line";


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Timestamp getDataTime() {
        return dataTime;
    }

    public void setDataTime(Timestamp dataTime) {
        this.dataTime = dataTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Timestamp time_stamp) {
        this.time_stamp = time_stamp;
    }

    public boolean getTransmitted() {
        return transmitted;
    }

    public void setTransmitted(boolean transmitted) {
        this.transmitted = transmitted;
    }

    @Override
    public String toJSON() {
        DateFormat sendFormat = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss");
        return "{\"lineTitle\":\"" + lineTitle
                + "\",\"uid\":\"" + uid
                + "\",\"datetime\":\"" + sendFormat.format(new java.util.Date(dataTime.getTime()))
                + "\",\"status\":\"" + status
                + "\",\"type\":\"" + type
                + "\"}";
    }


}
