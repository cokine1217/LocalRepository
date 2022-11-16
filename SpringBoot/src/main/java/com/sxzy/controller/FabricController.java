package com.sxzy.controller;

import com.sxzy.pojo.Steel;
import com.sxzy.service.FabricService;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeoutException;

@Controller
public class FabricController {

    @Autowired
    private FabricService fabricService;

    //====================查询====================

    //模糊查询
    @PostMapping("/search")
    public String search(@RequestParam("steel") String string,
                         Model model) throws ContractException {
        List<Steel>list = fabricService.fuzzyQuery(string);
        model.addAttribute("list",list);
        return  "dashboard";
    }

    //====================添加====================

    //生产商添加
    @PostMapping("/manufacturerAdd")
    public String manufacturerAdd(@RequestParam("steel_P_Id") String steel_P_Id,
                                  @RequestParam("steel_P_Name") String steel_P_Name,
                                  @RequestParam("steel_P_Quality") String steel_P_Quality,
                                  @RequestParam("steel_P_ManufactureTime") String steel_P_ManufactureTime,
                                  Model model) throws ContractException, InterruptedException, TimeoutException {

        boolean flag = fabricService.manufacturerAdd(steel_P_Id,steel_P_Name,steel_P_Quality,steel_P_ManufactureTime);
        if (flag) {
            model.addAttribute("msgSuccess","Add Success");
        }else {
            model.addAttribute("msgFail","Add Failed");
        }
         List<Steel> list = fabricService.queryAll();
        model.addAttribute("list",list);
        return "dashboard";
    }
    //销售商添加
    @PostMapping("/retailerAdd")
    public String retailerAdd(@RequestParam("steel_P_Id") String steel_P_Id,
                                @RequestParam("steel_R_Price") String steel_R_Price,
                                @RequestParam("steel_R_SalesTime") String steel_R_SalesTime,
                            Model model) throws ContractException, InterruptedException, TimeoutException {

        boolean flag = fabricService.retailerAdd(steel_P_Id,steel_R_Price,steel_R_SalesTime);
        if (flag) {
            model.addAttribute("msgSuccess","Add Success");
        }else {
            model.addAttribute("msgFail","Add Failed");
        }
        List<Steel> list = fabricService.queryAll();
        model.addAttribute("list",list);
        return "dashboard";
    }
    //运输商添加
    @PostMapping("/transporterAdd")
    public String transporterAdd(@RequestParam("steel_P_Id") String steel_P_Id,
                                @RequestParam("steel_T_Pathway") String steel_T_Pathway,
                                @RequestParam("steel_T_Shift") String steel_T_Shift,
                               Model model) throws ContractException, InterruptedException, TimeoutException {
        boolean flag = fabricService.transporterAdd(steel_P_Id,steel_T_Pathway,steel_T_Shift);
        if (flag) {
            model.addAttribute("msgSuccess","Add Success");
        }else {
            model.addAttribute("msgFail","Add Failed");
        }
        List<Steel> list = fabricService.queryAll();
        model.addAttribute("list",list);
        return "dashboard";

    }

    //====================删除====================


    //manufacturer删除
    @GetMapping("/manufacturerDelete/{steel_P_Id}")
    public String manufacturerDelete(@PathVariable("steel_P_Id") String steel_P_Id,
                                     Model model) throws ContractException, InterruptedException, TimeoutException {
        boolean flag = fabricService.manufacturerDelete(steel_P_Id);
        if (flag==true) {
            List<Steel>list = fabricService.queryAll();
            model.addAttribute("list",list);
            model.addAttribute("msgSuccess","Delete Success");
        }else {
            model.addAttribute("msgFail","Delete failed");
        }
        return  "dashboard";
    }

    //transporter删除
    @GetMapping("transporterDelete/{steel_P_Id}")
    public String transporterDelete(@PathVariable("steel_P_Id") String steel_P_Id,
                                    Model model) throws ContractException, InterruptedException, TimeoutException {
        boolean flag = fabricService.transporterDelete(steel_P_Id);
        if (flag==true) {
            List<Steel>list = fabricService.queryAll();
            model.addAttribute("list",list);
            model.addAttribute("msgSuccess","Delete Success");
        }
         return  "dashboard";
    }


    //retailer删除
    @GetMapping("retailerDelete/{steel_P_Id}")
    public String retailerDelete(@PathVariable("steel_P_Id") String steel_P_Id,
                                 Model model) throws ContractException, InterruptedException, TimeoutException {
        boolean flag = fabricService.retailerDelete(steel_P_Id);
        if (flag==true) {
            List<Steel>list = fabricService.queryAll();
            model.addAttribute("list",list);
            model.addAttribute("msgSuccess","Delete Success");
        }
        return  "dashboard";
    }

}
