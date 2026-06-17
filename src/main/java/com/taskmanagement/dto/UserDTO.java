package com.taskmanagement.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}