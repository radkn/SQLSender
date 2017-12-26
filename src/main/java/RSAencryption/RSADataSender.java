package RSAencryption;

import Main.DataSender;

/**
 * Created by Andrew on 09/06/17.
 */
public class RSADataSender extends DataSender {


    /**
     * Creating an instance with specified domain address, postfix and location<br>
     * entire URL wil be Domain+Postfix. Example:www.servername.net/example/add-cam
     */
    public RSADataSender() {
        //lies here because it can
    }

    @Override
    public boolean SendData(String jSon, String url) throws Exception {

        String encryptedMessage = null;

        IPublicKeyProvider keyProvider = new PKProvider("rsa/public.txt");

        RSAEncrypter encrypter = new RSAEncrypter(keyProvider);

        encryptedMessage = encrypter.encrypt(jSon);

        return super.SendData(encryptedMessage, url);
    }

    /**
     * encrypts JSON
     * @param jSon - string that is to be encrypted
     * @return
     */
    public String encryptJSON(String jSon){
        String encryptedMessage = null;

        IPublicKeyProvider keyProvider = new PKProvider("rsa/public.txt");

        RSAEncrypter encrypter = new RSAEncrypter(keyProvider);

        try {
            encryptedMessage = encrypter.encrypt(jSon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedMessage;
    }

}
