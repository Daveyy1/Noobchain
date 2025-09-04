import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
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

    // Returns the merkle root of an array of transactions
    public static String getMerkleRoot(ArrayList<Transaction> transactions){

        int count = transactions.size();
        ArrayList<String> previousTreeLayer = new ArrayList<String>();
        for(Transaction t : transactions){
            previousTreeLayer.add(t.transactionId);
        }

        ArrayList<String> treeLayer = previousTreeLayer;
        while(count > 1){
            treeLayer = new ArrayList<String>();
            for(int i = 1; i < previousTreeLayer.size(); i++){
                treeLayer.add(applySHA256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;
    }

    public static String getDifficultyString(int difficulty){
        String target = new String(new char[difficulty]).replace('\0', '0');
        return target;
    }

}
