package com.backend.wipcontrol.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.wipcontrol.dto.sprint.SprintBacklogDTO;
import com.backend.wipcontrol.dto.sprint.SprintDTOConverter;
import com.backend.wipcontrol.dto.sprint.SprintPostDTO;

import com.backend.wipcontrol.model.Sprint;
import com.backend.wipcontrol.repository.SprintRepository;

import javax.persistence.EntityExistsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    /**
     * get all sprints
     *
     * @return 
     */
    public List<Sprint> getAllNames() {
        List<Sprint> sprints = sprintRepository.findOpenSprint();
        if(sprints.isEmpty()) {
            throw new ResourceNotFoundException("No sprints");
        } else {
            return sprints;
        }
    }


    /**
     * get all sprints
     *
     * @return sprints list
     */
    public Map<String, Object> getAllSprints(int nPage){
        // parameter page
        Pageable parametersPage = PageRequest.of(nPage,30, Sort.by("name").ascending());
        Page<Sprint> listSprints = sprintRepository.findAll(parametersPage);
        // sprints list to DTO
        SprintDTOConverter sprintDTOConverter = new SprintDTOConverter();
        List<SprintBacklogDTO> listSprintsDTO = listSprints.getContent().stream()
                .map(sprintDTOConverter::convertToBacklogDTO)
                .collect(Collectors.toList());
        // Create object
        Map<String, Object> paginatedResponse = new HashMap<>();
        paginatedResponse.put("sprints", listSprintsDTO);
        paginatedResponse.put("currentPage", listSprints.getNumber());
        paginatedResponse.put("totalBacklog", listSprints.getTotalElements());
        paginatedResponse.put("totalPages", listSprints.getTotalPages());
        return paginatedResponse;
    }

    /**
     * get list of sprints
     *
     * @return sprints list
     */
    public List<Sprint> getAllSprintsMini() {
        List<Sprint> listSprints = sprintRepository.findAllByOrderByName();
        if(listSprints.isEmpty()) {
            throw new ResourceNotFoundException("No User Sprints in Backlog");
        } else {
            return listSprints;
        }
    }

    /**
     * get sprint by id
     *
     * @param id sprint id
     * @return Sprint
     */
    public Sprint getSprintById(Long id) {
        return sprintRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sprint not found: " + id));
    }

    /**
     * get sprint by name
     *
     * @param id sprint id
     * @return Sprint
     */
    public Sprint getSprintByName(String name) {
         return sprintRepository.findByName(name);
    }

   

    /**
     * delete sprint by id 
     *
     * @param id sprint id
     * @return deleted sprint
     */
    public Sprint deleteSprintById(Long id) {
        Sprint sprint = sprintRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sprint not found: " + id));
        sprintRepository.deleteById(id);
        return sprint;
    }

    /**
     * add new sprint
     *
     * @param newSprint new sprint
     * @return Sprint added
     */
    public Sprint addSprintAPI(SprintPostDTO newSprint) {
        Sprint sprintExiste = sprintRepository.findByName(newSprint.getName());
        if(sprintExiste != null) {
            throw new EntityExistsException("Sprint already exist");
        }
        Sprint sprintCreated = new Sprint();
        sprintCreated.setName(newSprint.getName());
        sprintCreated.setStatus(newSprint.getStatus());
        sprintRepository.save(sprintCreated);
        return sprintCreated;
    }

    

    /**
     * update user sprint
     * @param idSprintToUpdate 
     * @param sprintToUpdate 
     * @return updated user sprint
     */
    
      
    public Sprint updateSprint(String idSprintToUpdate, SprintPostDTO sprintToUpdate) {
    	
        Sprint sprint = sprintRepository.findByName(idSprintToUpdate);

        sprint.setName(sprintToUpdate.getName() != null ? sprintToUpdate.getName() : sprint.getName());
        sprint.setStatus(sprintToUpdate.getStatus() != null ? sprintToUpdate.getStatus() : sprint.getStatus());
        

    
    	sprintRepository.save(sprint);

    	return sprint;
    }


    public long counter() {
         return sprintRepository.count();
    }

    /**
     * Get the status of a sprint by its name.
     *
     * @param sprintName Sprint name.
     * @return Sprint status.
     * @throws ResourceNotFoundException If the sprint is not found.
     */
    public String getSprintStatusByName(String sprintName) {
        Sprint sprint = sprintRepository.findByName(sprintName);
        if (sprint == null) {
            throw new ResourceNotFoundException("Sprint not found: " + sprintName);
        }
        return sprint.getStatus();
    }

       
}
