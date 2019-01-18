package com.digcredit.blockchain.fabric.wallet.option;

import com.digcredit.blockchain.fabric.wallet.Wallet;

/**
 * Gateway option
 * <p>
 * Created by shniu on 2019/1/18 0018.
 */
public class GatewayOptions {

    private DiscoveryOptions discovery;
    private Wallet wallet;
    private String identity;
    private EventHandlerOptions eventHandlerOptions;

    public DiscoveryOptions getDiscovery() {
        return discovery;
    }

    public void setDiscovery(DiscoveryOptions discovery) {
        this.discovery = discovery;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public EventHandlerOptions getEventHandlerOptions() {
        return eventHandlerOptions;
    }

    public void setEventHandlerOptions(EventHandlerOptions eventHandlerOptions) {
        this.eventHandlerOptions = eventHandlerOptions;
    }

    public static class DiscoveryOptions {
        private boolean enabled;
        private boolean asLocalhost;
    }

    public static class EventHandlerOptions {
        private int commitTimeout;
        private String strategy;  // Todo
    }
}
