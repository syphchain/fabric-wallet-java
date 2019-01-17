package com.digcredit.blockchain.fabric.wallet.util;


import com.google.common.base.Function;

/**
 * @author shniu
 * @date 2018-06-13 下午9:33
 * @email niushaohan@digcredit.com
 *
 * Bean复制的方式有多种，但是除去set/get之外, cglib实现的BeanCopier效率最高，
 * 是因为使用了动态代理，比使用反射的方式性能好
 */

public class BeanCopier<T, R> implements Function<T, R> {

    // cglib BeanCopier
    private net.sf.cglib.beans.BeanCopier beanCopier;

    // from
    private Class<T> sourceClass;
    // to
    private Class<R> targetClass;

    // 反转
    private BeanCopier<R, T> reverse;

    protected net.sf.cglib.beans.BeanCopier getBeanCopier() {
        return beanCopier;
    }

    protected void init() {
        this.beanCopier = net.sf.cglib.beans.BeanCopier.create(sourceClass, targetClass, false);
    }

    public BeanCopier<R, T> reverse() {
        return reverse;
    }

    public Function<R, T> getReverse() {
        if (this.reverse != null) {
            return this.reverse;
        }

        BeanCopier<R, T> reverse = null;
        synchronized (this) {
            if (reverse == null) {
                reverse = new BeanCopier<>();
                reverse.setSourceClass(this.getTargetClass());
                reverse.setTargetClass(this.getSourceClass());
                reverse.init();
            }
        }
        return reverse;
    }

    public void setReverse(BeanCopier<R, T> reverse) {
        this.reverse = reverse;
    }

    public Class<R> getTargetClass() {
        return targetClass;
    }

    public Class<T> getSourceClass() {
        return sourceClass;
    }

    public void setTargetClass(Class<R> targetClass) {
        this.targetClass = targetClass;
    }

    public void setSourceClass(Class<T> sourceClass) {
        this.sourceClass = sourceClass;
    }

    public R afterCopy(T source, R target){
        return target;
    }

    public R copy(T input) {
        try {
            R o = targetClass.newInstance();
            beanCopier.copy(input, o, null);
            return afterCopy(input, o);
        } catch (Exception e) {
            throw new RuntimeException("create object fail, class:" + targetClass.getName() + " ", e);
        }
    }

    public void copyBean(T sourceBean, R targetBean) {
        beanCopier.copy(sourceBean, targetBean, null);
        afterCopy(sourceBean, targetBean);
    }

    @Override
    public R apply(T t) {
        return copy(t);
    }
}
