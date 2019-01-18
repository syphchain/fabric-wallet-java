package com.digcredit.blockchain.fabric.wallet;

/**
 * Response of a transaction, query or invoke
 * <p>
 * Created by shniu on 2019/1/18 0018.
 */
public class TransactionResponse {
    private String code;
    private String payload;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "code='" + code + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
