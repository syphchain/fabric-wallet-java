package com.digcredit.blockchain.fabric.wallet;

import com.digcredit.blockchain.fabric.wallet.option.GatewayOptions;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;

/**
 * This module provides a higher level API for interacting with smart contracts, and is the recommended API for
 * client applications to interact with smart contracts deployed to a Hyperledger Fabric blockchain network.
 * <p>
 * Created by shniu on 2019/1/16 0016.
 */
public interface Gateway {

    /**
     * Connect to the gateway with a networkConfig and connection option
     */
    void connect(NetworkConfig networkConfig, GatewayOptions gatewayOptions);

    /**
     * Clean up and disconnect this Gateway connection
     */
    void disconnect();

    /**
     * Returns an instance representing a network
     *
     * @param channelName channel name
     * @return Network instance
     */
    Network getNetwork(String channelName);

    /**
     * Get the underlying Client object instance
     *
     * @return HFClient
     */
    HFClient getClient();

    /**
     * Get the current identity
     *
     * @return the current identity used by the gateway
     */
    FabricUser getCurrentIdentity();

    /**
     * Returns the set of options associated with the Gateway connection
     */
    GatewayOptions getOptions();
}
