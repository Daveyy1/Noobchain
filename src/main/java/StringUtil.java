import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class StringUtil {

    // applies SHA-2566 to a String and returns the resulting hex string
    public static String applySHA256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // apply sha256 to input
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer(); // this will contain the hash as a hex string
            for (int i = 0; i < hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // returns the encoded String from a Signature
    public static String getStringFromKey(java.security.Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // applies ECDSA Signature and returns the result as bytes
    public static byte[] applyECDSASig(PrivateKey privateKey, String input){
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } return output;
    }

    // verify a String signature
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature){
        try{
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
