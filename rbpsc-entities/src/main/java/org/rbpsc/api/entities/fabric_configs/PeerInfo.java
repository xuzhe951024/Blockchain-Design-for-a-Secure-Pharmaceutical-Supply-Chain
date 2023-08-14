package org.rbpsc.api.entities.fabric_configs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.rbpsc.api.entities.base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PeerInfo extends BaseEntity<String> {
    private String peerName;
    private String orgName;
}
