package com.hny.controller;

import com.hny.bean.User;
import com.hny.service.UserService;
import com.hny.utils.MD5Utils;
import com.hny.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserService userService;


    // 注册
    @PostMapping("/common/register")
    public R handleRegisterToLogin(@RequestBody User user){
        String account = userService.getPasswordByAccount(user.getAccount());
        if(account == null){
            // 加密
            user.setPassword(MD5Utils.getPWD(user.getPassword()));
            int flag = userService.addAccount(user);
           if(flag==1){
               System.out.println("注册成功！");
               return R.ok();
           }
            System.out.println("注册失败！");
            return R.error();
        }
        System.out.println("注册失败！");
        return R.error();

    }

    // 获取所有用户
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }


    // 根据ID删除用户
    @DeleteMapping("/users/{userIds}")
    public R derUsers(@PathVariable("userIds") List<String> userIds){
        System.out.println("----------delusers------------");
        System.out.println(userIds);
        for (String userId:userIds) {
            int flag = userService.delUserByUserId(Integer.parseInt(userId));
            if(flag==0){
                System.out.println(userId+":删除失败！");
                return R.error();
            }
            System.out.println(userId+":删除成功!");
        }
        return R.ok();
    }

    // 根据ID获取用户
    @GetMapping("/users/{userId}")
    public R getUserByUserId(@PathVariable("userId") int userId){

        User user = userService.getUserByUserId(userId);

        if(user!=null){
            System.out.println("获取成功！");
            R r = R.ok();
            r.getData().put("user",user);
            return r;
        }else {
            System.out.println("获取失败！");
            return R.error();
        }

    }

    // 添加用户
    @PostMapping("/users")
    public R addUser(@RequestBody User user){
        System.out.println("------------addUser---------------");
        System.out.println(user);
        String password = user.getPassword();
        user.setPassword(MD5Utils.getPWD(password));
        int flag = userService.addAccount(user);
        if(flag==0){
            return R.error();
        }else {
            return R.ok();
        }
    }

    // 更新用户
    @PutMapping("/users")
    public R updateUser(@RequestBody User user){
        Integer userId = user.getUserId();
        int flag1 = userService.delUserByUserId(userId);
        if(flag1==1){
            int flag2 = userService.addAccount(user);
            if(flag2==1){
                return R.ok();
            }
        }
        return R.error();



    }
}
