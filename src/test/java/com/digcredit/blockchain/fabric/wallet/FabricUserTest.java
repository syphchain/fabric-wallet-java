package com.digcredit.blockchain.fabric.wallet;

import com.digcredit.blockchain.fabric.wallet.util.WalletUtil;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.util.HashSet;

/**
 * Test
 * <p>
 * Created by shniu on 2019/1/16 0016.
 */
public class FabricUserTest {
    private static Logger logger = LoggerFactory.getLogger(FabricUserTest.class);
    private CryptoPrimitives cryptoPrimitives;

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

    private PrivateKey userPrivateKey;

    @Before
    public void setUp() {
        try {
            cryptoPrimitives = new CryptoPrimitives();
            cryptoPrimitives.init();

            userPrivateKey = WalletUtil.getPrivateKeyFromString(privateKeyString);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void test() throws CryptoException {
        FabricUser user = new FabricUser("user1", "org1.digcredit.com");
        user.setMspId("Org1MSP");
        user.setAffiliation("org1.it");

        Assert.assertTrue("user1".equals(user.getName()));
        Assert.assertTrue("org1.digcredit.com".equals(user.getOrganization()));
        Assert.assertTrue("org1.it".equals(user.getAffiliation()));
        Assert.assertTrue("Org1MSP".equals(user.getMspId()));

        Assert.assertEquals(false, user.isRegistered());
        Assert.assertEquals(false, user.isEnerolled());

        // Set secret
        user.setEnrollmentSecret("123456");
        Assert.assertEquals(true, user.isRegistered());

        // Set Enrollment
        // KeyPair keyPair = cryptoPrimitives.keyGen();
        Enrollment enrollment = new X509Enrollment(userPrivateKey, signedPem);
        user.setEnrollment(enrollment);
        Assert.assertEquals(true, user.isEnerolled());
        Assert.assertEquals(signedPem, user.getEnrollment().getCert());

        // Roles: peer orderer client user
        user.setRoles(new HashSet<String>(){{
            add("client");
            add("user");
        }});
        logger.info("roles={}", user.getRoles());
        Assert.assertTrue(user.getRoles().size() == 2);

        // User u = user;
    }

}