package com.taskmanagement.repository;

import com.taskmanagement.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUserId(Long userId, Pageable pageable);
    List<Task> findByUserIdAndStatus(Long userId, String status);
    List<Task> findByUserIdAndPriority(Long userId, String priority);
    long countByUserIdAndStatus(Long userId, String status);
    long countByUserIdAndPriority(Long userId, String priority);
    long countByUserId(Long userId);
}