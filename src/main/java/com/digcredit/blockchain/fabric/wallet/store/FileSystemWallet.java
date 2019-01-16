package com.digcredit.blockchain.fabric.wallet.store;

import com.digcredit.blockchain.fabric.wallet.Wallet;
import org.hyperledger.fabric.sdk.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File system wallet, a local wallet
 * <p>
 * Created by shniu on 2019/1/16 0016.
 */
public class FileSystemWallet implements Wallet {
    private static Logger logger = LoggerFactory.getLogger(FileSystemWallet.class);

    @Override
    public void imports(String label, User user) {
        logger.debug("in imports, label={}", label);

    }

    @Override
    public User exports(String label) {
        return null;
    }
}
