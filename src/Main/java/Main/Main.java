package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.Timer;

import DAO.*;

public class Main {
    private static String parametersAddress = "parameters/parameters.xml";
    private static XMLwriterReader<Parameters> reader = new XMLwriterReader(parametersAddress);

    public static void main(String[] args){
        String parametersAddress = "parameters/parameters.xml";
        XMLwriterReader<Parameters> reader = new XMLwriterReader(parametersAddress);
        //here we get our parameters from .xml file
        Parameters param = reader.ReadFile(Parameters.class);

        Timer timerSendData = new Timer(param.getCheckTransmittedPeriod(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SaveSliсeToReserveDB objToSave = new SaveSliсeToReserveDB();
                if (objToSave.getCountOfRecords(Line.class) >= param.getNumberToSendReserve()) {
                    objToSave.sendLinesToReserve();
                    System.out.println("L - true");
                }
                else {
                    System.out.println("There is no reserve Lines data");
                }

                if (objToSave.getCountOfRecords(Zone.class) >= param.getNumberToSendReserve()) {
                    objToSave.sendZonesToReserve();
                    System.out.println("Z - true");
                }
                else {
                    System.out.println("There is no reserve Zones data");
                }

                if (objToSave.getCountOfRecords(HeatMap.class) >= param.getNumberToSendReserve()) {
                    objToSave.sendHeatMapToReserve();
                    System.out.println("H - true");
                }
                else {
                    System.out.println("There is no reserve HeatMaps data");
                }

                //here we check whether we need to send data to reserve DB
                //if true, we send
/*
                if (SendToReserveDB.getCountOfRecords(Line.class) >= param.getNumberToSendReserve()) {
                    SendToReserveDB.sendLinesToReserve();
                    System.out.println("L - true");
                }
                else {
                    System.out.println("There is no reserve Lines data");
                }

                if (SendToReserveDB.getCountOfRecords(Zone.class) >= param.getNumberToSendReserve()) {
                    SendToReserveDB.sendZonesToReserve();
                    System.out.println("Z - true");
                }
                else {
                    System.out.println("There is no reserve Zones data");
                }

                if (SendToReserveDB.getCountOfRecords(HeatMap.class) >= param.getNumberToSendReserve()) {
                    SendToReserveDB.sendHeatMapToReserve();
                    System.out.println("H - true");
                }
                else {
                    System.out.println("There is no reserve HeatMaps data");
                }
*/

                boolean sendSuccess;

                SendNVToServer objToSendServer = new SendNVToServer();

                System.out.println("Start check count of Line...");
                long countL = objToSendServer.getCountOfRecords(Line.class); //records with transmitted=false
                while (countL > 0) {
                    if (countL >= param.getOnePackOfStrings())
                        sendSuccess = objToSendServer.sendLines(param.getOnePackOfStrings());

                    else
                        sendSuccess = objToSendServer.sendLines(countL);
                    System.out.println("Lines success: " + sendSuccess);
                    countL = objToSendServer.getCountOfRecords(Line.class);
                }
                System.out.println("Count of Line checked.");


                System.out.println("Start check count of Zone...");
                //here we send new Zones data to server
                long countZ = objToSendServer.getCountOfRecords(Zone.class); //records with transmitted=false
                while (countZ > 0) {
                    if (countZ >= param.getOnePackOfStrings())
                        sendSuccess = objToSendServer.sendZones(param.getOnePackOfStrings());
                    else
                        sendSuccess = objToSendServer.sendZones(countZ);
                    System.out.println("Zones success: " + sendSuccess);
                    countZ = NVToServer.getCountOfRecords(Zone.class);
                }
                System.out.println("Count of Zone checked.");

                System.out.println("Start check count of HeatMap...");
                //here we send new HeatMap data to server
                long countH = objToSendServer.getCountOfRecords(HeatMap.class); //records with transmitted=false
                while (countH > 0){
                    if(countH >= param.getOnePackOfStrings())
                        sendSuccess = objToSendServer.sendHeatMap(param.getOnePackOfStrings());
                    else
                        sendSuccess = objToSendServer.sendHeatMap(countH);
                    System.out.println("HeatMaps success: " + sendSuccess);
                    countH = objToSendServer.getCountOfRecords(HeatMap.class);
                }
                System.out.println("Count of HeatMap checked.");

                //here we send new Lines data to server
/*
                System.out.println("Start check count of Line...");
                long countL = NVToServer.getCountOfRecords(Line.class); //records with transmitted=false
                while (countL > 0) {
                    if (countL >= param.getOnePackOfStrings())
                        sendSuccess = NVToServer.sendLines(param.getOnePackOfStrings());

                    else
                        sendSuccess = NVToServer.sendLines(countL);
                    System.out.println("Lines success: " + sendSuccess);
                    countL = NVToServer.getCountOfRecords(Line.class);
                }
                System.out.println("Count of Line checked.");


                System.out.println("Start check count of Zone...");
                //here we send new Zones data to server
                long countZ = NVToServer.getCountOfRecords(Zone.class); //records with transmitted=false
                while (countZ > 0) {
                    if (countZ >= param.getOnePackOfStrings())
                        sendSuccess = NVToServer.sendZones(param.getOnePackOfStrings());
                    else
                        sendSuccess = NVToServer.sendZones(countZ);
                    System.out.println("Zones success: " + sendSuccess);
                    countZ = NVToServer.getCountOfRecords(Zone.class);
                }
                System.out.println("Count of Zone checked.");

                System.out.println("Start check count of HeatMap...");
                //here we send new HeatMap data to server
                long countH = NVToServer.getCountOfRecords(HeatMap.class); //records with transmitted=false
                while (countH > 0){
                    if(countH >= param.getOnePackOfStrings())
                        sendSuccess = NVToServer.sendHeatMap(param.getOnePackOfStrings());
                    else
                        sendSuccess = NVToServer.sendHeatMap(countH);
                    System.out.println("HeatMaps success: " + sendSuccess);
                    countH = NVToServer.getCountOfRecords(HeatMap.class);
                }
                System.out.println("Count of HeatMap checked.");
*/
            }
        });
        timerSendData.start();

