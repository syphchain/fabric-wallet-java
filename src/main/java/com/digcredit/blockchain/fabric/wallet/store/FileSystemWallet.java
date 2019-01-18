package com.digcredit.blockchain.fabric.wallet.store;

import com.digcredit.blockchain.fabric.wallet.FabricUser;
import com.digcredit.blockchain.fabric.wallet.Wallet;
import com.digcredit.blockchain.fabric.wallet.util.WalletUtil;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;

/**
 * File system wallet, a local wallet
 * <p>
 * Created by shniu on 2019/1/16 0016.
 */
public class FileSystemWallet implements Wallet {
    private static Logger logger = LoggerFactory.getLogger(FileSystemWallet.class);

    private static final String PRIVATE_KEY_SUFFIX = "-priv";
    private static final String PUBLIC_KEY_SUFFIX = "-pub";

    private String walletName; // Todo
    private String walletStorePath;

    public FileSystemWallet(String walletStorePath) {
        if (Strings.isNullOrEmpty(walletStorePath)) {
            throw new RuntimeException("basePath can not be empty or null");
        }
        this.walletStorePath = walletStorePath;
    }

    /**
     * /tmp/fabric-wallet
     * /user1/wallet/user1@org1.digcredit.com/
     * |- user1@org1.digcredit.com
     * |- user1-priv
     * |- user1-pub
     */
    @Override
    public void imports(String label, FabricUser user) throws Exception {
        logger.debug("in imports, label={}", label);

        checkLabel(label);
        Objects.requireNonNull(user.getEnrollment());

        // @link https://docs.oracle.com/javase/tutorial/essential/io/dirs.html
        Path storePath = genUserStorePath(walletStorePath, label);
        Files.createDirectories(storePath);
        logger.debug("user store path: {}", storePath.toString());

        store(label, user, storePath);
    }

    private void store(String label, FabricUser user, Path storePath) throws IOException, CertificateException {
        // Store private key
        PrivateKey privateKey = user.getEnrollment().getKey();
        // String privateKeyHex = ByteUtils.toHexString(privateKey.getEncoded());
        // logger.debug("private key hex={}", privateKeyHex);
        String privateName = getId(label) + PRIVATE_KEY_SUFFIX;
        String privateStorePath = Paths.get(storePath.toString(), privateName).toString();
        WalletUtil.storePrivateKey(privateStorePath, privateKey);
        logger.debug("store private key succeed: {}", privateStorePath);

        // Store public key
        X509Certificate x509Certificate = WalletUtil.parseCertFromString(user.getEnrollment().getCert());
        PublicKey publicKey = x509Certificate.getPublicKey();
        String publicName = getId(label) + PUBLIC_KEY_SUFFIX;
        String publicStorePath = Paths.get(storePath.toString(), publicName).toString();
        WalletUtil.storePublicKey(publicStorePath, publicKey);
        logger.debug("store public key succeed: {}", publicStorePath);

        // Store user
        String jsonData = user.toJson();
        Path userStorePath = Paths.get(storePath.toString(), label);
        Files.write(userStorePath, jsonData.getBytes());
        logger.debug("store user succeed: {}", userStorePath);
    }

    @Override
    public FabricUser exports(String label) throws Exception {
        // Get path
        Path storePath = genUserStorePath(walletStorePath, label);

        // Private key
        String privateName = getId(label) + PRIVATE_KEY_SUFFIX;
        Path privateStorePath = Paths.get(storePath.toString(), privateName);
        byte[] bytesPriv = Files.readAllBytes(privateStorePath);
        PrivateKey privateKey = WalletUtil.getPrivateKeyFromString(new String(bytesPriv));

        // User
        Path userStorePath = Paths.get(storePath.toString(), label);
        byte[] bytesUser = Files.readAllBytes(userStorePath);
        String jsonData = new String(bytesUser);

        FabricUser user = new FabricUser();
        user.fromJson(jsonData, privateKey);
        return user;
    }
}
