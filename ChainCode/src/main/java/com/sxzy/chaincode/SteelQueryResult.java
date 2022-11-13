package com.sxzy.chaincode;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@Data
@DataType
@Accessors(chain = true)
public class SteelQueryResult {

    @Property
    String steel_P_Id;

    @Property
    Steel steel;

}
