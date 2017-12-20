package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.Timer;

import DAO.HeatMap;
import DAO.Line;
import DAO.Zone;

import java.io.IOException;

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
                    System.out.println("L - true");
                }
                else {
                    System.out.println("There is no reserve Lines data");
                }
                System.out.println("Count L: " + NVToServer.getCountOfLines());
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

                boolean sendSuccess;
                //here we send new Lines data to server
/*
                System.out.println("Start check count of Line...");
                long countL = NVToServer.getCountOfLines(); //records with transmitted=false
                while (countL > 0) {
                    if (countL >= param.getOnePackOfStrings())
                        sendSuccess = NVToServer.sendLines(param.getOnePackOfStrings());
                    else
                        sendSuccess = NVToServer.sendLines(countL);
                    System.out.println("Lines success: " + sendSuccess);
                    countL = NVToServer.getCountOfLines();
                }
                System.out.println("Count of Line checked.");


                System.out.println("Start check count of Zone...");
                //here we send new Zones data to server
                long countZ = NVToServer.getCountOfZones(); //records with transmitted=false
                while (countZ > 0) {
                    if (countZ >= param.getOnePackOfStrings())
                        sendSuccess = NVToServer.sendZones(param.getOnePackOfStrings());
                    else
                        sendSuccess = NVToServer.sendZones(countZ);
                    System.out.println("Zones success: " + sendSuccess);
                    countZ = NVToServer.getCountOfZones();
                }
                System.out.println("Count of Zone checked.");*/
            }
        });
        timerSendData.start();

        //program will never stop
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
