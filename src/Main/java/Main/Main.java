package Main;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        String parametersAddress = "parameters/parameters.xml";
        XMLwriterReader<Parameters> reader = new XMLwriterReader(parametersAddress);
        Parameters param = new Parameters();
        boolean sendSuccess;

        //here we get our parameters from .xml file
        try {
            param = reader.ReadFile(Parameters.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //program will never stop
        //while (true) {

            //here we check whether we need to send data to reserve DB
            //if true, we send
            if(NVToServer.getCountOfLines()>=param.getNumberToSendReserve()) {
                SendToReserveDB.sendLinesToReserve();
            }
            if(NVToServer.getCountOfZones()>=param.getNumberToSendReserve()){
                SendToReserveDB.sendZonesToReserve();
            }

            //here we send new Lines data to server
            long countL = NVToServer.getCountOfLines(); //records with transmitted=false
            while (countL > 0) {
                countL = NVToServer.getCountOfLines();
                if (countL >= param.getOnePackOfStrings())
                    sendSuccess = NVToServer.sendLines(param.getOnePackOfStrings());
                else
                    sendSuccess = NVToServer.sendLines(countL);
                System.out.println("Lines success: " + sendSuccess);
            }

            //here we send new Zones data to server
            long countZ = NVToServer.getCountOfZones(); //records with transmitted=false
            while (countZ > 0) {
                countZ = NVToServer.getCountOfZones();
                if (countZ >= param.getOnePackOfStrings())
                    sendSuccess = NVToServer.sendZones(param.getOnePackOfStrings());
                else
                    sendSuccess = NVToServer.sendZones(countZ);
                System.out.println("Zones success: " + sendSuccess);
            }
        //}
    }
}
