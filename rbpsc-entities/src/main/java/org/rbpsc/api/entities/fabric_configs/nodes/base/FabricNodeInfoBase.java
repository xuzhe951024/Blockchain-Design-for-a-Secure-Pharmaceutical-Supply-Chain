package org.rbpsc.api.entities.fabric_configs.nodes.base;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.rbpsc.api.entities.base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FabricNodeInfoBase extends BaseEntity<String> {
    private String orgName;
    private String mspOrgName;
    private String nodeName;
    private int portBaseOnHost;

    public void setOrgInfo(Orgnization orgInfo){
        this.setOrgName(orgInfo.getOrgName());
        this.setMspOrgName(orgInfo.getMspOrgName());
    }
}
