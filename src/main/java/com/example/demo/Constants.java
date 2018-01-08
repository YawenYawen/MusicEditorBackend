package com.example.demo;

import com.example.demo.utils.OSInfoUtils;
import com.google.common.base.Strings;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

public final class Constants {

    /**
     * 默认文件存储在服务器的位置
     */
    public static final String AUDIO_FILE_SAVE_PATH = "d:/musicEditor/audio_files|~/musicEditor/audio_files|/musicEditor/audio_files";

    /**
     * 默认1天后分享过期
     */
    public static final int DUATION = 1;

    /**
     * 分享时暂时存储的铃声唯一分享id长度
     */
    public static final int SHARING_ID_LENGTH = 20;

    /**
     * 根据不同的操作系统返回不同的路径,规则是:
     * Windows|Mac|Linux
     *
     * @return 操作系统对应的路径
     */
    public static String getDataPath() {
        String[] array = AUDIO_FILE_SAVE_PATH.split("\\|");
        String userDir = System.getProperty("user.home");
        if (OSInfoUtils.isWindows()) {
            return array[0];
        } else if (OSInfoUtils.isMacOS()) {
            if (array[1].startsWith("~")) {
                return userDir + array[1].substring(1);
            }
            return array[1];
        } else if (OSInfoUtils.isLinux()) {
            if (array[2].startsWith("~")) {
                return userDir + array[2].substring(1);
            }
            return array[2];
        }
        return null;
    }

    public enum SharingLogStatus {
        available("未被下载，未过期"),
        expired("未被下载，已过期"),
        finished("已被下载");

        private String explaination;

        SharingLogStatus(String explaination) {
            this.explaination = explaination;
        }

        public String getExplaination() {
            return explaination;
        }
    }

    public enum FileType {
        mp3(".mp3", "audio/mp3"),
        mp4(".mp4", "video/mpeg4");

        private String suffix;
        private String contentType;

        FileType(String suffix, String contentType) {
            this.suffix = suffix;
            this.contentType = contentType;
        }

        public String getSuffix() {
            return suffix;
        }

        public String getContentType() {
            return contentType;
        }

        public static FileType parse(String suffix) {
            if (Strings.isNullOrEmpty(suffix)) {
                return null;
            }
            FileType[] types = FileType.values();
            for (FileType t : types) {
                if (Objects.equals(t.getSuffix(), suffix)) {
                    return t;
                }
            }
            return null;
        }
    }
}
