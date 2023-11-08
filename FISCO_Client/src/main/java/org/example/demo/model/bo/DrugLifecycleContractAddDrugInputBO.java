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
public class DrugLifecycleContractAddDrugInputBO {
  private String _id;

  private String _batchId;

  private String _drugTagTagId;

  private String _drugName;

  private Boolean _fake;

  private Boolean _recalled;

  private String _drugLifeCycleId;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_id);
    args.add(_batchId);
    args.add(_drugTagTagId);
    args.add(_drugName);
    args.add(_fake);
    args.add(_recalled);
    args.add(_drugLifeCycleId);
    return args;
  }
}
