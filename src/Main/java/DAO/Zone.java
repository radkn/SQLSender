package DAO;

import Main.ISendable;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Object of this class corresponds to one record of table Zone
 */
public class Zone extends AbstractSendableRecord implements ISendable {
    private int id;
    private String scene_id;
    private String zoneTitle;
    private String uid;
    private Timestamp datatime_start;
    private Timestamp datatime_end;
    private int datatime_delay;
    private int type;
    private Timestamp time_stamp;
    private boolean transmitted;
    private String nameOfTable = "Zone";

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

    public Timestamp getDatatime_start() {
        return datatime_start;
    }

    public void setDatatime_start(Timestamp datatime_start) {
        this.datatime_start = datatime_start;
    }

    public Timestamp getDatatime_end() {
        return datatime_end;
    }

    public void setDatatime_end(Timestamp datatime_end) {
        this.datatime_end = datatime_end;
    }

    public int getDatatime_delay() {
        return datatime_delay;
    }

    public void setDatatime_delay(int datatime_delay) {
        this.datatime_delay = datatime_delay;
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
        return "{\"zoneTitle\":\"" + zoneTitle
                + "\",\"uid\":\"" + uid
                + "\",\"datetime_start\":\"" + sendFormat.format(new java.util.Date(datatime_start.getTime()))
                + "\",\"datetime_end\":\"" + sendFormat.format(new java.util.Date(datatime_end.getTime()))
                + "\",\"datetime_delay\":\"" + datatime_delay
                + "\",\"type\":\"" + type
                + "\"}";
    }

}
