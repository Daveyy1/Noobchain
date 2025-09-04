import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;

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

    // returns true if new transaction could be created
    public boolean processTransaction(){
        if(verifySignature() == false){
            System.out.println("false transaction signature");
            return false;
        }

        // get all transaction inputs (make sure they are unspent)
        for(TransactionInput i : inputs){
            i.UTXO = Noobchain.UTXOs.get(i.transactionOutputId);
        }

        // check if transaction is valid
        if(getInputsValue() < Noobchain.minimumTransaction){
            System.out.println("Insufficient funds: " + getInputsValue());
            return false;
        }

        // generate transaction outputs
        float lefOver = getInputsValue() - value; // get value of inputs then the left over change
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.recipient, value, transactionId)); // send value to recipient
        outputs.add(new TransactionOutput(this.sender, lefOver, transactionId)); // send the left over "change" back to sender

        // add outputs to Unspend list
        for(TransactionOutput o : outputs){
            Noobchain.UTXOs.put(o.id, o);
        }

        // remove transaction inputs from UTXO lists as spent
        for(TransactionInput i : inputs){
            if(i.UTXO == null) continue;
            Noobchain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    // returns sum of input(UTXOs) values
    public float getInputsValue(){
        float sum = 0;
        for(TransactionInput i : inputs){
            if(i.UTXO == null) continue;
            sum += i.UTXO.value;
        }
        return sum;
    }

    // return sum of outputs
    public float getOutputsValue(){
        float sum = 0;
        for(TransactionOutput o : outputs){
            sum += o.value;
        }
        return sum;
    }

}
