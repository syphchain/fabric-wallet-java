package com.digcredit.blockchain.fabric.wallet.util;

import com.digcredit.blockchain.fabric.wallet.FabricUser;
import com.digcredit.blockchain.fabric.wallet.store.FileSystemWalletTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bean copier
 * Created by shniu on 2019/1/17 0017.
 */
public class SimpleBeanCopierTest {
    private static Logger logger = LoggerFactory.getLogger(FileSystemWalletTest.class);

    @Test
    public void test() {
        SimpleBeanCopier simpleBeanCopier = new SimpleBeanCopier<>(FabricUser.class, FabricUser.class);
        FabricUser u1 = new FabricUser("copier", "org101");
        FabricUser u2 = new FabricUser("copier2", "org102");
        //noinspection unchecked
        simpleBeanCopier.copyBean(u1, u2);
        logger.info("{}", u1);
        logger.info("{}", u2);
        Assert.assertEquals(u1.getName(), u2.getName());
    }
}