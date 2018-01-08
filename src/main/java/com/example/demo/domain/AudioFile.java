package com.example.demo.domain;

import com.example.demo.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "AUDIO_FILE", indexes = {@Index(name = "name_index", columnList = "name", unique = true)})
public class AudioFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 歌曲名字
     */
    @NotNull
    private String name;

    /**
     * 铃声在实际存储时的文件名
     */
    @NotNull
    private String filename;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 创建时间
     */
    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_date", nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime createdDate = DateTime.now();

    /**
     * 文件类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 5)
    private Constants.FileType fileType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Constants.FileType getFileType() {
        return fileType;
    }

    public void setFileType(Constants.FileType fileType) {
        this.fileType = fileType;
    }
}
