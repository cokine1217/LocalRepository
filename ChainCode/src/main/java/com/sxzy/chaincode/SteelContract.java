package com.sxzy.chaincode;

import com.alibaba.fastjson2.JSON;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.List;


/*
除了合约注释之外，所有合约都应实现此接口。public interface ContractInterface
此接口上的所有方法都有默认实现;对于许多合同，可能不需要对这些合同进行子分类。

合约上用@Transaction标记的每个方法都被视为事务函数。
这符合通话条件。每个事务函数都提供，其第一个参数是Context，其他参数由开发人员自行决定。

调用的顺序是：
createContext()  -> beforeTransaction() -> the transaction function -> afterTransaction()
如果这些函数中的任何一个引发异常，则将其视为错误情况，并且整个事务都失败。

Context 是一个非常重要的对象，因为它为访问当前事务ID，分类帐状态等提供了事务Context。

ChaincodeStub 一个对象，它管理事务Context，提供对状态变量的访问，并支持对其他链代码实现的调用。*/

//@Contract 表示该类是一个智能合约
@Contract(name = "chaincode")
//@Default 表示默认合约的意思
@Default
public class SteelContract implements ContractInterface {

    //初始化ok
    @Transaction
    public void initLedger(final Context ctx) {
        //stub- 用于此事务的链代码存根的实例
        ChaincodeStub stub = ctx.getStub();

        Steel steel1 = new Steel().setSteel_P_Id("steel-" + 1).setSteel_P_Name("iron")
                .setSteel_P_Quality("A+").setSteel_P_ManufactureTime("2022/1/1")
                .setSteel_T_Pathway("Air").setSteel_T_Shift("A19")
                .setSteel_R_Price("16899").setSteel_R_SalesTime("2022/1/6");

        Steel steel2 = new Steel().setSteel_P_Id("steel-" + 2).setSteel_P_Name("iron")
                .setSteel_P_Quality("B+").setSteel_P_ManufactureTime("2022/1/2")
                .setSteel_T_Pathway("Air").setSteel_T_Shift("A20")
                .setSteel_R_Price("12799").setSteel_R_SalesTime("2022/1/7");

        Steel steel3 = new Steel().setSteel_P_Id("steel-" + 3).setSteel_P_Name("copper")
                .setSteel_P_Quality("A").setSteel_P_ManufactureTime("2022/10/15")
                .setSteel_T_Pathway("Train").setSteel_T_Shift("T02")
                .setSteel_R_Price("8999").setSteel_R_SalesTime("2022/11/4");

        Steel steel4= new Steel().setSteel_P_Id("steel-" + 4).setSteel_P_Name("copper")
                .setSteel_P_Quality("B").setSteel_P_ManufactureTime("2022/10/15")
                .setSteel_T_Pathway("Train").setSteel_T_Shift("T05")
                .setSteel_R_Price("6799").setSteel_R_SalesTime("2022/11/5");

        Steel steel5 = new Steel().setSteel_P_Id("steel-" + 5).setSteel_P_Name("aluminum")
                .setSteel_P_Quality("C").setSteel_P_ManufactureTime("2022/12/23")
                .setSteel_T_Pathway("Water").setSteel_T_Shift("W89")
                .setSteel_R_Price("4379").setSteel_R_SalesTime("2023/1/16");

        Steel steel6 = new Steel().setSteel_P_Id("steel-" + 6).setSteel_P_Name("aluminum")
                .setSteel_P_Quality("C-").setSteel_P_ManufactureTime("2022/12/24")
                .setSteel_T_Pathway("Water").setSteel_T_Shift("W90")
                .setSteel_R_Price("3689").setSteel_R_SalesTime("2023/1/18");

        stub.putStringState(steel1.getSteel_P_Id() , JSON.toJSONString(steel1));
        stub.putStringState(steel2.getSteel_P_Id() , JSON.toJSONString(steel2));
        stub.putStringState(steel3.getSteel_P_Id() , JSON.toJSONString(steel3));
        stub.putStringState(steel4.getSteel_P_Id() , JSON.toJSONString(steel4));
        stub.putStringState(steel5.getSteel_P_Id() , JSON.toJSONString(steel5));
        stub.putStringState(steel6.getSteel_P_Id() , JSON.toJSONString(steel6));
    }

