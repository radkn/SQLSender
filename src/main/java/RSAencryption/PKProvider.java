package RSAencryption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Andrew on 09/06/17.
 */
public class PKProvider implements IPublicKeyProvider {

    private PKProvider(){}

    private KeyPair key = null;

    public PKProvider(String file){

        String modulusStr = null;
        modulusStr = readUsingFiles(file);

        modulusStr = modulusStr.replaceAll(" |:","");
        modulusStr = modulusStr.replaceAll("\n","");
        modulusStr = modulusStr.replaceAll("\r","");

        key = new KeyPair("010001", modulusStr);

    }

    @Override
    public KeyPair getKeyPair() {
        return key;
    }

    private String readUsingFiles(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
