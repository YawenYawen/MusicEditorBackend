package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dtos.JsonResult;
import com.example.demo.repository.UserRepository;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResult login(final HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password) {
        JsonResult result = new JsonResult();
        try {
            boolean b = userService.login(username, password);
            if (!b) {
                result.setMessage("用户名或密码错误");
                return result;
            }
            result.setMessage("登录成功");
            result.setOk(true);
            session.setAttribute("username", username);//TODO：只有登录之后才能进入app
        } catch (Exception e) {
            log.error("", e);
            result.setMessage("登录失败");
        }
        return result;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonResult register(@RequestParam("username") String username, @RequestParam("nickname") String nickname, @RequestParam("password") String password) {
        JsonResult result = new JsonResult();
        try {
            User exist = userRepository.findByUsername(username);
            if (exist != null) {
                result.setMessage("用户名已存在");
                return result;
            }
            exist = userRepository.findByNickname(username);
            if (exist != null) {
                result.setMessage("昵称已存在");
                return result;
            }
            User user = new User();
            user.setPassword(password);
            user.setUsername(username);
            user.setNickname(nickname);
            userService.saveUser(user);
            result.setMessage("注册成功");
            result.setOk(true);
        } catch (Exception e) {
            log.error("", e);
            result.setMessage("注册失败");
        }
        return result;
    }


    /**
     * 只有登陆的人才能修改密码
     *
     * @param oldPassord
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public JsonResult modify(final HttpSession session, @RequestParam("old") String oldPassord, @RequestParam("new") String newPassword) {
        JsonResult result = new JsonResult();
        try {
            String username = session.getAttribute("username").toString();
            if (username == null) {
                result.setMessage("用户未登录");
                return result;
            }
            User user = userRepository.findByUsername(username);
            if (user == null) {
                result.setMessage("用户不存在");
                return result;
            }
            userService.modify(user, oldPassord, newPassword);
            result.setOk(true);
            result.setMessage("修改密码成功");
        } catch (Exception e) {
            log.error("", e);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
