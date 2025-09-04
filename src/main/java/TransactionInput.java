public class TransactionInput {
    public String transactionOutputId; // reference to TransactionsOutputs -> transactionId
    public TransactionOutput UTXO; // contains the unspend transaction output (balance)

    public TransactionInput(String transactionOutputId){
        this.transactionOutputId = transactionOutputId;
    }
}
