package com.digcredit.blockchain.fabric.wallet;

import org.hyperledger.fabric.sdk.User;

/**
 * Wallet interface
 * <p>
 * Created by shniu on 2019/1/16 0016.
 */
public interface Wallet {

    /**
     * Import the certificate, the private key and the user info into wallet
     *
     * @param label Identity in the wallet, e.g. user1@org1.example.com
     * @param user  user with certificate, private key and other information
     */
    void imports(String label, User user);

    /**
     * Export the certificate, the private key and the user info from wallet
     *
     * @param label Identity in the wallet, e.g. user1@org1.example.com
     * @return user with certificate, private key and other information
     */
    User exports(String label);
}
