package com.sxzy.controller;

import com.sxzy.pojo.Steel;
import com.sxzy.pojo.SteelQueryResult;
import com.sxzy.pojo.SteelQueryResultList;
import com.sxzy.pojo.User;
import com.sxzy.service.FabricService;
import com.sxzy.service.UserService;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController {

    private User user;
    @Autowired
    private UserService userService;
    @Autowired
    private FabricService fabricService;

    //登陆页面
    @RequestMapping(value = {"/"})
    public String loginPage() {
        return "login";
    }

    //退出功能
    @RequestMapping("/signout")
    public String signOut() {
        return "login";
    }

    //验证用户页面
    @PostMapping("/verifyLogin")
    public String verifyUser(@RequestParam("Username") String username, @RequestParam("Password") String password, Model model) throws ContractException {

        user = userService.selectOneByUsername(username);

        //用户不存在
        if(user == null){
            model.addAttribute("msg", "用户名或密码错误！");
            return "login";
        }

        //用户存在且密码相同 登陆成功
        if (user.getPassword().equals(password)) {
            List<Steel> list = fabricService.queryAll();
            model.addAttribute("list",list);
            return "dashboard";
        } else {
            model.addAttribute("msg", "用户名或密码错误！");
            return "login";
        }

    }

    //注册用户页面
    @PostMapping("/register")
    public String registerUser(@RequestParam("Username") String username, @RequestParam("Password") String password, Model model, @RequestParam("Company") String company)  {

        System.out.println(company);

        //用户已经存在
        if (userService.ifUserAlreadyExists(username)==true){
            model.addAttribute("msg", "注册失败，已存在该用户!");
        } else {
            userService.insertOneUser(username,password,company);
            model.addAttribute("msg", "注册成功，请登录！");
        }

        return "login";
    }

}