    //增加一个ok
    @Transaction
    public Steel createSteel(final Context ctx, final String steel_P_Id,
                             String steel_P_Name, String steel_P_Quality,
                             String steel_P_ManufactureTime, String steel_T_Pathway,
                             String steel_T_Shift, String steel_R_Price,
                             String steel_R_SalesTime) {

        ChaincodeStub stub = ctx.getStub();
        String steelState = stub.getStringState(steel_P_Id);

        if (StringUtils.isNotBlank(steelState)) {
            String errorMessage = String.format("Steel %s already exists", steel_P_Id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        Steel steel = new Steel().setSteel_P_Id(steel_P_Id).setSteel_P_Name(steel_P_Name)
                .setSteel_P_Quality(steel_P_Quality).setSteel_P_ManufactureTime(steel_P_ManufactureTime)
                .setSteel_T_Pathway(steel_T_Pathway).setSteel_T_Shift(steel_T_Shift)
                .setSteel_R_Price(steel_R_Price).setSteel_R_SalesTime(steel_R_SalesTime);

        String json = JSON.toJSONString(steel);
        stub.putStringState(steel_P_Id, json);

        stub.setEvent("createSteelEvent", org.apache.commons.codec.binary.StringUtils.getBytesUtf8(json));
        return steel;

    }

    //删除一个
    @Transaction
    public Steel deleteSteel(final Context ctx, final String steel_P_Id) {

        ChaincodeStub stub = ctx.getStub();
        String steelState = stub.getStringState(steel_P_Id);

        if (StringUtils.isBlank(steelState)) {
            String errorMessage = String.format("Steel %s does not exist", steel_P_Id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        stub.delState(steel_P_Id);

        return JSON.parseObject(steelState, Steel.class);

    }

    //修改一个
    @Transaction
    public Steel updateSteel(final Context ctx, final String steel_P_Id,
                             String steel_P_Name, String steel_P_Quality,
                             String steel_P_ManufactureTime, String steel_T_Pathway,
                             String steel_T_Shift, String steel_R_Price,
                             String steel_R_SalesTime) {

        ChaincodeStub stub = ctx.getStub();
        String steelState = stub.getStringState(steel_P_Id);

        if (StringUtils.isBlank(steelState)) {
            String errorMessage = String.format("Steel %s does not exist", steel_P_Id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        Steel steel = new Steel().setSteel_P_Id(steel_P_Id).setSteel_P_Name(steel_P_Name)
                .setSteel_P_Quality(steel_P_Quality).setSteel_P_ManufactureTime(steel_P_ManufactureTime)
                .setSteel_T_Pathway(steel_T_Pathway).setSteel_T_Shift(steel_T_Shift)
                .setSteel_R_Price(steel_R_Price).setSteel_R_SalesTime(steel_R_SalesTime);

        stub.putStringState(steel_P_Id, JSON.toJSONString(steel));

        return steel;
    }

    //transporter添加ok
    @Transaction
    public void transporterAdd(final Context ctx, final String steel_P_Id,
                                String steel_T_Pathway, String steel_T_Shift) {

        ChaincodeStub stub = ctx.getStub();
        String steelState = stub.getStringState(steel_P_Id);

        if (StringUtils.isBlank(steelState)) {
            String errorMessage = String.format("Steel %s does not exist", steel_P_Id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        Steel steel = JSON.parseObject(steelState,Steel.class);


        steel.setSteel_P_Id(steel_P_Id).setSteel_T_Pathway(steel_T_Pathway).setSteel_T_Shift(steel_T_Shift);

        stub.putStringState(steel_P_Id, JSON.toJSONString(steel));
    }

    //retailer添加ok
    @Transaction
    public void retailerAdd(final Context ctx, final String steel_P_Id,
                            String steel_R_Price, String steel_R_SalesTime) {

        ChaincodeStub stub = ctx.getStub();
        String steelState = stub.getStringState(steel_P_Id);

        if (StringUtils.isBlank(steelState)) {
            String errorMessage = String.format("Steel %s does not exist", steel_P_Id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        Steel steel = JSON.parseObject(steelState,Steel.class);


        steel.setSteel_R_Price(steel_R_Price).setSteel_R_SalesTime(steel_R_SalesTime);

        stub.putStringState(steel_P_Id, JSON.toJSONString(steel));
    }

    //根据Id查询一个
    @Transaction
    public Steel querySteelById(final Context ctx, final String steel_P_Id) {
        ChaincodeStub stub = ctx.getStub();
        String steelState = stub.getStringState(steel_P_Id);

        if (StringUtils.isBlank(steelState)) {
            String errorMessage = String.format("Steel %s does not exist", steel_P_Id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        return JSON.parseObject(steelState, Steel.class);
    }

    //查询所有ok
    @Transaction
    public SteelQueryResultList queryAll(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        SteelQueryResultList resultList = new SteelQueryResultList();
        QueryResultsIterator<KeyValue> queryResult = stub.getStateByRange("", "");
        List<SteelQueryResult> results = new ArrayList<>();
        if (!IterableUtils.isEmpty(queryResult)) {
            for (KeyValue kv : queryResult) {
                results.add(new SteelQueryResult().setSteel_P_Id(kv.getKey()).setSteel(com.alibaba.fastjson2.JSON.parseObject(kv.getStringValue(), Steel.class)));
            }
            resultList.setSteels(results);
        }
        return resultList;
    }

}
