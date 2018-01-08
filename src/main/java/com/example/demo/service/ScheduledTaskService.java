package com.example.demo.service;

import com.example.demo.Constants;
import com.example.demo.domain.AudioFile;
import com.example.demo.domain.TemporarySharingLog;
import com.example.demo.repository.AudioFileRepository;
import com.example.demo.repository.TemporarySharingLogRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Service
@Configurable
@EnableScheduling
public class ScheduledTaskService {

    private final Logger log = LoggerFactory.getLogger(ScheduledTaskService.class);

    @Inject
    private TemporarySharingLogRepository temporarySharingLogRepository;


    /**
     * 负责定时更新过期的分享
     */
    @Scheduled(fixedRate = 100000000)//单位：ms
    @Transactional
    @Async
    public void updateSharingLog() {
        log.info("定时更新分享日志开始: " + DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        List<TemporarySharingLog> logs = temporarySharingLogRepository.findAll();
        logs.stream().forEach(a -> {
            if (a.getStatus() == Constants.SharingLogStatus.available && a.getExpiredDate().isBefore(DateTime.now())) {
                a.setStatus(Constants.SharingLogStatus.expired);
            }
        });
        temporarySharingLogRepository.save(logs);
        log.info("定时更新分享日志结束: " + DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));

    }
}
