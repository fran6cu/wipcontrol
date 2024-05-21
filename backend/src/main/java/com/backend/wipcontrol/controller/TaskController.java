package com.backend.wipcontrol.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.backend.wipcontrol.dto.task.TaskBacklogDTO;
import com.backend.wipcontrol.dto.task.TaskDTOConverter;
import com.backend.wipcontrol.dto.task.TaskMiniDTO;
import com.backend.wipcontrol.dto.task.TaskPostDTO;
import com.backend.wipcontrol.model.Task;
import com.backend.wipcontrol.services.TaskService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    

    
     /**
     * get comments of a story
     */
    @GetMapping("/tasks/story/{idStory}/page/{nPage}")
    public Map<String, Object> getTasksStory(@PathVariable Long idStory, @PathVariable int nPage) {
       
        return taskService.getTasksStory(idStory, nPage);
    }

    /**
     * get list task
     *
     * @return List 
     */
    @GetMapping("/tasksMini")
    public List<TaskMiniDTO> getAllTask() {
        List<Task> tasks = taskService.getAllTaskMini();
        TaskDTOConverter taskDTOConverter = new TaskDTOConverter();
        return tasks.stream().map(taskDTOConverter::convertToMiniDTO).collect(Collectors.toList());
    }

    /**
     * get task by id
     *
     * @param idTask 
     * @return Task or 404
     */
    @GetMapping("/tasks")
    public TaskBacklogDTO getTaskById(@RequestParam Long idTask) {
        Task taskEncontrada = taskService.getTaskById(idTask);
        TaskDTOConverter taskDTOConverter = new TaskDTOConverter();
        return taskDTOConverter.convertToBacklogDTO(taskEncontrada);
    }

    /**
     * delete task by id
     *
     * @param idTask 
     * @return deleted task 
     */
    @DeleteMapping("/delTask/{idTask}")
    public Task deleteTaskById(@PathVariable Long idTask) {
        return taskService.deleteTaskById(idTask);
    }

    /**
     * add new task with data from api call
     * Return HTTP code 
     */
    @PostMapping("/tasks")
    public Task addTaskAPI(@RequestBody TaskPostDTO newTask){
        return taskService.addTaskAPI(newTask);
    }


    @PutMapping(value = "/tasks")
    public Task updateTask(
        @RequestParam Long idTask,
        @RequestBody TaskPostDTO taskToUpdate)
 {
    return taskService.updateTask(idTask, taskToUpdate);
}

    
}
