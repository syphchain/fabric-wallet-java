package com.digcredit.blockchain.fabric.wallet;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Wallet interface
 * <p>
 * Created by shniu on 2019/1/16 0016.
 */
public interface Wallet {

    default void checkLabel(String label) {
        Objects.requireNonNull(label);
        String[] labels = label.split("@");
        // Todo: opt ?
        assert labels.length >= 2;
    }

    default String getId(String label) {
        return label.substring(0, label.indexOf("@"));
    }

    default Path genUserStorePath(String walletBasePath, String label) {
        String id = getId(label);
        // baseStorePath + id + "wallet" + label
        /*return walletBasePath + File.separator + id + File.separator +
                "wallet" + File.separator + label;*/
        return Paths.get(walletBasePath, id, "wallet", label);
    }

    /**
     * Import the certificate, the private key and the user info into wallet
     *
     * @param label Identity in the wallet, e.g. default is `user1@org1.example.com`
     * @param user  user with certificate, private key and other information
     */
    void imports(String label, FabricUser user) throws Exception;

    /**
     * Export the certificate, the private key and the user info from wallet
     *
     * @param label Identity in the wallet, e.g. user1@org1.example.com
     * @return user with certificate, private key and other information
     */
    FabricUser exports(String label) throws Exception;
}
