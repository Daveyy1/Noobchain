import java.security.*;
import java.util.ArrayList;

public class Transaction {
    public String transactionId; // the hash of the transaction, unique identifier
    public PublicKey sender; // public key of the sender
    public PublicKey recipient; // public key of the recipient
    public float value; // amount of funds sent in transaction
    public byte[] signature; // unique signature provided by the sender

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0; // count of transactions that have been generated

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs){
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    // calculate the transaction hash (transactionId)
    private String calculateHash(){
        sequence ++;
        return StringUtil.applySHA256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient) +
                        Float.toString(value) +
                        sequence
        );
    }

    // Signs all the data we dont wish to be tampered with
    public void generateSignature(PrivateKey privateKey){
        String data = StringUtil.getStringFromKey(sender) +
                StringUtil.getStringFromKey(recipient) +
                Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    // verify that the data you signed hasnt been tampered with
    public boolean verifySignature(){
        String data = StringUtil.getStringFromKey(sender) +
                StringUtil.getStringFromKey(recipient) +
                Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

}
