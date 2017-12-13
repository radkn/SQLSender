package Main;
/**
 * Created by July on 22.11.2017.
 */
public class Parameters {
    private String DB_URL = "jdbc:mysql://localhost:3306/NewVision";
    private String DB_USER = "mysql";
    private String DB_PASSWORD = "mysql";
    private String ReserveDB_URL = "jdbc:mysql://localhost:3306/MusarnyaDB";
    private String ReserveDB_USER = "mysql";
    private String ReserveDB_PASSWORD = "mysql";
    private boolean ReserveTransmitted = true;
    private boolean newReserveTable = false;
    private boolean transmitted = false;
    private int onePackOfStrings = 2000;
    private int checkTransmittedPeriod = 100;
    private int numberToSendReserve = 5;

    public Parameters(){
        DB_URL = "jdbc:mysql://localhost:3306/NewVision";
        DB_USER = "mysql";
        DB_PASSWORD = "mysql";
    }

    public Parameters(String DB_URL, String DB_USER, String DB_PASSWORD, boolean transmitted){
        this.DB_URL = DB_URL ;
        this.DB_USER = DB_USER ;
        this.DB_PASSWORD = DB_PASSWORD;
        this.transmitted = transmitted;
    }

    public String getDB_URL(){
        return DB_URL;
    }

    public String getDB_USER(){
        return DB_USER;
    }

    public String getDB_PASSWORD(){
        return DB_PASSWORD;
    }

    public String getReserveDB_URL(){
        return ReserveDB_URL;
    }

    public String getReserveDB_USER(){
        return ReserveDB_USER;
    }

    public String getReserveDB_PASSWORD(){
        return ReserveDB_PASSWORD;
    }

    public boolean getReserveTransmitted(){return ReserveTransmitted;}

    public boolean getNewReserveTable(){return newReserveTable;}

    public boolean getTransmitted(){return transmitted;}

    public int getOnePackOfStrings(){return onePackOfStrings;}

    public int getCheckTransmittedPeriod(){return checkTransmittedPeriod;}

    public int getNumberToSendReserve(){return numberToSendReserve;}
}