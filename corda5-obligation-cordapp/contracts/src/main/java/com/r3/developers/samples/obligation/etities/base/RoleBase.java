package com.r3.developers.samples.obligation.etities.base;

public class RoleBase extends SupplyChainBaseEntity {
    private String roleName;

    private String address;

    private String domain;

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain(){
        return this.address.split("/")[2];
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
