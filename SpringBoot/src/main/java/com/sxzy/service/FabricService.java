package com.sxzy.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sxzy.pojo.Steel;
import com.sxzy.pojo.SteelQueryResult;
import com.sxzy.pojo.SteelQueryResultList;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.StringUtils;
import org.hyperledger.fabric.gateway.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class FabricService {

    final Gateway gateway;

    final Contract contract;

    final Network network;

    //test
    public Steel querySteel() throws ContractException {
        byte[] bytes = contract.evaluateTransaction("querySteel","steel-1");
        String string = StringUtils.newStringUtf8(bytes);
        JSONObject jsonObject = JSONObject.parseObject(string);

        String steel_P_Quality = jsonObject.getString("steel_P_Quality");
        String steel_T_Shift = jsonObject.getString("steel_T_Shift");
        String steel_P_ManufactureTime = jsonObject.getString("steel_P_ManufactureTime");
        String steel_P_Id = jsonObject.getString("steel_P_Id");
        String steel_P_Name = jsonObject.getString("steel_P_Name");
        String steel_R_SalesTime = jsonObject.getString("steel_R_SalesTime");
        String steel_R_Price = jsonObject.getString("steel_R_Price");
        String steel_T_Pathway = jsonObject.getString("steel_T_Pathway");
        Steel steel = new Steel(steel_P_Id, steel_P_Name, steel_P_Quality, steel_P_ManufactureTime, steel_T_Pathway, steel_T_Shift, steel_R_Price, steel_R_SalesTime);

        return steel;
    }

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


}