package com.example.demo.repository;

import com.example.demo.domain.AudioFile;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {

    AudioFile findByName(String name);

    @Query("select af.name from AudioFile af where af.user = :user")
    List<String> findAllNamesByUser(@Param("user") User user);
}
