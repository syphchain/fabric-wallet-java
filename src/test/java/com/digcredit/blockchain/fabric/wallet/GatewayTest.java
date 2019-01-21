package com.digcredit.blockchain.fabric.wallet;

import com.digcredit.blockchain.fabric.wallet.impl.DefaultGateway;
import com.digcredit.blockchain.fabric.wallet.option.GatewayOptions;
import com.digcredit.blockchain.fabric.wallet.store.FileSystemWallet;
import com.digcredit.blockchain.fabric.wallet.util.WalletUtil;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.HashSet;

/**
 * Gateway test
 * <p>
 * Created by shniu on 2019/1/17 0017.
 */
public class GatewayTest {
    private static Logger logger = LoggerFactory.getLogger(GatewayTest.class);

    private String privateKeyString = "-----BEGIN PRIVATE KEY-----\n" +
            "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQg9q05wVgS9yaWAFk1\n" +
            "E9ANck6YYlgmIM//Blv+6pM/u1ahRANCAATYDYBs2zMkAaXEW7kow68JWS4msf/+\n" +
            "LD+RuSAGycbrBkJi4aU0CRIr0Fk9NX+rFMudmo0g3y6+Dp47fNiuEi8T\n" +
            "-----END PRIVATE KEY-----";
    private String signedPem = "-----BEGIN CERTIFICATE-----\n" +
            "MIICGTCCAb+gAwIBAgIQasN+oEYEWpzdwd5/XOoVNzAKBggqhkjOPQQDAjBzMQsw\n" +
            "CQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNU2FuIEZy\n" +
            "YW5jaXNjbzEZMBcGA1UEChMQb3JnMS5leGFtcGxlLmNvbTEcMBoGA1UEAxMTY2Eu\n" +
            "b3JnMS5leGFtcGxlLmNvbTAeFw0xODAyMjUxMjQzMjlaFw0yODAyMjMxMjQzMjla\n" +
            "MFsxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRYwFAYDVQQHEw1T\n" +
            "YW4gRnJhbmNpc2NvMR8wHQYDVQQDDBZVc2VyMUBvcmcxLmV4YW1wbGUuY29tMFkw\n" +
            "EwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE2A2AbNszJAGlxFu5KMOvCVkuJrH//iw/\n" +
            "kbkgBsnG6wZCYuGlNAkSK9BZPTV/qxTLnZqNIN8uvg6eO3zYrhIvE6NNMEswDgYD\n" +
            "VR0PAQH/BAQDAgeAMAwGA1UdEwEB/wQCMAAwKwYDVR0jBCQwIoAgscw0w/LQz4B4\n" +
            "aPo6GhGHTSBBMIRf2O6zbS5ZRNd2dxwwCgYIKoZIzj0EAwIDSAAwRQIhAPYzdEPT\n" +
            "ek748A82YB4I6CC5K28k9IXjh0Ny4Z3UmiYEAiBLqwWDlJQMFoY7TOtrIHLMEpHz\n" +
            "8mclDuUg7Gtp1ktHgg==\n" +
            "-----END CERTIFICATE-----";
    private Enrollment enrollment;

    @Before
    public void setUp() throws Exception {
        PrivateKey userPrivateKey = WalletUtil.getPrivateKeyFromString(privateKeyString);
        enrollment = new X509Enrollment(userPrivateKey, signedPem);
    }

    @Test
    public void test() throws Exception {
        // Todo: 应用端配置项?
        //  channel name, chaincode, client
        // initial
        String channelName = "mychannel";
        ChaincodeID chaincodeID = ChaincodeID.newBuilder()
                .setName("examples02")
                .setPath("examples02/")
                .setVersion("1.0")
                .build();

        // User identity
        // String user = "User1@org1.digcredit.com";
        FabricUser user = new FabricUser("User1", "org1.digcredit.com");
        user.setRoles(new HashSet<String>() {{
            add("user");
        }});
        user.setEnrollment(enrollment);
        user.setMspId("Org1MSP");
        user.setAffiliation("it.org1");

        // Wallet
        String walletPath;
        if (System.getProperty("os.name").startsWith("win")) {
            walletPath = "D:\\tmp\\fabric-wallet";
        } else {
            walletPath = "/tmp/fabric-wallet";
        }
        Wallet wallet = new FileSystemWallet(walletPath);
        wallet.imports(user.getLabel(), user);

        // NetworkConfig
        Path networkConfigPath = Paths.get("src/test/fixture/network-config-tls.yaml");
        NetworkConfig networkConfig = NetworkConfig.fromYamlFile(new File(networkConfigPath.toString()));

        // GatewayOptions
        GatewayOptions gatewayOptions = new GatewayOptions();
        gatewayOptions.setWallet(wallet);
        gatewayOptions.setIdentity(user.getLabel());

        // gateway
        Gateway gateway = new DefaultGateway();
        gateway.connect(networkConfig, gatewayOptions);

//        Network network = gateway.getNetwork(channelName);
//        Contract contract = network.getContract(chaincodeID, "");
//
//        // ------ Invoke transaction
//        String fcn = "transfer";
//        String[] args = new String[]{"a", "b", "100"};
//        logger.info("submit transaction, fcn={}, args={}", fcn, args);
//        TransactionResponse txResponse = contract.submitTransaction(fcn, args);
//        logger.info("transaction response: {}", txResponse);

        // ------ Query transaction
        // fcn = "get";

    }
}