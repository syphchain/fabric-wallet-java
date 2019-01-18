package com.digcredit.blockchain.fabric.wallet.gateway;

import com.digcredit.blockchain.fabric.wallet.FabricUser;
import com.digcredit.blockchain.fabric.wallet.Gateway;
import com.digcredit.blockchain.fabric.wallet.Network;
import com.digcredit.blockchain.fabric.wallet.Wallet;
import com.digcredit.blockchain.fabric.wallet.option.GatewayOptions;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A default implementation of Gateway
 * <p>
 * Created by shniu on 2019/1/16 0016.
 */
public class DefaultGateway implements Gateway {
    private static Logger logger = LoggerFactory.getLogger(DefaultGateway.class);

    private Wallet wallet;
    private Map<String, Network> networks;
    private HFClient client;
    private FabricUser currentIdentity;
    private Map<String, HFCAClient> caClients;

    private GatewayOptions gatewayOptions;
    private NetworkConfig networkConfig;

    public DefaultGateway() {
        networks = new HashMap<>();
        caClients = new HashMap<>();
    }

    @Override
    public void connect(NetworkConfig networkConfig, GatewayOptions gatewayOptions) {
        logger.debug("in connect");
        wallet = gatewayOptions.getWallet();

        // instance HFClient
        client = HFClient.createNewInstance();
        try {
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

            currentIdentity = wallet.exports(gatewayOptions.getIdentity());
            if (currentIdentity != null) {
                logger.info("{} identity does not exists. Visit CA Server to download.", gatewayOptions.getIdentity());

                String label = gatewayOptions.getIdentity();
                currentIdentity = new FabricUser(label.substring(0, label.indexOf("@")), label.substring(label.indexOf("@") + 1));

                List<NetworkConfig.CAInfo> caLists = networkConfig.getClientOrganization().getCertificateAuthorities();
                for (NetworkConfig.CAInfo ca : caLists) {
                    HFCAClient caClient = HFCAClient.createNewInstance(ca);
                    caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
                    caClients.putIfAbsent(ca.getName(), caClient);

                    Collection<NetworkConfig.UserInfo> registrars = ca.getRegistrars();
                    for (NetworkConfig.UserInfo registrar : registrars) {
                        Enrollment enrollment = caClient.enroll(registrar.getName(), registrar.getEnrollSecret());
                        registrar.setEnrollment(enrollment);
                    }
                    logger.info("{}", registrars);
                }
            }
            // assert currentIdentity.isEnerolled();
            client.setUserContext(currentIdentity);

            Channel channelFoo = client.loadChannelFromConfig("foo", networkConfig);
            logger.info("channelFoo.isInitialized()={}", channelFoo.isInitialized());
            if (!channelFoo.isInitialized()) {
                channelFoo.initialize();
            }

        } catch (Exception e) {
            logger.error("", e);
        }

        this.networkConfig = networkConfig;
        this.gatewayOptions = gatewayOptions;
    }

    @Override
    public void disconnect() {
        networks.clear();
    }

    @Override
    public Network getNetwork(String channelName) {
        // todo: networks.put()
        return networks.getOrDefault(channelName, null);
    }

    @Override
    public HFClient getClient() {
        return client;
    }

    @Override
    public FabricUser getCurrentIdentity() {
        return currentIdentity;
    }

    @Override
    public GatewayOptions getOptions() {
        return gatewayOptions;
    }
}
