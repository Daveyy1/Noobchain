import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class NavigationMenu {

    // Remove the static scanner - we'll use the one from Noobchain
    public static Wallet loadedWallet;
    private static String output = "";
    private static Noobchain noobchain;
    public static ArrayList<Wallet> wallets = WalletManager.wallets;

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
                    System.out.println("Which wallet would you like to load? (Enter index of wallet)\n");
                    try{
                        WalletManager.listWallets();
                        System.out.print("Enter wallet index: ");
                        walletIndex = scanner.nextInt() - 1;
                        scanner.nextLine(); // Consume newline
                        System.out.println("Loading Wallet...");
                        loadedWallet = loadWallet(walletIndex);
                        noobchain.setLoadedWallet(loadedWallet);
                        System.out.println("Wallet loaded successfully!");
                        // Loop back to menu
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                        return true;
                    } catch (RuntimeException e){
                        System.out.println("No wallets created yet.");
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