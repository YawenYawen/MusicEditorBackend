package com.example.demo.service;


import com.example.demo.domain.User;
import com.example.demo.dtos.JsonResult;
import com.example.demo.repository.UserRepository;
import com.google.common.base.Strings;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.security.MessageDigest;


@Service
@Transactional
public class UserService {

    @Inject
    private UserRepository userRepository;

    @Transactional
    public void saveUser(User user) throws Exception {
        if (user.getId() == null || !Strings.isNullOrEmpty(user.getPassword())) {
            String encryptedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(encryptedPassword);
        }
        userRepository.save(user);
    }

    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username.trim());
        if (user == null) {
            return false;
        }
        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return false;
        }
        return true;
    }

    @Transactional
    public void modify(User user, String oldPassword, String newPassword) throws Exception {
        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(oldPassword.getBytes()))) {
           throw new Exception("旧密码输入错误");
        }
        user.setPassword(newPassword);
        saveUser(user);
    }

}
