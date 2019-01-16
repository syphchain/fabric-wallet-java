package com.digcredit.blockchain.fabric.wallet.store;

import com.digcredit.blockchain.fabric.wallet.FabricUser;
import com.digcredit.blockchain.fabric.wallet.Wallet;
import com.digcredit.blockchain.fabric.wallet.util.WalletUtil;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.util.HashSet;

/**
 * Unit test for FileSystemWallet
 * <p>
 * Created by shniu on 2019/1/16 0016.
 */
public class FileSystemWalletTest {
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

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void imports() throws Exception {
        FabricUser user = new FabricUser("user1", "org1.digcredit.com");
        user.setRoles(new HashSet<String>(){{
            add("user");
        }});
        user.setEnrollment(enrollment);
        user.setMspId("Org1MSP");
        user.setAffiliation("it.org1");

        Wallet fsWallet = new FileSystemWallet();
        fsWallet.imports(user.getLabel(), user);
    }

    @Test
    public void exports() throws Exception {

    }

}