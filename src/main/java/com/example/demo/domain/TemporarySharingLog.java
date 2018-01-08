package com.example.demo.domain;

import com.example.demo.Constants;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "TEMPORARY_SHARING_LOG", indexes = {@Index(name = "unique_index", columnList = "unique_id", unique = true)})
public class TemporarySharingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 暂时储存一个唯一表示，方便用户进行下载，下载完后此条记录立即删除，如果超过时间限制，那么记录也会删除
     */
    @Column(name = "unique_id")
    private String uniqueId;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audio_file_id")
    private AudioFile audioFile;

    /**
     * 过期时间
     */
    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "expired_date", nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime expiredDate;

    /**
     * 创建时间
     */
    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_date", nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime createdDate = DateTime.now();


    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Constants.SharingLogStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public AudioFile getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(AudioFile audioFile) {
        this.audioFile = audioFile;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public DateTime getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(DateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Constants.SharingLogStatus getStatus() {
        return status;
    }

    public void setStatus(Constants.SharingLogStatus status) {
        this.status = status;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }
}
