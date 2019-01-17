package com.digcredit.blockchain.fabric.wallet;

import com.digcredit.blockchain.fabric.wallet.util.ModelMapper;
import com.digcredit.blockchain.fabric.wallet.util.SimpleBeanCopier;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.netty.util.internal.StringUtil;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;

import java.io.Serializable;
import java.security.PrivateKey;
import java.util.Set;

/**
 * User for Fabric block chain network
 */
public class FabricUser implements User, Serializable {

    private static final long serialVersionUID = 8077132155383604355L;
    @JsonIgnore
    private ModelMapper modelMapper = new ModelMapper();

    private String name;
    // peer orderer client user
    private Set<String> roles;
    private String account;
    private String affiliation;
    private String organization;
    private String enrollmentSecret;
    private String signedCert;

    String mspId;

    @JsonIgnore
    Enrollment enrollment = null;

    public FabricUser(){}

    public FabricUser(String name, String organization) {
        this.name = name;
        this.organization = organization;
    }

    /**
     * To json
     */
    public String toJson() {
        return modelMapper.writeString(this);
    }

    /**
     * From json
     */
    public void fromJson(String jsonStr, PrivateKey privateKey) {
        FabricUser user = (FabricUser) modelMapper.make(FabricUser.class, jsonStr);
        SimpleBeanCopier beanCopier = new SimpleBeanCopier<>(this.getClass(), this.getClass());
        //noinspection unchecked
        beanCopier.copyBean(user, this);
        if (privateKey != null) {
            this.enrollment = new X509Enrollment(privateKey, this.getSignedCert());
        }
    }

    /**
     * Get label, unique user id in the wallet
     *
     * @return unique user id
     */
    @JsonIgnore
    public String getLabel() {
        return String.format("%s@%s", name, organization);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
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
        if (enrollment != null) {
            this.signedCert = enrollment.getCert();
        }
        this.enrollment = enrollment;
    }

    public String getSignedCert() {
        return signedCert;
    }

    public void setSignedCert(String signedCert) {
        this.signedCert = signedCert;
    }

    public void setEnrollmentSecret(String enrollmentSecret) {
        this.enrollmentSecret = enrollmentSecret;
    }

    public String getEnrollmentSecret() {
        return enrollmentSecret;
    }

    @JsonIgnore
    public boolean isEnerolled() {
        return null != enrollment;
    }

    @JsonIgnore
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
