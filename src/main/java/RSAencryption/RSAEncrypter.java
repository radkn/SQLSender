package RSAencryption;
import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

/**
 * Created by Andrew on 09/05/17.
 */
public class RSAEncrypter implements IEncrypter {

    private String modStr = null;

    private RSAEncrypter(){}

    public RSAEncrypter(IPublicKeyProvider provider){
        KeyPair keys = provider.getKeyPair();
        modStr = keys.getModulus();
    }

    /**
     * Splits large text into pieces (chunks) and encrypts it
     * @param plaintext
     * @return
     * @throws Exception
     */
    public String encryptLarge(String plaintext) throws Exception{


        BigInteger modulus = new BigInteger(modStr,16);
        BigInteger pubExp = new BigInteger("010001", 16);

        int bit = modulus.bitLength();
        int chunkSize = (int)Math.ceil(bit / 8) - 11;

        String[] chunks = plaintext.split("(?<=\\G.{" + chunkSize + "})");

//        System.out.println("Plain text: " + plaintext.length());
//        System.out.println("Chunks: " + chunks.length);
//        System.out.println("Chunk size: " + chunkSize);

//        for (int i = 0; i < chunks.length; i++) {
//            System.out.println("Chunk " + i + " is: " + chunks[i].length());
//        }

        String encrypted = "";

        for(String chunk : chunks){
            encrypted += encryptMessage(chunk);
        }

        return encrypted;
    }

    private byte[] encrypt(byte[] b1) throws Exception {

        BigInteger modulus = new BigInteger(modStr,16);
        BigInteger pubExp = new BigInteger("010001", 16);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, pubExp);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // byte[] decodedStr = Base64.decode(key, Base64.DEFAULT);
        byte[] plainText = cipher.doFinal(b1);

        return plainText;
    }

    @Override
    public String encrypt(String message) throws Exception {
        String encrypted = encryptLarge(message);
        return encrypted;
    }

    private String encryptMessage(String message) throws Exception {
        String encrypted = Base64.getEncoder().encodeToString(encrypt(message.getBytes("UTF-8")));
        return encrypted;
    }
}
