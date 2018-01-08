package com.example.demo.service;


import com.example.demo.Constants;
import com.example.demo.domain.AudioFile;
import com.example.demo.domain.User;
import com.example.demo.repository.AudioFileRepository;
import com.example.demo.utils.AudioFileIOUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

@Service
public class AudioFileService {
    private final Logger log = LoggerFactory.getLogger(AudioFileService.class);

    @Inject
    private AudioFileRepository audioFileRepository;

    @Transactional
    public void upload(String name, MultipartFile file, User user) throws Exception {
        //TODO:上传的文件自动去掉后缀名，变为和file一样的后缀名
        // 例如：用户上传文件2(MP3文件),那么数据库里存储name=2，type=mp3,服务器上的文件名为2.mp3
        // 用户也用2搜索，但是程序自动变成用2+mp3搜索
        int index = name.lastIndexOf(".");
        String filename = name;
        if (index != -1) {
            filename = name.substring(0, index);//不要后缀名的部分
        }
        String[] tmp = file.getOriginalFilename().split("\\.");
        String suffix = "." + tmp[tmp.length - 1];
        if (tmp != null || tmp.length != 0) {//如果源文件有后缀名
            filename = filename + suffix;//加上后缀名
        }

        Constants.FileType fileType = Constants.FileType.parse(suffix);
        if (fileType == null) {
            throw new Exception("文件类型不存在");
        }
        AudioFileIOUtils.uploadFile(file, filename);//将文件保存在服务器上


        AudioFile audioFile = new AudioFile();
        audioFile.setName(name);
        audioFile.setUser(user);
        audioFile.setFilename(filename);
        audioFile.setFileType(fileType);
        audioFileRepository.save(audioFile);//保存记录

    }

    @Transactional
    public void delete(AudioFile audioFile) throws Exception {
        audioFileRepository.delete(audioFile);
        File file = AudioFileIOUtils.readAudioFile(audioFile.getFilename());
        if (file != null) {
            file.delete();//删掉文件
        }
    }

    @Transactional
    public void download(AudioFile audioFile, final HttpServletResponse response) throws Exception {
        response.setContentType(audioFile.getFileType().getContentType());//TODO
        String encodedName = URLEncoder.encode(audioFile.getFilename(), "utf-8");
        response.setHeader("Content-disposition", "attachment; filename=\"" + encodedName + "\";filename*=utf-8''" + encodedName);

        //得到文件的输入流并写入response
        File dowloadFile = AudioFileIOUtils.readAudioFile(audioFile.getFilename());
        FileInputStream fis = new FileInputStream(dowloadFile);
        IOUtils.copy(fis, response.getOutputStream());
        response.addHeader("Content-Length", "" + dowloadFile.length());
        fis.close();
    }
}
