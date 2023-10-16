package com.acast.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        var isDatesValid = validateDates(taskStartDate, taskEndDate);
        if (!isDatesValid) {
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

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var targetUserId = request.getAttribute("idUser");
        var targetUserTasksList = this.taskRepository.findByUserId((UUID)targetUserId);
        return targetUserTasksList;
    }
    
}
