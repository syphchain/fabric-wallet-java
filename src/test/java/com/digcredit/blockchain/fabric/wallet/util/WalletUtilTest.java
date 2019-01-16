package com.digcredit.blockchain.fabric.wallet.util;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;

/**
 * Test
 * <p>
 * Created by shniu on 2019/1/16 0016.
 */
public class WalletUtilTest {

    private static Logger logger = LoggerFactory.getLogger(WalletUtilTest.class);

    @Test
    public void getPrivateKeyFromString() throws Exception {
        String privateKeyStirng = "-----BEGIN PRIVATE KEY-----\n" +
                "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQg9q05wVgS9yaWAFk1\n" +
                "E9ANck6YYlgmIM//Blv+6pM/u1ahRANCAATYDYBs2zMkAaXEW7kow68JWS4msf/+\n" +
                "LD+RuSAGycbrBkJi4aU0CRIr0Fk9NX+rFMudmo0g3y6+Dp47fNiuEi8T\n" +
                "-----END PRIVATE KEY-----";
        PrivateKey privateKey = WalletUtil.getPrivateKeyFromString(privateKeyStirng);
        String pvBase64 = Base64.encodeBase64String(privateKey.getEncoded());
        logger.info("pv getEncodedToString={}", pvBase64);
        Assert.assertEquals("MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQg9q05wVgS9yaWAFk1E9ANck6YYlgmIM" +
                "//Blv+6pM/u1ahRANCAATYDYBs2zMkAaXEW7kow68JWS4msf/+LD+RuSAGycbrBkJi4aU0CRIr0Fk9NX+rFMudmo0g3y6+Dp47fNiuEi8T",
                pvBase64);
    }

}