package Main;

import java.io.IOException;

/**
*
*
* */
public class Main {
    public static void main(String[] args){
        String parametersAddress = "parameters/parameters.xml";
        XMLwriterReader writer = new XMLwriterReader(parametersAddress);
        Parameters par = new Parameters();
        try {
            writer.WriteFile(par, Parameters.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        NVToServer senderToServer = new NVToServer();
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
            long count = senderToServer.getCountOfLines(); //records with transmitted=0
            while (count > 0) {
                count = senderToServer.getCountOfLines();
                if(count >= param.getOnePackOfStrings() )
                    sendSuccess = senderToServer.sendLines(param.getOnePackOfStrings());
                else
                    sendSuccess = senderToServer.sendLines(count);
                System.out.println("Lines success: " + sendSuccess);
        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            long count = senderToServer.getCountOfZones(); //records with transmitted=0
            int i = 0;
            while (count > 0||i<5) {
                count = senderToServer.getCountOfZones();
                if (count >= param.getOnePackOfStrings())
                    sendSuccess = senderToServer.sendZones(param.getOnePackOfStrings());
                else
                    sendSuccess = senderToServer.sendZones(count);
                System.out.println("Zones success: " + sendSuccess);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
