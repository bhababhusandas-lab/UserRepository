package com.taskmanagement.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @Pattern(regexp = "PENDING|IN_PROGRESS|COMPLETED", message = "Status must be PENDING, IN_PROGRESS, or COMPLETED")
    private String status;

    @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "Priority must be LOW, MEDIUM, or HIGH")
    private String priority;

    @NotNull(message = "User ID is required")
    private Long userId;

    private LocalDateTime dueDate;
}