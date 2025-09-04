import java.lang.reflect.Array;
import java.util.Date;
import java.util.ArrayList;
import java.util.Objects;

public class Block {

    public String hash;
    public String previousHash;
    private String data; // data here will be a message, normally a transaction
    private long timeStamp; // number of milliseconds from 1/1/1970
    private int nonce;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash(){
        String calculatedhash = StringUtil.applySHA256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
        return calculatedhash;
    }

    // difficulty is the number of 0's the target hash must contain at the beginning, typical values for modern currencies
    // like Litecoin start at around 420000
    public void mineBlock(int difficulty){
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDifficultyString(difficulty);
        while(!hash.substring(0, difficulty).equals(target)){
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined successfully. : " + hash);
    }

    public boolean addTransaction(Transaction transaction){
        // process transaction and check if valid, unless block is genesis block then ignore
        if(transaction == null) return false;
        if(!Objects.equals(previousHash, "0")){
            if(!transaction.processTransaction()){
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }

}
