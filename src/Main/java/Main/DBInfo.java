package Main;

public class DBInfo {
    private String lastCreateLineTable = "Line";
    private String lastCreateZoneTable = "Zone";

    public String getLastCreateLineTable() {
        return lastCreateLineTable;
    }

    public void setLastCreateLineTable(String lastCreateLineTable) {
        this.lastCreateLineTable = lastCreateLineTable;
    }

    public String getLastCreateZoneTable() {
        return lastCreateZoneTable;
    }

    public void setLastCreateZoneTable(String lastCreateZoneneTable) {
        this.lastCreateZoneTable = lastCreateZoneneTable;
    }
}
