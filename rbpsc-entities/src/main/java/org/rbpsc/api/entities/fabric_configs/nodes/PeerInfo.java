package org.rbpsc.api.entities.fabric_configs.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.rbpsc.api.entities.fabric_configs.nodes.base.FabricNodeInfoBase;
import org.rbpsc.api.entities.fabric_configs.nodes.base.Orgnization;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PeerInfo extends FabricNodeInfoBase {
}
