import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data; // data here will be a message, normally a transaction
    private long timeStamp; // number of milliseconds from 1/1/1970
    private int nonce;

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash(){
        String calculatedhash = StringUtil.applySHA256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        data
        );
        return calculatedhash;
    }

    // difficulty is the number of 0's the target hash must contain at the beginning, typical values for modern currencies
    // like Litecoin start at around 420000
    public void mineBlock(int difficulty){
        String target = new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring(0, difficulty).equals(target)){
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined successfully. : " + hash);
    }
}
