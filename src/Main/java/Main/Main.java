package Main;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        String parametersAddress = "parameters/parameters.xml";

        try {
            SendToReserveDB.sendAll();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        boolean sendSuccess = false;
        XMLwriterReader<Parameters> reader = new XMLwriterReader(parametersAddress);
        Parameters param = new Parameters();
//here we get our parameters from .xml file
        try {
            param = reader.ReadFile(Parameters.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            long count = NVToServer.getCountOfLines(); //records with transmitted=false
            while (count > 0) {
                count = NVToServer.getCountOfLines();
                if(count >= param.getOnePackOfStrings() )
                    sendSuccess = NVToServer.sendLines(param.getOnePackOfStrings());
                else
                    sendSuccess = NVToServer.sendLines(count);
                System.out.println("Lines success: " + sendSuccess);
        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            long count = NVToServer.getCountOfZones(); //records with transmitted=false
            while (count > 0) {
                count = NVToServer.getCountOfZones();
                if (count >= param.getOnePackOfStrings())
                    sendSuccess = NVToServer.sendZones(param.getOnePackOfStrings());
                else
                    sendSuccess = NVToServer.sendZones(count);
                System.out.println("Zones success: " + sendSuccess);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
