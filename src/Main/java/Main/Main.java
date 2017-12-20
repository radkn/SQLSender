package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.Timer;
import DAO.Line;
import DAO.Zone;

public class Main {

    public static void main(String[] args){
        String parametersAddress = "parameters/parameters.xml";
        XMLwriterReader<Parameters> reader = new XMLwriterReader(parametersAddress);
        //here we get our parameters from .xml file
        Parameters param = reader.ReadFile(Parameters.class);

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


        Timer timerSendData = new Timer(param.getCheckTransmittedPeriod(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean sendSuccess;
                //here we send new Lines data to server
                long countL = NVToServer.getCountOfLines(); //records with transmitted=false
                while (countL > 0) {
                    if (countL >= param.getOnePackOfStrings())
                        sendSuccess = NVToServer.sendLines(param.getOnePackOfStrings());

                    else
                        sendSuccess = NVToServer.sendLines(countL);
                    System.out.println("Lines success: " + sendSuccess);
                    countL = NVToServer.getCountOfLines();
                }

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
