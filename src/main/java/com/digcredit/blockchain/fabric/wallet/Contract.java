package com.digcredit.blockchain.fabric.wallet;

/**
 * Represents a smart contract (chaincode) instance in a network.
 * Applications should get a contract instance using the network's
 * [getContract] method.
 * <p>
 * Created by shniu on 2019/1/18 0018.
 */
public interface Contract {

    /**
     * Create an object representing a specific invocation of a transaction
     * function implemented by this contract, and provides more control over
     * the transaction invocation.
     *
     * @param name transaction function name
     * @return A transaction object
     */
    Transaction createTransaction(String name);

    /**
     * Submit a transaction to the ledger. The transaction function name
     * will be evaluated on the endorsing peers and then submitted to the
     * ordering service for committing to the ledger.
     *
     * @param name transaction function name
     * @param args transaction function args
     * @return A transaction response object
     */
    default TransactionResponse submitTransaction(String name, String... args) {
        return createTransaction(name).submit(args);
    }
}
