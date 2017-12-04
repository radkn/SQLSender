package Main;
/**
 * Created by July on 22.11.2017.
 */
public class Parameters {
    private String DB_URL = "jdbc:mysql://localhost:3306/NewVision";
    private String DB_USER = "mysql";
    private String DB_PASSWORD = "mysql";
    private boolean transmitted = false;
    private int numberOfStrings = 30;
    private int onePackOfStrings = 10;

    public Parameters(){
        DB_URL = "jdbc:mysql://localhost:3306/NewVision";
        DB_USER = "mysql";
        DB_PASSWORD = "mysql";
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

    public boolean getTtansmitted(){return transmitted;}

    public int getNumberOfStrings(){return numberOfStrings;}

    public int getOnePackOfStrings(){return onePackOfStrings;}
}