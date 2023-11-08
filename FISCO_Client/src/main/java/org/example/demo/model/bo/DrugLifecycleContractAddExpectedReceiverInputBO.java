package org.example.demo.model.bo;

import java.lang.Boolean;
import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugLifecycleContractAddExpectedReceiverInputBO {
  private String _id;

  private String _batchId;

  private String _roleName;

  private String _address;

  private BigInteger _expectedDose;

  private Boolean _satisfied;

  private String _domain;

  private String _drugLifeCycleId;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_id);
    args.add(_batchId);
    args.add(_roleName);
    args.add(_address);
    args.add(_expectedDose);
    args.add(_satisfied);
    args.add(_domain);
    args.add(_drugLifeCycleId);
    return args;
  }
}
