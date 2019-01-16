package com.digcredit.blockchain.fabric.wallet;

import io.netty.util.internal.StringUtil;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.io.Serializable;
import java.util.Set;

/**
 * User for Fabric block chain network
 */
public class FabricUser implements User, Serializable {

    private static final long serialVersionUID = 8077132155383604355L;

    private String name;
    // peer orderer client user
    private Set<String> roles;
    private String account;
    private String affiliation;
    private String organization;
    private String enrollmentSecret;

    String mspId;
    Enrollment enrollment = null;

    public FabricUser(String name, String organization) {
        this.name = name;
        this.organization = organization;
    }

    /**
     * Get label, unique user id in the wallet
     *
     * @return unique user id
     */
    public String getLabel() {
        return name + organization;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Set<String> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String getAffiliation() {
        return this.affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    @Override
    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public void setEnrollmentSecret(String enrollmentSecret) {
        this.enrollmentSecret = enrollmentSecret;
    }

    public String getEnrollmentSecret() {
        return enrollmentSecret;
    }

    public boolean isEnerolled() {
        return null != enrollment;
    }

    public boolean isRegistered() {
        return !StringUtil.isNullOrEmpty(enrollmentSecret);
    }

    @Override
    public String getMspId() {
        return this.mspId;
    }

    public void setMspId(String mspId) {
        this.mspId = mspId;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "FabricUser{" +
                "name='" + name + '\'' +
                ", roles=" + roles +
                ", account='" + account + '\'' +
                ", affiliation='" + affiliation + '\'' +
                ", organization='" + organization + '\'' +
                ", enrollmentSecret='" + enrollmentSecret + '\'' +
                ", mspId='" + mspId + '\'' +
                ", enrollment=" + enrollment +
                '}';
    }
}
