package Main;
/**
 * Created by July on 22.11.2017.
 */
public class Parameters {
    private String DB_URL = "jdbc:mysql://localhost:3306/NewVision";
    private String DB_USER = "mysql";
    private String DB_PASSWORD = "mysql";
    private boolean transmitted = false;
    private int numberOfStringsLines = 6366;
    private int numberOfStringsZones = 60243;
    private int onePackOfStrings = 2000;

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

    public boolean getTransmitted(){return transmitted;}

    public int getNumberOfStringsLines(){return numberOfStringsLines;}

    public int getNumberOfStringsZones(){return numberOfStringsZones;}

    public int getOnePackOfStrings(){return onePackOfStrings;}

    public void setNumberOfStringsLines(int number){
        numberOfStringsLines = number;
    }

    public void setNumberOfStringsZones(int number){
        numberOfStringsZones = number;
    }
}