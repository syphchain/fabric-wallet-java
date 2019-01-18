package com.digcredit.blockchain.fabric.wallet;

import java.util.Map;

/**
 * Represents a specific invocation of a transaction function, and provide
 * flexibility over how that transaction is invoked.
 * <p>
 * Instances of this interface are stateful. A new instance must be created
 * for each transaction invocation.
 * <p>
 * Created by shniu on 2019/1/18 0018.
 */
public interface Transaction {

    /**
     * Get the transaction name
     *
     * @return transaction name
     */
    String getName();

    /**
     * Get transaction id
     *
     * @return txId
     */
    String getTxId();

    /**
     * Submit a transaction to the ledger.
     *
     * @param args args
     * @return TransactionResponse
     */
    TransactionResponse submit(String... args);

    /**
     * Set transient data
     *
     * @param transientMap transient data
     */
    void setTransient(Map<String, Object> transientMap);

    // evaluate ?
    // void evaluate();
}
