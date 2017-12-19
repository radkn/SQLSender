package RSAencryption;
import com.sun.istack.internal.NotNull;

/**
 * Created by Andrew on 09/01/17.
 */
public class KeyPair {
    private String modulus = null;
    private String exponent = null;

    private KeyPair(){    }

    public KeyPair(@NotNull String exponent, @NotNull String modulus){
        this.modulus = modulus;
        this.exponent = exponent;
    }

    public String getModulus() {
        return modulus;
    }

    public String getExponent() {
        return exponent;
    }
}
