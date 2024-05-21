package com.backend.wipcontrol.services;

import com.backend.wipcontrol.dto.story.StoryBacklogDTO;
import com.backend.wipcontrol.dto.story.StoryDTOConverter;
import com.backend.wipcontrol.dto.sched.SchedPostDTO;
import com.backend.wipcontrol.model.*;
import com.backend.wipcontrol.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

@Service
public class SchedService {

    @Autowired
    private SchedRepository schedRepository;

    @Autowired
    private StoryRepository storyRepository;

    
    /**
     * get todays scheds
     *
     * @return
     */
    public HashMap<Sched, HashMap<String, Object>> getTodayScheds() {
        ZoneId zoneId = ZoneId.of("Europe/Madrid");
        List<Sched> schedsToday = schedRepository.findByDateEquals(LocalDate.now(zoneId));
        if(schedsToday.isEmpty()) {
            throw new ResourceNotFoundException("No scheds for today");
        }
        return convertToBacklogDTO(schedsToday);
    }

    /**
     * get all sched from today
     *
     * @return 
     */
    public HashMap<Sched, HashMap<String, Object>> getSchedsAfterToday() {
        ZoneId zoneId = ZoneId.of("Europe/Madrid");
        List<Sched> schedsAfterToday = schedRepository.findByDateGreaterThanEqualOrderByDate(LocalDate.now(zoneId));
        if(schedsAfterToday.isEmpty()) {
            throw new ResourceNotFoundException("No scheds after today");
        }
        return convertToBacklogDTO(schedsAfterToday);
    }

    /**
     * @param 
     * @return 
     */
    private HashMap<Sched, HashMap<String, Object>> convertToBacklogDTO(List<Sched> scheds) {
        HashMap<Sched, HashMap<String, Object>> schedStory = new HashMap<>();
        StoryDTOConverter storyConverter = new StoryDTOConverter();
         scheds.forEach((sched) -> {
            StoryBacklogDTO storyBacklog = storyConverter.convertToBacklogDTO(sched.getStory());
            HashMap<String, Object> infoStory = new HashMap<>() {{
                put("story", storyBacklog);
                   }};
            schedStory.put(sched, infoStory);
        });
        return schedStory;
    }

    
    /**
     * Create new sched
     *
     * @param newSesion
     * @return sched
     */
    public Boolean createSched(SchedPostDTO newSched) {
        Story storyExists = storyRepository.findById(newSched.getId_story())
                                                        .orElseThrow( () -> new ResourceNotFoundException("Does not exist user story id: " + newSched.getId_story()));
        

        String[] horasArray = newSched.getHours().split(",");

        for(String hora : horasArray){
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            try {
                java.util.Date horaParseada = dateFormat.parse(hora);
                Time horaFormatoTime = new Time(horaParseada.getTime());
                Sched createdSched = new Sched();
                createdSched.setDate(newSched.getDate());
                createdSched.setHour(horaFormatoTime);
                createdSched.setStory(storyExists);
                schedRepository.save(createdSched);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Delete sched
     *
     * @param idSched
     * @return 
     */
    public Sched deleteSched(Long idSched) {
        Sched schedExists = schedRepository.findById(idSched)
                .orElseThrow(() -> new ResourceNotFoundException("Does not exist sched with id: " + idSched));
        schedRepository.delete(schedExists);
        return schedExists;
    }
}
