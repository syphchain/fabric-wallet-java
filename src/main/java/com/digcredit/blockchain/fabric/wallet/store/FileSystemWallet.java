package com.digcredit.blockchain.fabric.wallet.store;

import com.digcredit.blockchain.fabric.wallet.Wallet;
import org.hyperledger.fabric.sdk.User;

/**
 * File system wallet, a local wallet
 * <p>
 * Created by shniu on 2019/1/16 0016.
 */
public class FileSystemWallet implements Wallet {

    @Override
    public void imports(String label, User user) {

    }

    @Override
    public User exports(String label) {
        return null;
    }
}
