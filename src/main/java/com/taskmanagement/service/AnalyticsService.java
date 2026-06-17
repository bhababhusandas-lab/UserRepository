package com.taskmanagement.service;

import com.taskmanagement.dto.TaskAnalyticsDTO;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskAnalyticsDTO getAnalyticsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        long totalTasks = taskRepository.countByUserId(userId);
        long completedTasks = taskRepository.countByUserIdAndStatus(userId, "COMPLETED");
        long pendingTasks = taskRepository.countByUserIdAndStatus(userId, "PENDING");
        long inProgressTasks = taskRepository.countByUserIdAndStatus(userId, "IN_PROGRESS");
        long highPriorityTasks = taskRepository.countByUserIdAndPriority(userId, "HIGH");
        long mediumPriorityTasks = taskRepository.countByUserIdAndPriority(userId, "MEDIUM");
        long lowPriorityTasks = taskRepository.countByUserIdAndPriority(userId, "LOW");

        double completionPercentage = totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0.0;

        return TaskAnalyticsDTO.builder()
                .userId(userId)
                .userName(user.getFirstName() + " " + user.getLastName())
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .pendingTasks(pendingTasks)
                .inProgressTasks(inProgressTasks)
                .highPriorityTasks(highPriorityTasks)
                .mediumPriorityTasks(mediumPriorityTasks)
                .lowPriorityTasks(lowPriorityTasks)
                .completionPercentage(Math.round(completionPercentage * 100.0) / 100.0)
                .build();
    }

    public List<TaskAnalyticsDTO> getAnalyticsForAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> getAnalyticsForUser(user.getId()))
                .collect(Collectors.toList());
    }
}