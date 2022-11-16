package com.sxzy.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.base.Strings;
import com.sxzy.pojo.Steel;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.StringUtils;
import org.hyperledger.fabric.gateway.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;


@Service
@AllArgsConstructor
public class FabricService {

    final Gateway gateway;

    final Contract contract;

    final Network network;

    //====================查询====================

    public Steel querySteel(String steel_P_Id) throws ContractException {
        byte[] bytes = contract.evaluateTransaction("querySteelById",steel_P_Id);
        String string = StringUtils.newStringUtf8(bytes);
        JSONObject jsonObject = JSONObject.parseObject(string);

        String steel_P_Quality = jsonObject.getString("steel_P_Quality");
        String steel_T_Shift = jsonObject.getString("steel_T_Shift");
        String steel_P_ManufactureTime = jsonObject.getString("steel_P_ManufactureTime");
        String steel_P_Name = jsonObject.getString("steel_P_Name");
        String steel_R_SalesTime = jsonObject.getString("steel_R_SalesTime");
        String steel_R_Price = jsonObject.getString("steel_R_Price");
        String steel_T_Pathway = jsonObject.getString("steel_T_Pathway");
        Steel steel = new Steel(steel_P_Id, steel_P_Name, steel_P_Quality, steel_P_ManufactureTime, steel_T_Pathway, steel_T_Shift, steel_R_Price, steel_R_SalesTime);

        return steel;
    }

    //模糊查询
    public List<Steel> fuzzyQuery(String string) throws ContractException {
        List<Steel>list = this.queryAll();
        List<Steel>result = new ArrayList<Steel>();
        for (Steel steel:list) {
            if (steel.getSteel_P_Id().equals(string)
                    || steel.getSteel_P_Name().equals(string)
                    || steel.getSteel_P_Quality().equals(string)
                    || steel.getSteel_P_ManufactureTime().equals(string)
                    || steel.getSteel_T_Pathway().equals(string)
                    || steel.getSteel_T_Shift().equals(string)
                    || steel.getSteel_R_Price().equals(string)
                    || steel.getSteel_R_SalesTime().equals(string)) {
                result.add(steel);
            }
        }
        return result;
    }

    //查询所有ok
    public List<Steel> queryAll() throws ContractException {
        byte[] bytes = contract.evaluateTransaction("queryAll");
        String string = StringUtils.newStringUtf8(bytes);
        JSONObject object = JSONObject.parseObject(string);
        JSONArray jsonArray = object.getJSONArray("steels");

        List<Steel>list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONObject kv = jsonObject.getJSONObject("steel");

            String steel_P_Quality = kv.getString("steel_P_Quality");
            String steel_T_Shift = kv.getString("steel_T_Shift");
            String steel_P_ManufactureTime = kv.getString("steel_P_ManufactureTime");
            String steel_P_Id = kv.getString("steel_P_Id");
            String steel_P_Name = kv.getString("steel_P_Name");
            String steel_R_SalesTime = kv.getString("steel_R_SalesTime");
            String steel_R_Price = kv.getString("steel_R_Price");
            String steel_T_Pathway = kv.getString("steel_T_Pathway");
            Steel steel = new Steel(steel_P_Id, steel_P_Name, steel_P_Quality, steel_P_ManufactureTime, steel_T_Pathway, steel_T_Shift, steel_R_Price, steel_R_SalesTime);

            list.add(steel);
        }
        return list;
    }

    //====================增加====================

    //manufacturer初始化一个商品ok
    public boolean manufacturerAdd(String steel_P_Id, String steel_P_Name,
                                String steel_P_Quality,
                                String steel_P_ManufactureTime) throws ContractException, InterruptedException, TimeoutException {
        contract.submitTransaction("createSteel", steel_P_Id,steel_P_Name,steel_P_Quality,steel_P_ManufactureTime, "","","","");
        return true;
    }


    //transporter添加信息ok
    public boolean transporterAdd(String steel_P_Id,
                               String steel_T_Pathway,
                               String steel_T_Shift) throws ContractException, InterruptedException, TimeoutException {
        contract.submitTransaction("transporterAdd",steel_P_Id,steel_T_Pathway,steel_T_Shift);
        return true;
    }

    //retailer添加信息ok
    public boolean retailerAdd(String steel_P_Id,
                            String steel_R_Price,
                            String steel_R_SalesTime) throws ContractException, InterruptedException, TimeoutException {
        contract.submitTransaction("retailerAdd",steel_P_Id,steel_R_Price,steel_R_SalesTime);
        return true;
    }

    //====================删除====================

    //manufacturer删除
    public boolean manufacturerDelete(String steel_P_Id) throws ContractException, InterruptedException, TimeoutException {
        Steel steel = querySteel(steel_P_Id);
        if (steel.getSteel_T_Shift().equals("")
        && steel.getSteel_T_Pathway().equals("")
        && steel.getSteel_R_Price().equals("")
        && steel.getSteel_R_SalesTime().equals("")) {
            contract.submitTransaction("deleteSteel",steel_P_Id);
            return true;
        }else {
            return false;
        }
    }

    //transporter删除
    public  boolean transporterDelete(String steel_P_Id) throws ContractException, InterruptedException, TimeoutException {
       Steel steel = querySteel(steel_P_Id);
       steel.setSteel_T_Shift("").setSteel_T_Pathway("");
        contract.submitTransaction("updateSteel",
                steel_P_Id, steel.getSteel_P_Name(),
                steel.getSteel_P_Quality(), steel.getSteel_P_ManufactureTime(),
                steel.getSteel_T_Pathway(), steel.getSteel_T_Shift(),
                steel.getSteel_R_Price(), steel.getSteel_R_SalesTime());
        return true;
    }

    //retailer删除
    public  boolean retailerDelete(String steel_P_Id) throws ContractException, InterruptedException, TimeoutException {
        Steel steel = querySteel(steel_P_Id);
        steel.setSteel_R_SalesTime("").setSteel_R_Price("");
        contract.submitTransaction("updateSteel",
                steel_P_Id, steel.getSteel_P_Name(),
                steel.getSteel_P_Quality(), steel.getSteel_P_ManufactureTime(),
                steel.getSteel_T_Pathway(), steel.getSteel_T_Shift(),
                steel.getSteel_R_Price(), steel.getSteel_R_SalesTime());
        return true;
    }




}