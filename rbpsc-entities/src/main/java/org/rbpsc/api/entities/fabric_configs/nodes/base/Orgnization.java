package org.rbpsc.api.entities.fabric_configs.nodes.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.rbpsc.api.entities.base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Orgnization extends BaseEntity<String> {
    private String orgName;
    private String mspOrgName;

    public void setOrgName(String orgName) {
        this.orgName = orgName;
        this.mspOrgName = Character.toUpperCase(orgName.charAt(0)) + orgName.substring(1);
    }
}
