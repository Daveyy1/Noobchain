import java.util.ArrayList;

public class WalletManager {
    public static ArrayList<Wallet> wallets = new ArrayList<>(); // Initialize the ArrayList

    public static void listWallets() throws RuntimeException{
        if(wallets.isEmpty()) {
            throw new RuntimeException();
        }
        String output = "";
        for(int i = 0; i < wallets.size(); i++){
            Wallet w = wallets.get(i);
            output += i+1 + ": " + w.walletName + " (Balance: " + w.getBalance() + ")\n";
        }
        System.out.println(output);
    }
    
    // Optional: Add a method to add wallets safely
    public static void addWallet(Wallet wallet) {
        wallets.add(wallet);
        System.out.println("Wallet added to manager: " + wallet.walletName);
    }
}