package com.example.demo.controller;

import com.example.demo.domain.AudioFile;
import com.example.demo.domain.User;
import com.example.demo.dtos.JsonResult;
import com.example.demo.repository.AudioFileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AudioFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/audio-file")
public class AudioFileController {

    private final Logger log = LoggerFactory.getLogger(AudioFileController.class);

    @Inject
    private AudioFileService audioFileService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AudioFileRepository audioFileRepository;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResult upload(final HttpSession session, @RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
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
            audioFileService.upload(name, file, user);
            result.setMessage("上传成功");
            result.setOk(true);
        } catch (Exception e) {
            log.error("", e);
            result.setMessage("上传失败");
        }
        return result;
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public JsonResult download(final HttpServletResponse response, final HttpSession session, @RequestParam("name") String name) {
        JsonResult result = new JsonResult();
        try {
            String username = session.getAttribute("username").toString();
            if (username == null) {
                result.setMessage("用户未登录");
                return result;
//                response.sendError(1, "用户未登录");
//                return;
            }
            User user = userRepository.findByUsername(username);
            if (user == null) {
                result.setMessage("用户不存在");
                return result;
//                response.sendError(2, "用户不存在");
//                return;
            }
            AudioFile audioFile = audioFileRepository.findByName(name);
            if (audioFile == null || audioFile.getUser().getId() != user.getId()) {
                result.setMessage("铃声不存在");
                return result;
//                response.sendError(3, "铃声不存在");
//                return;
            }
            audioFileService.download(audioFile, response);
            result.setOk(true);
            result.setMessage("下载成功");
        } catch (Exception e) {
            log.error("下载失败", e);
//            response.addHeader("错误原因", e.getMessage());
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JsonResult delete(final HttpSession session, @RequestParam("name") String name) {
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
            AudioFile audioFile = audioFileRepository.findByName(name);
            if (audioFile == null || audioFile.getUser().getId() != user.getId()) {
                result.setMessage("铃声不存在");
                return result;
            }
            //但是不删除分享记录
            audioFileService.delete(audioFile);
            result.setMessage("删除成功");
            result.setOk(true);
        } catch (Exception e) {
            log.error("", e);
            result.setMessage("删除失败");
        }
        return result;

    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list(final HttpSession session) {
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
            List<String> list = audioFileRepository.findAllNamesByUser(user);
            result.setMessage("查找成功");
            result.setOk(true);
            result.setObject(list);
        } catch (Exception e) {
            log.error("", e);
            result.setMessage("查找失败");
        }
        return result;
    }


}
