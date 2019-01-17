package com.digcredit.blockchain.fabric.wallet.util;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Wallet util
 * <p>
 * Created by shniu on 2019/1/16 0016.
 *
 * @link https://github.com/bjlhx15/algorithm-sign/blob/master/algorithm-sign-impl/src/main/java/com/lhx/cert/CertificateCoder.java
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

    /**
     * Parse X.509 Cert from String
     */
    public static X509Certificate parseCertFromString(String pemCert) throws CertificateException {
        CertificateFactory certInstance = CertificateFactory.getInstance("X.509");
        return (X509Certificate) certInstance.generateCertificate(
                new ByteArrayInputStream(
                        pemCert.getBytes(Charset.forName("UTF-8"))
                )
        );
    }

    /**
     * Store private key to file
     *
     * @link http://anandsekar.github.io/exporting-the-private-key-from-a-jks-keystore/
     */
    public static void storePrivateKey(String storePath, PrivateKey privateKey) throws IOException {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String encoded = base64Encoder.encode(privateKey.getEncoded());
        FileWriter writer = new FileWriter(storePath);
        writer.write("-----BEGIN PRIVATE KEY-----\n");
        writer.write(encoded);
        writer.write("\n");
        writer.write("-----END PRIVATE KEY-----");
        writer.close();
    }

    /**
     * Store public key to file
     */
    public static void storePublicKey(String storePath, PublicKey publicKey) throws IOException {
        BASE64Encoder encoder = new BASE64Encoder();
        String encoded = encoder.encode(publicKey.getEncoded());
        FileWriter fw = new FileWriter(storePath);
        // BEGIN CERTIFICATE
        // BEGIN PUBLIC KEY
        fw.write("-----BEGIN PUBLIC KEY-----\n");
        fw.write(encoded);
        fw.write("\n");
        fw.write("-----BEGIN PUBLIC KEY-----");
        fw.close();
    }

    /*KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(null, "123".toCharArray());
    File keystore = new File("D:\\user1.keystore");
    FileOutputStream fos = new FileOutputStream(keystore);
    fos.write(privateKeyHex.getBytes());
    ks.store(fos, "123".toCharArray());
    fos.close();

    FileInputStream fis = new FileInputStream(keystore);
    ks.load(fis, "123".toCharArray());
    String alias = ks.aliases().nextElement();
    PrivateKey newKey = (PrivateKey) ks.getKey(alias, "123".toCharArray());
    logger.info("newKey hex={}", ByteUtils.toHexString(newKey.getEncoded()));
    fis.close();*/
}
