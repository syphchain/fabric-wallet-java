package com.digcredit.blockchain.fabric.wallet.impl;

import com.digcredit.blockchain.fabric.wallet.Contract;
import com.digcredit.blockchain.fabric.wallet.Gateway;
import com.digcredit.blockchain.fabric.wallet.Network;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Network
 */
public class DefaultNetwork implements Network {
    private static Logger logger = LoggerFactory.getLogger(DefaultNetwork.class);

    private Gateway gateway;
    private Channel channel;

    public DefaultNetwork(Gateway gateway, Channel channel) throws TransactionException, InvalidArgumentException {
        logger.debug("in DefaultNetwork");
        this.gateway = gateway;
        this.channel = channel;

        if (!this.channel.isInitialized()) {
            this.channel.initialize();
            logger.debug("initialize channel: {} succeed", channel.getName());
        }
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public Contract getContract(ChaincodeID chaincodeId, String namespace) {
        // Todo
        return null;
    }
}
