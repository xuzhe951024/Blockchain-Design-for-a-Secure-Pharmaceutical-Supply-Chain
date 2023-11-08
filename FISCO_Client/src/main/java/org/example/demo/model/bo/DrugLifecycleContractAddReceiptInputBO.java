package org.example.demo.model.bo;

import java.lang.Boolean;
import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugLifecycleContractAddReceiptInputBO {
  private String _drugLifeCycleId;

  private String _id;

  private String _batchId;

  private String _roleName;

  private String _address;

  private String _operationMSG;

  private Boolean _processed;

  private String _domain;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_drugLifeCycleId);
    args.add(_id);
    args.add(_batchId);
    args.add(_roleName);
    args.add(_address);
    args.add(_operationMSG);
    args.add(_processed);
    args.add(_domain);
    return args;
  }
}
