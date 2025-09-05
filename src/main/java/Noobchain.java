import java.util.ArrayList;
import com.google.gson.*;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

public class Noobchain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;

    public static Wallet loadedWallet;
    public static Wallet coinbase;

    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
    public static float minimumTransaction = 0.1f;
    public static Transaction genesisTransaction;

    static Scanner scanner = new Scanner(System.in);
    private static int menuChoice;

    public static void main(String[] args) {

        // setup BounceyCastle as the provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // create the coinbase
        coinbase = new Wallet();
        loadedWallet = new Wallet("Default Wallet");

        // create genesis transaction which sends 100 coins to the coinbase
        genesisTransaction = new Transaction(coinbase.publicKey, loadedWallet.publicKey, 100, null);
        genesisTransaction.generateSignature(coinbase.privateKey);
        genesisTransaction.transactionId = "0";
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId));
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        System.out.println("Creating and mining the Genesis Block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        blockchain.add(genesis);
        System.out.println("Genesis block created successfully!\n");

        // Create NavigationMenu instance
        //NavigationMenu menu = new NavigationMenu(new Noobchain());
        
        // Main menu loop
        boolean running = true;
        while(running) {
            NavigationMenu.displayMenu();
            try {
                String input = scanner.nextLine().trim();
                
                // Check if input is empty
                if (input.isEmpty()) {
                    System.out.println("Please enter a valid option.");
                    continue;
                }
                
                menuChoice = Integer.parseInt(input);
                System.out.println(NavigationMenu.clearOutput());
                running = NavigationMenu.runChoice(menuChoice, scanner);
                
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0-9.");
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        // This will only run when user chooses to exit
        System.out.println("\nFinal blockchain validation:");
        isChainValid();
        scanner.close();
    }

    public static Boolean isChainValid(){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>();
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        for(int i = 1; i < blockchain.size(); i++){
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

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

            TransactionOutput tempOutput;
            for(int t=0; t < currentBlock.transactions.size(); t++){
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()){
                    System.out.println("Signature on Transaction: " + t + " is invalid");
                    return false;
                }
                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue() ) {
                    System.out.println("Inputs and outputs do not match on transaction no. " + t);
                    return false;
                }

                for(TransactionInput input : currentTransaction.inputs){
                    tempOutput = tempUTXOs.get(input.transactionOutputId);
                    if(tempOutput == null){
                        System.out.println("Referenced input on Transaction: " + t + " is missing");
                        return false;
                    }
                    if(input.UTXO.value != tempOutput.value){
                        System.out.println("Referenced input on Transaction: " + t + " is invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for(TransactionOutput output : currentTransaction.outputs){
                    tempUTXOs.put(output.id, output);
                }

                if(currentTransaction.outputs.get(0).recipient != currentTransaction.recipient){
                    System.out.println("Transaction " + t + "s recipient is not who it should be");
                    return false;
                }

                if(currentTransaction.outputs.get(1).recipient != currentTransaction.sender){
                    System.out.println("Transaction " + t + "s sender is not who it should be");
                    return false;
                }
            }
        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public void setLoadedWallet(Wallet wallet){
        loadedWallet = wallet;
    }
}