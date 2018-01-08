package com.example.demo.repository;

import com.example.demo.domain.TemporarySharingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporarySharingLogRepository extends JpaRepository<TemporarySharingLog, Long> {

    TemporarySharingLog findByUniqueId(String uniqueId);
}
