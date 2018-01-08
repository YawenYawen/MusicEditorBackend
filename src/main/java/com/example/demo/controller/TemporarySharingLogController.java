package com.example.demo.controller;

import com.example.demo.Constants;
import com.example.demo.domain.AudioFile;
import com.example.demo.domain.TemporarySharingLog;
import com.example.demo.domain.User;
import com.example.demo.dtos.JsonResult;
import com.example.demo.repository.AudioFileRepository;
import com.example.demo.repository.TemporarySharingLogRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TemporarySharingLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/share")
public class TemporarySharingLogController {

    private final Logger log = LoggerFactory.getLogger(TemporarySharingLogController.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private AudioFileRepository audioFileRepository;

    @Inject
    private TemporarySharingLogService temporarySharingLogService;

    @Inject
    private TemporarySharingLogRepository temporarySharingLogRepository;


    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public JsonResult download(final HttpSession session, @RequestParam("uniqueId") String uniqueId, final HttpServletResponse response) {
        JsonResult result = new JsonResult();
        try {
            String myname = session.getAttribute("username").toString();
            if (myname == null) {
                result.setMessage("用户未登录");
                return result;
            }
            User user = userRepository.findByUsername(myname);

            TemporarySharingLog temporarySharingLog = temporarySharingLogRepository.findByUniqueId(uniqueId);
            if (temporarySharingLog == null || temporarySharingLog.getReceiver().getId() != user.getId()) {
                result.setMessage("分享码错误");
                return result;
            }
            if (temporarySharingLog.getStatus() != Constants.SharingLogStatus.available) {
                result.setMessage("分享码" + temporarySharingLog.getStatus().getExplaination());
                return result;
            }
            temporarySharingLogService.download(user, temporarySharingLog, response);
            result.setOk(true);
            result.setMessage("分享成功");
        } catch (Exception e) {
            log.error("", e);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public JsonResult create(final HttpSession session, @RequestParam("username") String username, @RequestParam("name") String name) {
        JsonResult result = new JsonResult();
        try {
            String myname = session.getAttribute("username").toString();
            if (myname == null) {
                result.setMessage("用户未登录");
                return result;
            }
            User user = userRepository.findByUsername(myname);
            if (user == null) {
                result.setMessage("用户不存在");
                return result;
            }
            if (myname.equals(username)) {
                result.setMessage("不能分享给自己");
                return result;
            }
            User friend = userRepository.findByUsername(username);
            if (friend == null) {
                result.setMessage("对方不存在");
                return result;
            }
            AudioFile audioFile = audioFileRepository.findByName(name);
            if (audioFile == null) {
                result.setMessage("铃声不存在");
                return result;
            }
            if (audioFile.getUser().getId() != user.getId()) {
                result.setMessage("铃声不属于当前用户");
                return result;
            }
            String uniqueId = temporarySharingLogService.create(friend, audioFile);
            if (uniqueId.equals("")) {
                result.setMessage("分享失败");
                return result;
            }
            result.setMessage("分享成功");
            result.setOk(true);
            result.setObject(uniqueId);
        } catch (Exception e) {
            log.error("", e);
            result.setMessage("分享失败");
        }
        return result;

    }

}
