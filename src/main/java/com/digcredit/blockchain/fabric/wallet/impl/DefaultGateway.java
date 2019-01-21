package com.digcredit.blockchain.fabric.wallet.impl;

import com.digcredit.blockchain.fabric.wallet.FabricUser;
import com.digcredit.blockchain.fabric.wallet.Gateway;
import com.digcredit.blockchain.fabric.wallet.Network;
import com.digcredit.blockchain.fabric.wallet.Wallet;
import com.digcredit.blockchain.fabric.wallet.option.GatewayOptions;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        // networks = new HashMap<>();
        networks = new ConcurrentHashMap<>(8);
        caClients = new HashMap<>();
    }

    /**
     * 1. 实例化client（setUserContext/setCryptoSuite）
     * 2. 实例化ca client
     * 3. enroll registrar
     * 4. 初始化currentIdentity, 发送交易时使用
     */
    @Override
    public void connect(NetworkConfig networkConfig, GatewayOptions gatewayOptions) {
        logger.debug("in connect");
        wallet = gatewayOptions.getWallet();

        // instance HFClient
        client = HFClient.createNewInstance();
        try {
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

            currentIdentity = wallet.exports(gatewayOptions.getIdentity());
            if (currentIdentity != null) {  // Todo: == null
                logger.info("{} identity does not exists. Visit CA Server to download.", gatewayOptions.getIdentity());

                String label = gatewayOptions.getIdentity();
                currentIdentity = new FabricUser(label.substring(0, label.indexOf("@")), label.substring(label.indexOf("@") + 1));

                // get peer admin
                NetworkConfig.UserInfo orgPeerAdmin = networkConfig.getClientOrganization().getPeerAdmin();

                // register other user
                // ca 配置的可能是个列表，可以考虑一个一个试，或者简单处理取第一个
                List<NetworkConfig.CAInfo> caLists = networkConfig.getClientOrganization().getCertificateAuthorities();
                assert caLists.size() > 0;
                HFCAClient caClient = HFCAClient.createNewInstance(caLists.get(0));
                caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
                caClients.putIfAbsent(caLists.get(0).getName(), caClient);

                Collection<NetworkConfig.UserInfo> registrars = caLists.get(0).getRegistrars();
                // 一般情况下配置一个即可
                for (NetworkConfig.UserInfo registrar : registrars) {
                    Enrollment enrollment = caClient.enroll(registrar.getName(), registrar.getEnrollSecret());
                    registrar.setEnrollment(enrollment);
                }
                assert registrars.size() > 0;

                /*Enrollment enrollment = caClient.enroll(orgPeerAdmin.getName(), orgPeerAdmin.getEnrollSecret());
                orgPeerAdmin.setEnrollment(enrollment);*/

                /*for (NetworkConfig.CAInfo ca : caLists) {
                    HFCAClient caClient = HFCAClient.createNewInstance(ca);
                    caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
                    caClients.putIfAbsent(ca.getName(), caClient);

                    Collection<NetworkConfig.UserInfo> registrars = ca.getRegistrars();
                    for (NetworkConfig.UserInfo registrar : registrars) {
                        Enrollment enrollment = caClient.enroll(registrar.getName(), registrar.getEnrollSecret());
                        registrar.setEnrollment(enrollment);
                    }
                    logger.info("{}", registrars);
                }*/

                logger.info("caInfo: {}", caClient.info());

                FabricUser newUser = new FabricUser("tmp", networkConfig.getClientOrganization().getName());
                newUser.setMspId(networkConfig.getClientOrganization().getMspId());

                RegistrationRequest rr = new RegistrationRequest(newUser.getName(), "org1.department1");
                rr.setType(HFCAClient.HFCA_TYPE_USER);
                rr.addAttribute(new Attribute("hf.Registrar.Roles", "client,peer,user"));
                newUser.setEnrollmentSecret(caClient.register(rr, registrars.iterator().next()));

                if (!newUser.isEnerolled()) {
                    newUser.setEnrollment(caClient.enroll(newUser.getName(), newUser.getEnrollmentSecret()));
                }
                currentIdentity = newUser;
                logger.info("newUser: {}", newUser);
            }
            // assert currentIdentity.isEnerolled();
            client.setUserContext(currentIdentity);

            /*Channel channelFoo = client.loadChannelFromConfig("foo", networkConfig);
            logger.info("channelFoo.isInitialized()={}", channelFoo.isInitialized());
            if (!channelFoo.isInitialized()) {
                channelFoo.initialize();
            }*/

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
        logger.debug("in getNetwork");

        return networks.computeIfAbsent(channelName, key -> {
            logger.info("getNetwork: create network object and initialize");

            Network newNetwork = null;
            try {
                Channel channel = client.loadChannelFromConfig(channelName, networkConfig);
                newNetwork = new DefaultNetwork(this, channel);
                networks.put(channelName, newNetwork);
            } catch (TransactionException | InvalidArgumentException | NetworkConfigurationException e) {
                logger.error("", e);
            }

            return newNetwork;
        });
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
