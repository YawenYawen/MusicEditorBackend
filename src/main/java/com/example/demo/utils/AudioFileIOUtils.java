package com.example.demo.utils;

import com.example.demo.Constants;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class AudioFileIOUtils {

    public static boolean copyFile(String source, String dest) {
        try {
            String rootPath = Constants.getDataPath();
            File basePath = new File(rootPath);
            if (!basePath.exists()) {
                basePath.mkdirs();
            }
            File destFile = new File(basePath, dest);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destFile), 1024);

            File sourceFile = new File(basePath, source);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(sourceFile));

            IOUtils.copy(bufferedInputStream, bufferedOutputStream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void uploadFile(MultipartFile file, String name) throws Exception {
        String rootPath = Constants.getDataPath();
        File basePath = new File(rootPath);
        if (!basePath.exists()) {
            basePath.mkdirs();
        }

        File destFile = new File(basePath, name);
        file.transferTo(destFile);
    }

    public static byte[] readAudioFileToBytes(String name) {
        try {
            String rootPath = Constants.getDataPath();
            File basePath = new File(rootPath);
            if (!basePath.exists()) {
                basePath.mkdirs();
            }
            File destFile = new File(basePath, name);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(destFile));
            byte[] bytes = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(bytes);
            bufferedInputStream.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static File readAudioFile(String name) {
        try {
            String rootPath = Constants.getDataPath();
            File basePath = new File(rootPath);
            if (!basePath.exists()) {
                basePath.mkdirs();
            }
            File destFile = new File(basePath, name);
            return destFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
