import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data; // data here will be a message, normally a transaction
    private long timeStamp; // number of milliseconds from 1/1/1970

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
                        data
        );
        return calculatedhash;
    }
}
