package com.backend.wipcontrol.controller;

import com.backend.wipcontrol.dto.sched.SchedDTO;
import com.backend.wipcontrol.dto.sched.SchedDTOConverter;
import com.backend.wipcontrol.dto.sched.SchedPostDTO;
import com.backend.wipcontrol.dto.story.StoryBacklogDTO;
import com.backend.wipcontrol.model.Sched;
import com.backend.wipcontrol.services.SchedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SchedController {

    private final SchedService schedService;

    /**
     * return list with schedules for today
     *
     * @return 
     */
    @GetMapping("/scheds/today")
    public List<SchedDTO> getTodayScheds() {
        HashMap<Sched, HashMap<String, Object>> schedStory = schedService.getTodayScheds();
        return convertSchedsDTO(schedStory);
    }

    /**
     * get all scheds after today
     *
     * @return List of schedules
     */
    @GetMapping("/scheds/afterToday")
    public List<SchedDTO> getSchedsAfterToday() {
        HashMap<Sched, HashMap<String, Object>> schedsAfterToday = schedService.getSchedsAfterToday();
        return convertSchedsDTO(schedsAfterToday);
    }

    /**
     * get all scheds of one us
     *
     * @param idStory 
     * @return 
     */
    @GetMapping("/scheds/afterToday/story")
    public List<SchedDTO> getSchedsAfterTodayByStory(@RequestParam Long idStory) {
        HashMap<Sched, HashMap<String, Object>> relSchedsAfterTodayStory = schedService.getSchedsAfterToday();
        HashMap<Sched, HashMap<String, Object>> schedsAfterTodayStory = relSchedsAfterTodayStory
                                                                                    .entrySet()
                                                                                    .stream()
                                                                                    .filter(entry -> {
                                                                                        HashMap<String, Object> infoSched = entry.getValue();
                                                                                        StoryBacklogDTO storyBacklogDTO = (StoryBacklogDTO) infoSched.get("story");
                                                                                        return storyBacklogDTO.getId().equals(idStory);
                                                                                    })
                                                                                    .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
        if(schedsAfterTodayStory.isEmpty()) throw new ResourceNotFoundException("No schedules for story with id: " + idStory);
        return convertSchedsDTO(schedsAfterTodayStory);
    }

    /**
     * get list of stories can be scheduled
     * @param storyRelation Hashmap 
     * @return list
     */
    private List<SchedDTO> convertSchedsDTO(HashMap<Sched, HashMap<String, Object>> storyRelation) {
        SchedDTOConverter schedConverter = new SchedDTOConverter();
        List<SchedDTO> schedsDTO = new ArrayList<>();
        storyRelation.forEach((sched, infoSched) -> {
            SchedDTO schedTransformed = schedConverter.convertToDTO(sched);
            schedTransformed.setStoryBacklog((StoryBacklogDTO) infoSched.get("story"));
            schedsDTO.add(schedTransformed);
        });
        return schedsDTO;
    }

    /**
     * New schedule
     *
     * @param newSched 
     * @return Sched
     */
    @PostMapping("/scheds")
    public ResponseEntity<Boolean> createSched(@RequestBody SchedPostDTO newSched) {
        Boolean schedsStored = schedService.createSched(newSched);

        if(!schedsStored) return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
    }

    /**
     * delete sched
     *
     * @param idSched
     * @return 
     */
    @DeleteMapping("/scheds")
    public Sched deleteSched(@RequestParam Long idSched) {
        return schedService.deleteSched(idSched);
    }

}
