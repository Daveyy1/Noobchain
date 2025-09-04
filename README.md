Blockchain Project Documentation

This project implements a barebones cryptocurrency blockchain in Java.
It demonstrates fundamental blockchain concepts including cryptographic hashing, digital signatures, proof-of-work consensus, and the UTXO (Unspent Transaction Output) model.
The implementation is educational in nature: it provides a minimal but functional example of how blockchains operate, without networking or persistent storage.

To run the current version, download the code and run "Noobchain.java". This will provide a short demonstration of the project.

1. Noobchain — Main Controller

The Noobchain class is the central orchestrator of the blockchain. It initializes the system, manages blocks and transactions, and validates the chain.

Attributes

blockchain: List of all blocks in the chain.

difficulty: Mining difficulty (number of leading zeros required in a valid hash).

walletA, walletB, coinbase: Wallets used for testing and demonstration.

UTXOs: Global set of unspent transaction outputs.

minimumTransaction: Minimum allowed transaction value.

genesisTransaction: First transaction that bootstraps the blockchain.

Key Methods

main(String[] args) — Sets up the blockchain with the BouncyCastle provider, creates test wallets, establishes the genesis transaction, and runs demonstration transactions.

isChainValid() — Verifies blockchain integrity by checking block hashes, mining difficulty, transaction signatures, and UTXO consistency.

addBlock(Block newBlock) — Mines and adds a block to the chain, ensuring continuity.

2. Block — Blockchain Unit

The Block class represents a single block, linking transactions to the chain through cryptographic hashing.

Attributes

hash, previousHash — Block’s own hash and the hash of the previous block.

merkleRoot — Root hash of all transactions in the block.

transactions — List of transactions in the block.

timeStamp — Block creation time.

nonce — Counter used in proof-of-work mining.

Key Methods

calculateHash() — Generates the block’s SHA-256 hash using previousHash, timestamp, nonce, and Merkle root.

mineBlock(int difficulty) — Runs the proof-of-work algorithm until the hash meets the difficulty requirement.

addTransaction(Transaction tx) — Validates and includes a transaction, updates UTXOs, and recalculates the Merkle root.

3. Transaction — Value Transfer

The Transaction class defines the transfer of value between wallets using signatures and the UTXO model.

Attributes

transactionId — Unique transaction hash.

sender, recipient — Public keys of sender and recipient.

value — Transferred amount.

signature — Sender’s digital signature.

inputs, outputs — Lists of consumed and created UTXOs.

Key Methods

generateSignature(PrivateKey key) — Creates an ECDSA signature with the sender’s private key.

verifySignature() — Confirms transaction authenticity with the sender’s public key.

processTransaction() — Validates inputs, creates outputs, prevents double-spending, and updates UTXOs.

getInputsValue() / getOutputsValue() — Compute total values of inputs and outputs to enforce balance.

4. Wallet — User Account

The Wallet class manages key pairs and user-owned UTXOs, allowing users to hold and send funds.

Attributes

privateKey, publicKey — Cryptographic key pair.

UTXOs — Set of unspent outputs owned by the wallet.

Key Methods

generateKeyPair() — Creates an ECDSA key pair via BouncyCastle.

getBalance() — Computes balance by summing the wallet’s UTXOs.

sendFunds(PublicKey recipient, float value) — Builds and signs a transaction, selects inputs, and creates change outputs.

5. TransactionInput — Input Reference

The TransactionInput class references a UTXO that is being spent.

Attributes

transactionOutputId — ID of the consumed UTXO.

UTXO — The actual referenced output.

6. StringUtil — Utility Functions

The StringUtil class provides cryptographic and helper functions.

Key Methods

applySHA256(String input) — Returns SHA-256 hash of the input.

applyECDSASig(PrivateKey key, String input) — Signs data using ECDSA.

verifyECDSASig(PublicKey key, String data, byte[] sig) — Verifies ECDSA signatures.

getMerkleRoot(ArrayList<Transaction> txs) — Calculates a Merkle root over transactions.

getDifficultyString(int difficulty) — Creates a target string of leading zeros for mining validation.

Conclusion

This project demonstrates a simplified cryptocurrency blockchain with core features:

Block linking via cryptographic hashes.

Proof-of-work mining.

Transaction signing and verification using ECDSA.

Balance management through the UTXO model.

It provides a conceptual foundation for blockchain mechanics and can be extended with networking, persistent storage, and more advanced consensus mechanisms.
