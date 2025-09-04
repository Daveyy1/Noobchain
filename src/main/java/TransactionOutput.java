import java.security.*;

public class TransactionOutput {
    public String id;
    public PublicKey recipient; // aka new owner of the sent coins
    public float value; // amount sent
    public String parentTransactionId; // id of the transaction that this output was created in

    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId){
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySHA256(StringUtil.getStringFromKey(recipient) + Float.toString(value) + parentTransactionId);
    }

    // check if coin belongs to you
    public boolean isMine(PublicKey publicKey){
        return (publicKey == recipient);
    }
}
