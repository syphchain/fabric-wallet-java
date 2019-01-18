package com.digcredit.blockchain.fabric.wallet;

import org.hyperledger.fabric.sdk.ChaincodeID;

import java.nio.channels.Channel;

/**
 * A Network represents the set of peers in a Fabric network.
 * <p>
 * Created by shniu on 2019/1/18 0018.
 */
public interface Network {

    /**
     * Get the underlying channel object representation of this network
     *
     * @return A channel
     */
    Channel getChannel();

    /**
     * Get an instance of a contract (chaincode) on the current network
     *
     * @param chaincodeId chaincodeId
     * @param namespace   namespace of the contract, default is empty string
     * @return instance of a contract on the current network
     */
    Contract getContract(ChaincodeID chaincodeId, String namespace);
}
