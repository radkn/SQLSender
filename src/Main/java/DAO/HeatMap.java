package DAO;

import Main.ISendable;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Object of this class corresponds to one record of table HeatMap
 */
public class HeatMap extends AbstractSendableRecord implements ISendable {

    private String uid;
    private int id;
    private int x;
    private int y;
    private Timestamp datetime;
    private int type;
    private boolean transmitted;



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
        return "{\"x\":\"" + x
                + "\",\"y\":\"" + y
                + "\",\"datetime\":\"" + sendFormat.format(new java.util.Date(datetime.getTime()))
                + "\",\"type\":\"" + type
                +"\"}";
    }
}
