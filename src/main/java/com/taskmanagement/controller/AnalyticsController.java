package com.taskmanagement.controller;

import com.taskmanagement.dto.TaskAnalyticsDTO;
import com.taskmanagement.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<TaskAnalyticsDTO> getUserAnalytics(@PathVariable Long userId) {
        TaskAnalyticsDTO analytics = analyticsService.getAnalyticsForUser(userId);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping
    public ResponseEntity<List<TaskAnalyticsDTO>> getAllUsersAnalytics() {
        List<TaskAnalyticsDTO> analytics = analyticsService.getAnalyticsForAllUsers();
        return ResponseEntity.ok(analytics);
    }
}