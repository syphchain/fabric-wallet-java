package com.digcredit.blockchain.fabric.wallet.util;

/**
 * @author shniu
 * @date 2018-06-25 下午1:02
 * @email niushaohan@digcredit.com
 */

public class SimpleBeanCopier<R, T> extends BeanCopier<R, T> {

    public SimpleBeanCopier(Class<R> sourceClass, Class<T> targetClass) {
        setSourceClass(sourceClass);
        setTargetClass(targetClass);
        init();
    }

}
