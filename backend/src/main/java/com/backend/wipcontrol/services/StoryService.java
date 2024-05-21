package com.backend.wipcontrol.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.wipcontrol.dto.story.StoryBacklogDTO;
import com.backend.wipcontrol.dto.story.StoryDTOConverter;
import com.backend.wipcontrol.dto.story.StoryPostDTO;

import com.backend.wipcontrol.model.Story;
import com.backend.wipcontrol.repository.StoryRepository;

import javax.persistence.EntityExistsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    
    /**
     * get all stories
     *
     * @return stories list
     */
    public Map<String, Object> getAllStories(int nPage){
        // parameter page
        Pageable parametersPage = PageRequest.of(nPage,30, Sort.by("name").ascending());
        Page<Story> listStories = storyRepository.findAll(parametersPage);
        // stories list to DTO
        StoryDTOConverter storyDTOConverter = new StoryDTOConverter();
        List<StoryBacklogDTO> listStoriesDTO = listStories.getContent().stream()
                .map(storyDTOConverter::convertToBacklogDTO)
                .collect(Collectors.toList());
        // Create object
        Map<String, Object> paginatedResponse = new HashMap<>();
        paginatedResponse.put("stories", listStoriesDTO);
        paginatedResponse.put("currentPage", listStories.getNumber());
        paginatedResponse.put("totalBacklog", listStories.getTotalElements());
        paginatedResponse.put("totalPages", listStories.getTotalPages());
        return paginatedResponse;
    }

    
    /**
     * get list of stories
     *
     * @return stories list
     */
    public List<Story> getAllStoriesMini() {
        List<Story> listStories = storyRepository.findStoriesCurrentSprint(); 
        if(listStories.isEmpty()) {
            throw new ResourceNotFoundException("No User Stories in Backlog");
        } else {
            return listStories;
        }
    }

    /**
     * get story by id
     * 
     * @param id story id
     * @return Story
     */
    public Story getStoryById(Long id) {
        return storyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Story not found: " + id));
    }

    /**
     * get next 10 stories
     *
     * @return list with next 10 stories
     */
    public List<Story> getNext10Stories() {
        return storyRepository.findNext10ByOrderByPointsDesc();
    }

    /**
     * delete story by id
     *
     * @param id story id
     * @return deleted story
     */
    public Story deleteStoryById(Long id) {
        Story story = storyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Story not found: " + id));
        storyRepository.deleteById(id);
        return story;
    }

    /**
     * add new story
     *
     * @param newStory new story
     * @return Story added
     */
    public Story addStoryAPI(StoryPostDTO newStory) {
        Story storyExiste = storyRepository.findByName(newStory.getName());
        if(storyExiste != null) {
            throw new EntityExistsException("Story already exist");
        }
        Story storyCreated = new Story();
        storyCreated.setName(newStory.getName());
        storyCreated.setAssigned(newStory.getAssigned());
        storyCreated.setEstimation(newStory.getEstimation());
        storyCreated.setStatus(newStory.getStatus());
        storyCreated.setCriteria(newStory.getCriteria());
        storyCreated.setDescription(newStory.getDescription());
        storyCreated.setSprint(newStory.getSprint());
        storyRepository.save(storyCreated);
        return storyCreated;
    }

    

    /**
     * update user story
     * @param idStoryToUpdate 
     * @param storyToUpdate 
     * @return updated user story
     */
    
    
    public Story updateStory(Long idStoryToUpdate, StoryPostDTO storyToUpdate) {
    	
        Story story = storyRepository.findById(idStoryToUpdate)
        .orElseThrow(() -> new ResourceNotFoundException("User story not found"));



        story.setName(storyToUpdate.getName() != null ? storyToUpdate.getName() : story.getName());
        story.setAssigned(storyToUpdate.getAssigned() != null ? storyToUpdate.getAssigned() : story.getAssigned());
        story.setEstimation(storyToUpdate.getEstimation() != null ? storyToUpdate.getEstimation() : story.getEstimation());
        story.setStatus(storyToUpdate.getStatus() != null ? storyToUpdate.getStatus() : story.getStatus());
        story.setDescription(storyToUpdate.getDescription() != null ? storyToUpdate.getDescription() : story.getDescription());
        story.setCriteria(storyToUpdate.getCriteria() != null ? storyToUpdate.getCriteria() : story.getCriteria());
        story.setSprint(storyToUpdate.getSprint() != null ? storyToUpdate.getSprint() : story.getSprint());
        story.setPoints(storyToUpdate.getPoints() != null ? storyToUpdate.getPoints() : story.getPoints());

    	storyRepository.save(story);

    	return story;
    }


}
