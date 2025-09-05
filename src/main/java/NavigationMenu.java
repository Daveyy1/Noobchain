import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class NavigationMenu {

    // Remove the static scanner - we'll use the one from Noobchain
    public static Wallet loadedWallet;
    private static String output = "";
    private static Noobchain noobchain;
    private static boolean isFirstWalletLoad = true; // Track if genesis block has been created

    public NavigationMenu(Noobchain noobchain){
        this.noobchain = noobchain;
    }

    public static void displayMenu(){
        output = ""; // Clear output each time
        output += "=== NoobChain Console ===\n" +
                "1. Create New Wallet\n" +
                "2. Load Existing Wallet\n" +
                "3. View Wallet Balance\n" +
                "4. Send Funds\n" +
                "5. Mine Block\n" +
                "6. View Blockchain\n" +
                "7. Validate Blockchain\n" +
                "8. Transaction History\n" +
                "9. Settings\n" +
                "0. Exit\n\n" +
                "Choose an option: ";
        System.out.print(output);
    }

    public static String clearOutput(){
        output = "";
        return "";
    }

    // Pass the scanner from Noobchain to avoid conflicts
    public static boolean runChoice(int choice, Scanner scanner){
        try{
            if(choice < 0 || choice > 9) throw new IOException();
            switch(choice){
                case 0:
                    System.out.println("Exiting NoobChain Console...");
                    return false; // Signal to exit the loop
                case 1:
                    String walletName;
                    System.out.print("Enter Wallet Name: ");
                    walletName = scanner.nextLine();
                    Wallet wallet = new Wallet(walletName);
                    WalletManager.addWallet(wallet); // Use WalletManager method instead
                    System.out.println("New Wallet '" + walletName + "' Created Successfully!");
                    // Loop back to menu
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    return true;
                case 2:
                    int walletIndex;
                    if(isFirstWalletLoad) {
                        System.out.println("First loaded wallet receives 100 coins, so choose wisely...");
                    }
                    System.out.println("Which wallet would you like to load? (Enter index of wallet)\n");
                    try{
                        // Check if wallets exist before calling listWallets
                        if(WalletManager.wallets.isEmpty()) {
                            System.out.println("No wallets created yet.");
                            System.out.println("\nPress Enter to continue...");
                            scanner.nextLine();
                            return true;
                        }
                        
                        WalletManager.listWallets();
                        System.out.print("Enter wallet index: ");
                        walletIndex = scanner.nextInt() - 1;
                        scanner.nextLine(); // Consume newline
                        
                        // Validate index range
                        if(walletIndex < 0 || walletIndex >= WalletManager.wallets.size()) {
                            System.out.println("Invalid wallet index! Please enter a number between 1 and " + WalletManager.wallets.size());
                            System.out.println("\nPress Enter to continue...");
                            scanner.nextLine();
                            return true;
                        }
                        
                        System.out.println("Loading Wallet...");
                        loadedWallet = loadWallet(walletIndex);
                        // Directly update the static field instead of calling instance method
                        Noobchain.loadedWallet = loadedWallet;
                        
                        // Only create genesis block for the first wallet load
                        if(isFirstWalletLoad){
                            Noobchain.createGenesisBlock(loadedWallet);
                            isFirstWalletLoad = false; // Prevent future genesis block creation
                        }
                        
                        System.out.println("Wallet '" + loadedWallet.walletName + "' loaded successfully!");
                        // Loop back to menu
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                        return true;
                    } catch (Exception e){
                        System.out.println("Invalid wallet index!");
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                        return true;
                    }
                case 3:
                    WalletManager.listWalletBalances();
                    System.out.println("Press enter to continue...");
                    scanner.nextLine();
                    return true;
                case 4:
                    if(loadedWallet == null) {
                        System.out.println("No wallet loaded. Please load a wallet first.");
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                        return true;
                    }
                    if(loadedWallet.UTXOs.isEmpty()) {
                        System.out.println("No funds in wallet. Please send funds to this wallet first.");
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                        return true;
                    }

                    WalletManager.listWallets();
                    System.out.print("Enter Recipient Wallet Index: ");
                    int recipientIndex = scanner.nextInt() - 1;

                    if(recipientIndex < 0 || recipientIndex >= WalletManager.wallets.size() || recipientIndex == WalletManager.wallets.indexOf(loadedWallet)) {
                        System.out.println("Invalid wallet index! Please enter a number between 1 and " + WalletManager.wallets.size() + " excluding the current wallet index.");
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                        return true;
                    }

                    Wallet recipientWallet = WalletManager.wallets.get(recipientIndex);
                    System.out.print("Enter amount to send (in coins): ");
                    Float amount = scanner.nextFloat();

                    Block newBlock = new Block(Noobchain.blockchain.getLast().hash);
                    System.out.println("\n Trying to send " + amount + "from " + loadedWallet.walletName + "(" + loadedWallet.getBalance() + ") to wallet '" + recipientWallet.walletName + "'...");
                    newBlock.addTransaction(loadedWallet.sendFunds(recipientWallet.publicKey, amount));
                    Noobchain.addBlock(newBlock);
                    System.out.println("Sender Wallet " + loadedWallet + "Balance: " + loadedWallet.getBalance() + "\n"+
                            "Recipient Wallet " + recipientWallet + "Balance: " + recipientWallet.getBalance() + "\n");
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    return true;
                default:
                    System.out.println("Feature not implemented yet.");
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    return true;
            }
        } catch (IOException e) {
            System.out.println("Invalid Choice!");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return true;
        }
    }

    public static Wallet loadWallet(int index){
        loadedWallet = WalletManager.wallets.get(index);
        return loadedWallet;
    }
}