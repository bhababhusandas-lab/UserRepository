package com.taskmanagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAnalyticsDTO {

    private Long userId;
    private String userName;
    private Long totalTasks;
    private Long completedTasks;
    private Long pendingTasks;
    private Long inProgressTasks;
    private Long highPriorityTasks;
    private Long mediumPriorityTasks;
    private Long lowPriorityTasks;
    private Double completionPercentage;
}