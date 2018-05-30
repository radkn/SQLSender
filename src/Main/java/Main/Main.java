package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.Timer;

import DAO.*;

public class Main {

    public static void main(String[] args){
        String parametersAddress = "parameters/parameters.xml";
        XMLwriterReader<Parameters> reader = new XMLwriterReader(parametersAddress);
        //here we get our parameters from .xml file
        Parameters param = reader.ReadFile(Parameters.class);

        Timer timerSendData = new Timer(param.getCheckTransmittedPeriod(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //here we check whether we need to send data to reserve DB
                //if true, we send
                if (SendToReserveDB.getCountOfRecords(Line.class) >= param.getNumberToSendReserve()) {
                    SendToReserveDB.sendLinesToReserve();
                }
                else {
                    System.out.println("There is no reserve Lines data");
                }

                if (SendToReserveDB.getCountOfRecords(Zone.class) >= param.getNumberToSendReserve()) {
                    SendToReserveDB.sendZonesToReserve();
                }
                else {
                    System.out.println("There is no reserve Zones data");
                }

                if (SendToReserveDB.getCountOfRecords(HeatMap.class) >= param.getNumberToSendReserve()) {
                    SendToReserveDB.sendHeatMapToReserve();
                }
                else {
                    System.out.println("There is no reserve HeatMaps data");
                }

                boolean sendSuccess;

                //here we send new Lines data to server
                System.out.println("Start check count of Line...");
                long countL = NVToServer.getCountOfRecords(Line.class); //records with transmitted=false
                while (countL > 0) {
                    if (countL >= param.getOnePackOfStrings())
                        sendSuccess = NVToServer.sendLines(param.getOnePackOfStrings(), param.getUrlLines(), param.getHash());

                    else
                        sendSuccess = NVToServer.sendLines(countL,  param.getUrlLines(), param.getHash());
                    System.out.println("Lines success: " + sendSuccess);
                    countL = NVToServer.getCountOfRecords(Line.class);
                }
                System.out.println("Count of Line checked.");


                System.out.println("Start check count of Zone...");
                //here we send new Zones data to server
                long countZ = NVToServer.getCountOfRecords(Zone.class); //records with transmitted=false
                while (countZ > 0) {
                    if (countZ >= param.getOnePackOfStrings())
                        sendSuccess = NVToServer.sendZones(param.getOnePackOfStrings(),  param.getUrlZones(), param.getHash());
                    else
                        sendSuccess = NVToServer.sendZones(countZ,  param.getUrlZones(), param.getHash());
                    System.out.println("Zones success: " + sendSuccess);
                    countZ = NVToServer.getCountOfRecords(Zone.class);
                }
                System.out.println("Count of Zone checked.");

                System.out.println("Start check count of HeatMap...");
                //here we send new HeatMap data to server
                long countH = NVToServer.getCountOfRecords(HeatMap.class); //records with transmitted=false
                while (countH > 0){
                    if(countH >= param.getOnePackOfStrings())
                        sendSuccess = NVToServer.sendHeatMap(param.getOnePackOfStrings(),  param.getUrlHeatMaps(), param.getHash());
                    else
                        sendSuccess = NVToServer.sendHeatMap(countH,  param.getUrlHeatMaps(), param.getHash());
                    System.out.println("HeatMaps success: " + sendSuccess);
                    countH = NVToServer.getCountOfRecords(HeatMap.class);
                }
                System.out.println("Count of HeatMap checked.");
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
}
