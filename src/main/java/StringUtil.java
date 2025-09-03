import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

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
}
