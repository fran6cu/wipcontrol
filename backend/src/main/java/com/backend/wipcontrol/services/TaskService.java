package com.backend.wipcontrol.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.wipcontrol.dto.task.TaskBacklogDTO;
import com.backend.wipcontrol.dto.task.TaskDTOConverter;
import com.backend.wipcontrol.dto.task.TaskPostDTO;
import com.backend.wipcontrol.model.Story;
import com.backend.wipcontrol.model.Task;
import com.backend.wipcontrol.repository.TaskRepository;
import com.backend.wipcontrol.repository.StoryRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StoryRepository storyRepository;

    
    /**
     * get all task
     *
     * @return task list
     */
   
    public Map<String, Object> getTasksStory(Long idStory, int nPage) {
        Pageable datosPage = PageRequest.of(nPage,  10, Sort.by("name").descending());
        Page<Task> listTask = taskRepository.findAllByStoryId(idStory, datosPage);
        TaskDTOConverter taskDTOConverter = new TaskDTOConverter();
        List<TaskBacklogDTO> listTaskDTO = listTask.getContent().stream()
            .map(taskDTOConverter::convertToBacklogDTO)
            .collect(Collectors.toList());

        Map<String, Object> paginatedResponse = new HashMap<>();
        paginatedResponse.put("tasks", listTaskDTO);
        paginatedResponse.put("currentPage", listTask.getNumber());
        paginatedResponse.put("totalBacklog", listTask.getTotalElements());
        paginatedResponse.put("totalPages", listTask.getTotalPages());

        return paginatedResponse;
    }

    /**
     * get list of task
     *
     * @return task list
     */
    public List<Task> getAllTaskMini() {
        List<Task> listTask = taskRepository.findAllByOrderByName();
        if(listTask.isEmpty()) {
            throw new ResourceNotFoundException("No task in user task");
        } else {
            return listTask;
        }
    }

    /**
     * get task by id
     *
     * @param id task id
     * @return Task
     */
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    }

    
    /**
     * delete task by id
     *
     * @param id task id
     * @return deleted task
     */
    public Task deleteTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        taskRepository.deleteById(id);
        return task;
    }

    /**
     * add new task
     *
     * @param newTask new task
     * @return Task added
     */
     @Transactional
     public Task addTaskAPI(TaskPostDTO newTask) {
        try {
        Optional<Story> storyExists = storyRepository.findById(newTask.getId_story());
        
        if(storyExists.isEmpty()) {
            throw new ResourceNotFoundException("User story does not exist");
        }

        Task task = new Task();
        task.setStory(storyExists.get());
        task.setName(newTask.getName());
        task.setStatus(newTask.getStatus());
        task.setCriteria(newTask.getCriteria());
        task.setDescription(newTask.getDescription());
        System.out.println("Adding new task: " + newTask.toString());
        return taskRepository.save(task);
    } catch (DataAccessException e) {
        // Log or handle the exception
        throw new RuntimeException("Error adding task", e);
    }
    }
    
    /**
     * update user task
     * @param idTaskToUpdate 
     * @param taskToUpdate 
     * @return updated user task
     */
    
    
    public Task updateTask(Long idTaskToUpdate, TaskPostDTO taskToUpdate) {
    	
        Task task = taskRepository.findById(idTaskToUpdate)
        .orElseThrow(() -> new ResourceNotFoundException("User task not found"));

        task.setName(taskToUpdate.getName() != null ? taskToUpdate.getName() : task.getName());
        task.setStatus(taskToUpdate.getStatus() != null ? taskToUpdate.getStatus() : task.getStatus());
        task.setDescription(taskToUpdate.getDescription() != null ? taskToUpdate.getDescription() : task.getDescription());
        task.setCriteria(taskToUpdate.getCriteria() != null ? taskToUpdate.getCriteria() : task.getCriteria());

        System.out.println("Updating task with ID " + idTaskToUpdate + ": " + taskToUpdate.toString());

    	taskRepository.save(task);

    	return task;
    }

    
}
