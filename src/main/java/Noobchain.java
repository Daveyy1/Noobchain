import java.util.ArrayList;
import com.google.gson.*;

public class Noobchain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 6;

    public static void main(String[] args) {

        //add blocks to the blockchain
        blockchain.add(new Block("Genesis Block", "0"));
        System.out.println("Trying to Mine block 1... (this may take a while) ");
        blockchain.get(0).mineBlock(difficulty);


        blockchain.add(new Block("Block 2", blockchain.get(blockchain.size()-1).hash));
        System.out.println("Trying to Mine block 2... (this may take a while) ");
        blockchain.get(1).mineBlock(difficulty);


        blockchain.add(new Block("Block 3", blockchain.get(blockchain.size()-1).hash));
        System.out.println("Trying to Mine block 3... (this may take a while) ");
        blockchain.get(2).mineBlock(difficulty);



        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
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
