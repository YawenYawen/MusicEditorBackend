package com.example.demo.service;


import com.example.demo.Constants;
import com.example.demo.controller.TemporarySharingLogController;
import com.example.demo.domain.AudioFile;
import com.example.demo.domain.TemporarySharingLog;
import com.example.demo.domain.User;
import com.example.demo.repository.AudioFileRepository;
import com.example.demo.repository.TemporarySharingLogRepository;
import com.example.demo.utils.AudioFileIOUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.util.Random;

@Service
@Transactional
public class TemporarySharingLogService {

    private final Logger log = LoggerFactory.getLogger(TemporarySharingLogService.class);

    @Inject
    private TemporarySharingLogRepository temporarySharingLogRepository;

    @Inject
    private AudioFileRepository audioFileRepository;

    @Transactional
    public String create(User friend, AudioFile audioFile) throws Exception {
        TemporarySharingLog temporarySharingLog = new TemporarySharingLog();
        temporarySharingLog.setAudioFile(audioFile);
        //从现在开始24小时之内分享有效
        temporarySharingLog.setExpiredDate(temporarySharingLog.getCreatedDate().plusDays(Constants.DUATION));
        temporarySharingLog.setReceiver(friend);
        String uniqueId = createUniqueId();
        temporarySharingLog.setUniqueId(uniqueId);
        temporarySharingLog.setStatus(Constants.SharingLogStatus.available);
        temporarySharingLogRepository.save(temporarySharingLog);
        return uniqueId;
    }


    /**
     * 下载的时候同时拷贝一份原文件
     *
     * @param user
     * @param temporarySharingLog
     * @return
     */
    @Transactional
    public void download(User user, TemporarySharingLog temporarySharingLog, final HttpServletResponse response) throws Exception {
        String newName = temporarySharingLog.getAudioFile().getName() + "_" + DateTime.now().toString("yyyyMMddHHmmss");
        String newFilename = DateTime.now().toString("yyyyMMddHHmmss") + "_" + temporarySharingLog.getAudioFile().getFilename();

        response.setContentType(temporarySharingLog.getAudioFile().getFileType().getContentType());//TODO
        String encodedName = URLEncoder.encode(newFilename, "utf-8");
        response.setHeader("Content-disposition", "attachment; filename=\"" + encodedName + "\";filename*=utf-8''" + encodedName);

        //在服务器上拷贝
        boolean b = AudioFileIOUtils.copyFile(temporarySharingLog.getAudioFile().getFilename(), newFilename);
        if (!b) {
            throw new Exception("拷贝文件时出错");
        }
        //再得到这个文件的输出流
        File downloadFile = AudioFileIOUtils.readAudioFile(newFilename);
        FileInputStream fis = new FileInputStream(downloadFile);
        //写入response
        IOUtils.copy(fis, response.getOutputStream());
        response.addHeader("Content-Length", "" + downloadFile.length());
        fis.close();

        AudioFile audioFile = new AudioFile();
        audioFile.setUser(user);
        audioFile.setName(newName);
        audioFile.setFilename(newFilename);
        audioFile.setFileType(temporarySharingLog.getAudioFile().getFileType());
        audioFileRepository.save(audioFile);

        temporarySharingLog.setStatus(Constants.SharingLogStatus.finished);
        temporarySharingLogRepository.save(temporarySharingLog);

    }

    private String createUniqueId() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Constants.SHARING_ID_LENGTH; i++) {
            int num = random.nextInt(62);
            builder.append(str.charAt(num));
        }
        return builder.toString();
    }
}
