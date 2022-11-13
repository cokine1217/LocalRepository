package com.sxzy.chaincode;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@Data
//@DataType([namespace]) 类级别注释，表示这个类可以代表一种可以被传入事务方法的复杂类型
@DataType()
@Accessors(chain = true)
public class Steel {

    //钢铁商品编号
    //@Property([schema]) 定义类属性的字段和参数级别注释。
    @Property()
    String steel_P_Id;

    //钢铁商品名称型号
    @Property()
    String steel_P_Name;

    //钢铁商品品质
    @Property()
    String steel_P_Quality;

    //钢铁商品生产日期
    @Property()
    String steel_P_ManufactureTime;

    //钢铁商品运输途径
    @Property()
    String steel_T_Pathway;

    //钢铁商品运输班次
    @Property()
    String steel_T_Shift;

    //钢铁商品销售价格
    @Property()
    String steel_R_Price;

    //钢铁商品销售时间
    @Property()
    String steel_R_SalesTime;

}
