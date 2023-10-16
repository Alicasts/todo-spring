package com.acast.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        taskModel.setUserId((UUID) idUser);

        var taskStartDate = taskModel.getStartAt();
        var taskEndDate = taskModel.getEndAt();
        if (currentDate.isAfter(taskStartDate) || currentDate.isAfter(taskEndDate) && taskModel.getStartAt().isAfter(taskModel.getEndAt()) ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad date");
        }
        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }
    
    private Boolean validateDates(LocalDateTime startAt, LocalDateTime endAt) {
        var currentDate = LocalDateTime.now();
        
        if (currentDate.isAfter(startAt) || currentDate.isAfter(endAt)) {
            return false;
        }
        if (startAt.isAfter(endAt)) {
            return false;
        }

        return true;
    }
    
}
