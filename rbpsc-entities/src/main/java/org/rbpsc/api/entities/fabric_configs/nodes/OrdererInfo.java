package org.rbpsc.api.entities.fabric_configs.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.rbpsc.api.entities.fabric_configs.nodes.base.FabricNodeInfoBase;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrdererInfo extends FabricNodeInfoBase {
}
