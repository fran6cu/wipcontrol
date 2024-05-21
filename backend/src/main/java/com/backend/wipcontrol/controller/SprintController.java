package com.backend.wipcontrol.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.backend.wipcontrol.dto.sprint.SprintBacklogDTO;
import com.backend.wipcontrol.dto.sprint.SprintDTOConverter;
import com.backend.wipcontrol.dto.sprint.SprintMiniDTO;
import com.backend.wipcontrol.dto.sprint.SprintPostDTO;
import com.backend.wipcontrol.model.Sprint;
import com.backend.wipcontrol.services.SprintService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;

     /**
     * return all sprints
     */
    @GetMapping("/sprints/names")
    public List<SprintBacklogDTO> getAllNames() {
        List<Sprint> sprints = sprintService.getAllNames();
        SprintDTOConverter converter = new SprintDTOConverter();
        List<SprintBacklogDTO> sprintsDTO = sprints.stream().map(converter::convertToBacklogDTO).collect(Collectors.toList());
        return sprintsDTO;
    }

    /**
     * get sprints list
     *
     * @return 
     */
    @GetMapping("/sprints/page")
    public Map<String, Object> getAllSprints(@RequestParam int numberPage) {
        return sprintService.getAllSprints(numberPage);
    }

    /**
     * get list sprints
     *
     * @return List 
     */
    @GetMapping("/sprintsMini")
    public List<SprintMiniDTO> getAllSprints() {
        List<Sprint> sprints = sprintService.getAllSprintsMini();
        SprintDTOConverter sprintDTOConverter = new SprintDTOConverter();
        return sprints.stream().map(sprintDTOConverter::convertToMiniDTO).collect(Collectors.toList());
    }

    /**
     * get sprint by id
     *
     * @param idSprint 
     * @return Sprint or 404
     */
    @GetMapping("/sprints")
    public SprintBacklogDTO getSprintById(@RequestParam Long idSprint) {
        Sprint sprintFound = sprintService.getSprintById(idSprint);
        SprintDTOConverter sprintDTOConverter = new SprintDTOConverter();
        return sprintDTOConverter.convertToBacklogDTO(sprintFound);
    }

    /**
     * delete sprint by id
     *
     * @param idSprint 
     * @return deleted sprint 
     */
    @DeleteMapping("/delSprint/{idSprint}")
    public Sprint deleteSprintById(@PathVariable Long idSprint) {
        return sprintService.deleteSprintById(idSprint);
    }

    /**
     * add new sprint with data from api call
     * Return HTTP code 
     */
    @PostMapping("/sprints")
    public Sprint addSprintAPI(@RequestBody SprintPostDTO newSprint){
        return sprintService.addSprintAPI(newSprint);
    }

    

    /**
     * update sprint by id
     *
     * @param sprintToUpdate 
     * @param sprintToUpdate 
     * @return 
     */
    
    @PutMapping(value = "/sprints")
       public Sprint updateSprint(
        @RequestParam String sprintName,
        @RequestBody SprintPostDTO sprintToUpdate)
 {
   
    return sprintService.updateSprint(sprintName, sprintToUpdate);
}


 /**
     * get sprint number
     *
     * @return 
     */
    @GetMapping("/sprints/counter")
    public long getNumSprints() {
        long counterSprints = sprintService.counter();
        return counterSprints;
    }

     /**
     * Get the status of a sprint by its name.
     *
     * @param sprintName Sprint name.
     * @return Sprint status.
     */
    @GetMapping("/sprints/statusByName")
    public String getSprintStatusByName(@RequestParam String sprintName) {
        return sprintService.getSprintStatusByName(sprintName);
    }

   
}
