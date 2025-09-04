import java.util.ArrayList;
import com.google.gson.*;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;

public class Noobchain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>(); // list of all unspent transactions

    public static void main(String[] args) {

        // setup BounceyCastle as the provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // create the new wallets
        walletA = new Wallet();
        walletB = new Wallet();

        // test public and private keys
        System.out.println("Public Key: " + StringUtil.getStringFromKey(walletA.publicKey));
        System.out.println("Private Key: " + StringUtil.getStringFromKey(walletA.privateKey));

        // create a test transaction from walletA to walletB
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 100, null);
        transaction.generateSignature(walletA.privateKey);

        // verify the signature works and verify it with the public key
        System.out.println("Signature verified: " + transaction.verifySignature());

    }

    // checks if the current blockchain is valid, if there were any changes made to the blocks, this method will return false
    public static Boolean isChainValid(){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        // loop through blockchain to check hashes
        for(int i = 1; i < blockchain.size(); i++){
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            // compare the registered hash with the calculated hash
            if(!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("Current Hashes not equal");
                return false;
            } else if (!previousBlock.hash.equals(currentBlock.previousHash)){
                System.out.println("Previous Hashes not equal");
                return false;
            } else if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)){
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}
