public class Noobchain {
    public static void main(String[] args) {

        Block GenesisBlock = new Block("Genesis Block", "0");
        System.out.println("Hash for block 1: " + GenesisBlock.hash);

        Block secondBlock = new Block("Second Block", GenesisBlock.hash);
        System.out.println("Hash for block 2: " + secondBlock.hash);

        Block thirdBlock = new Block("Third Block", secondBlock.hash);
        System.out.println("Hash for block 3: " + thirdBlock.hash);

    }
}
