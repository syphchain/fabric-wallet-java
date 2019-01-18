package com.digcredit.blockchain.fabric.wallet;

import com.digcredit.blockchain.fabric.wallet.option.GatewayOptions;
import com.digcredit.blockchain.fabric.wallet.store.FileSystemWallet;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Gateway test
 * <p>
 * Created by shniu on 2019/1/17 0017.
 */
public class GatewayTest {
    private static Logger logger = LoggerFactory.getLogger(GatewayTest.class);

    @Test
    public void test() throws Exception {
        // initial
        String channelName = "mychannel";
        ChaincodeID chaincodeID = ChaincodeID.newBuilder()
                .setName("examples02")
                .setPath("examples02/")
                .setVersion("1.0")
                .build();

        // User identity
        String user = "User1@org1.digcredit.com";

        // Wallet
        String walletPath;
        if (System.getProperty("os.name").startsWith("win")) {
            walletPath = "D:\\tmp\\fabric-wallet";
        } else {
            walletPath = "/tmp/fabric-wallet";
        }
        Wallet wallet = new FileSystemWallet(walletPath);

        // NetworkConfig
        Path networkConfigPath = Paths.get("", "");
        NetworkConfig networkConfig = NetworkConfig.fromYamlFile(new File(networkConfigPath.toString()));

        // GatewayOptions
        GatewayOptions gatewayOptions = new GatewayOptions();
        gatewayOptions.setWallet(wallet);
        gatewayOptions.setIdentity(user);

        // gateway
        Gateway gateway = null; // Todo
        gateway.connect(networkConfig, gatewayOptions);

        Network network = gateway.getNetwork(channelName);
        Contract contract = network.getContract(chaincodeID, "");

        // ------ Invoke transaction
        String fcn = "transfer";
        String[] args = new String[]{"a", "b", "100"};
        logger.info("submit transaction, fcn={}, args={}", fcn, args);
        TransactionResponse txResponse = contract.submitTransaction(fcn, args);
        logger.info("transaction response: {}", txResponse);

        // ------ Query transaction
        // fcn = "get";

    }
}