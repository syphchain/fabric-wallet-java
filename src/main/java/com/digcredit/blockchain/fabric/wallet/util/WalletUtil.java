package com.digcredit.blockchain.fabric.wallet.util;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;

/**
 * Wallet util
 * <p>
 * Created by shniu on 2019/1/16 0016.
 */
public class WalletUtil {

    /**
     * From private key string to generate PrivateKey
     *
     * @param privateKey private key string
     * @return PrivateKey instance
     * @throws IOException io
     */
    public static PrivateKey getPrivateKeyFromString(String privateKey)
            throws IOException {
        final StringReader reader = new StringReader(privateKey);

        final PrivateKeyInfo pemPair;
        try (PEMParser pemParser = new PEMParser(reader)) {
            pemPair = (PrivateKeyInfo) pemParser.readObject();
        }
        return new JcaPEMKeyConverter().getPrivateKey(pemPair);
    }
}
