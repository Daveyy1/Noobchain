import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

    public Wallet(){
        generateKeyPair();
    }

    /**
     * This method uses Elliptic Curve Cryptography to generate a secure Key pair consisting of a private and public key
     */
    public void generateKeyPair(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            // initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            // set the public and private keys from the keypair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // returns balance and stores the UTXOs owned by this wallet in this.UTXOs
    public float getBalance(){
        float total = 0;
        for(Map.Entry<String, TransactionOutput> entry : Noobchain.UTXOs.entrySet()){
            TransactionOutput UTXO = entry.getValue();
            if(UTXO.isMine(publicKey)){
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }
        return total;
    }

    // generates and returns a new transaction from this wallet
    public Transaction sendFunds(PublicKey recipient, float value){
        if(getBalance() < value){
            System.out.println("Insufficient funds, Transaction denied. ");
            return null;
        }

        // create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for(Map.Entry<String, TransactionOutput> entry : UTXOs.entrySet()){
            TransactionOutput UTXO = entry.getValue();
            inputs.add(new TransactionInput(UTXO.id));
            total += UTXO.value;
            if(total > value){
                break;
            }
        }

        Transaction newTransaction = new Transaction(this.publicKey, recipient, value, inputs);
        newTransaction.generateSignature(this.privateKey);

        for(TransactionInput i : inputs){
            UTXOs.remove(i.transactionOutputId);
        }
        return newTransaction;
    }
}
