package com.sxzy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Steel {

    //钢铁商品编号
    String steel_P_Id;

    //钢铁商品名称型号
    String steel_P_Name;

    //钢铁商品品质
    String steel_P_Quality;

    //钢铁商品生产日期
    String steel_P_ManufactureTime;

    //钢铁商品运输途径
    String steel_T_Pathway;

    //钢铁商品运输班次
    String steel_T_Shift;

    //钢铁商品销售价格
    String steel_R_Price;

    //钢铁商品销售时间
    String steel_R_SalesTime;


}
