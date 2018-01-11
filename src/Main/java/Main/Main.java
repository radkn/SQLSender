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
        //send date before pause
        sendData();
        Timer timerSendData = new Timer(param.getCheckTransmittedPeriod(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendData();
            }
        });
        timerSendData.start();

        //program will not stop until user enters "stop"
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.err.println("If you want to stop the program enter 'stop'");
            String stop = sc.nextLine();
            if(stop.equals("stop")){
                timerSendData.stop();
                break;
            }
        }
    }

    /**
     * Method execute save slice of DB to reserve DB
     * and send data to server
     * with non final classes SendNVToServer and SaveSliceToReserveDB
     * without static methods
     */
    public static void sendData(){

        String parametersAddress = "parameters/parameters.xml";
        XMLwriterReader<Parameters> reader = new XMLwriterReader(parametersAddress);
        //here we get our parameters from .xml file
        Parameters param = reader.ReadFile(Parameters.class);

        //here we check whether we need to send data to reserve DB
        //if true, we send
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


        //here we send new Lines data to server
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
            countZ = objToSendServer.getCountOfRecords(Zone.class);
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


    }

}