        //program will not stop until user enters "stop"
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("If you want to stop the program enter 'stop'");
            String stop = sc.nextLine();
            if(stop.equals("stop")){
                timerSendData.stop();
                break;
            }
        }
    }

//    public static void testSendHM(long count){
//
//        boolean sendSuccess = false;
//        DataSender sender = new DataSender();
//        String messageLines = null;
//        List<HeatMap> list = new ArrayList();
//        HeatMapsPars hmList = new HeatMapsPars();
//        SQLList sqlList = new SQLList();
//        try {
//            long time = System.currentTimeMillis();
//            list.addAll(getHeatMaps(count));
//            hmList.addAll(list);
//            sqlList.addAll(list);
//            messageLines = hmList.toJSON();
//            time = System.currentTimeMillis() - time;
//            System.out.println("Read time: " + time);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String jsonLines = "{" +
//                "\"hash\":\"--Julya test--\"," +
//                "\"data\":" + messageLines + "}";
//
//        System.out.println(jsonLines);
//
//    }
//
//    /**
//     * В методе показаны примеры работы с интерфейсом IGenericDAO
//     * (интерфейс работы с базой данных) на примере таблицы Line
//     * @return Список первых n записей таблицы Line
//     * (n указываться как аргумент limit метода daoL.getByTransmittedLimit)
//     * @throws Exception
//     */
//    private static List<HeatMap> getHeatMaps(long count) throws Exception{
//        Parameters param = reader.ReadFile(Parameters.class);
//        //создание фабрики объектов для работы с базой данных
//        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
//        //список для хранения полученых линий с базы данных
//        List<HeatMap> list;
//        //создание подключения к базе
//        try(Connection con = daoFactory.getConnection()){
//            //создание объекта реализующего интерфейс работы с базой данных
//            IGenericDAO daoL = daoFactory.getDAO(con, HeatMap.class);
//            //получение списка с определенным количеством записей таблици в которых параметр transmitted = false
//            list = daoL.getByTransmittedLimit(param.getTransmitted(), count);
//            con.close();
//        }
//        System.out.println("List lines size" + list.size());
//        return list;
//    }
}
