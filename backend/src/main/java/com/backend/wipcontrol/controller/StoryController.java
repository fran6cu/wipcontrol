package com.backend.wipcontrol.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.backend.wipcontrol.dto.story.StoryBacklogDTO;
import com.backend.wipcontrol.dto.story.StoryDTOConverter;
import com.backend.wipcontrol.dto.story.StoryMiniDTO;
import com.backend.wipcontrol.dto.story.StoryPostDTO;
import com.backend.wipcontrol.model.Story;
import com.backend.wipcontrol.services.StoryService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    

    /**
     * get stories list
     *
     * @return 
     */
    @GetMapping("/stories/page")
    public Map<String, Object> getAllStories(@RequestParam int numberPage) {
        return storyService.getAllStories(numberPage);
    }

    /**
     * get list stories
     *
     * @return List 
     */
    @GetMapping("/storiesMini")
    public List<StoryMiniDTO> getAllStories() {
        List<Story> stories = storyService.getAllStoriesMini();
        StoryDTOConverter storyDTOConverter = new StoryDTOConverter();
        return stories.stream().map(storyDTOConverter::convertToMiniDTO).collect(Collectors.toList());
    }

    /**
     * get story by id
     *
     * @param idStory 
     * @return Story or 404
     */
    @GetMapping("/stories")
    public StoryBacklogDTO getStoryById(@RequestParam Long idStory) {
        Story storyFound = storyService.getStoryById(idStory);
        StoryDTOConverter storyDTOConverter = new StoryDTOConverter();
        return storyDTOConverter.convertToBacklogDTO(storyFound);
    }

    /**
     * get stories by points
     *
     * @return 
     */
    @GetMapping("/stories/next")
    public List<StoryBacklogDTO> getNextStories() {
        List<Story> stories = storyService.getNext10Stories();
        StoryDTOConverter storyDTOConverter = new StoryDTOConverter();
        return stories.stream().map(storyDTOConverter::convertToBacklogDTO).collect(Collectors.toList());
    }

    /**
     * delete story by id
     *
     * @param idStory 
     * @return deleted story 
     */
    @DeleteMapping("/delStory/{idStory}")
    public Story deleteStoryById(@PathVariable Long idStory) {
        return storyService.deleteStoryById(idStory);
    }

    /**
     * add new story with data from api call
     * Return HTTP code 
     */
    @PostMapping("/stories")
    public Story addStoryAPI(@RequestBody StoryPostDTO newStory){
        return storyService.addStoryAPI(newStory);
    }


    @PutMapping(value = "/stories")
public Story updateStory(
        @RequestParam Long idStory,
        @RequestBody StoryPostDTO storyToUpdate)
 {
    System.out.println(storyToUpdate);
    return storyService.updateStory(idStory, storyToUpdate);
}

    

}
