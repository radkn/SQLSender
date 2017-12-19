package RSAencryption;
/**
 * Created by Andrew on 09/06/17.
 */
public interface IEncrypter {

    /**
     * This method is used to encrypt a secret message, using any cryptography method
     *
     * @param message secret message to decrypt
     * @return decrypted message
     */
    String encrypt(String message) throws Exception;
}
