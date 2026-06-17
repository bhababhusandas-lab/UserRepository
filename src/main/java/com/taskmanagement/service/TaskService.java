package com.taskmanagement.service;

import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.dto.CreateTaskRequest;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskDTO createTask(CreateTaskRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : "PENDING")
                .priority(request.getPriority() != null ? request.getPriority() : "MEDIUM")
                .user(user)
                .dueDate(request.getDueDate())
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToDTO(savedTask);
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        return mapToDTO(task);
    }

    public Page<TaskDTO> getTasksByUser(Long userId, int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Task> tasks = taskRepository.findByUserId(userId, pageable);
        return tasks.map(this::mapToDTO);
    }

    public Page<TaskDTO> filterAndSortTasks(Long userId, String status, String priority, int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Task> tasks = taskRepository.findByUserId(userId, pageable);

        // Apply filtering in memory (or use custom queries for optimization)
        if (status != null && !status.isEmpty()) {
            tasks = tasks.filter(t -> t.getStatus().equals(status));
        }
        if (priority != null && !priority.isEmpty()) {
            tasks = tasks.filter(t -> t.getPriority().equals(priority));
        }

        return tasks.map(this::mapToDTO);
    }

    public TaskDTO updateTask(Long id, CreateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : task.getStatus());
        task.setPriority(request.getPriority() != null ? request.getPriority() : task.getPriority());
        task.setDueDate(request.getDueDate());

        // If status is being set to COMPLETED, set the completedAt timestamp
        if ("COMPLETED".equals(request.getStatus()) && !task.getStatus().equals("COMPLETED")) {
            task.setCompletedAt(LocalDateTime.now());
        }

        Task updatedTask = taskRepository.save(task);
        return mapToDTO(updatedTask);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        taskRepository.delete(task);
    }

    private TaskDTO mapToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .userId(task.getUser().getId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .dueDate(task.getDueDate())
                .completedAt(task.getCompletedAt())
                .build();
    }
}