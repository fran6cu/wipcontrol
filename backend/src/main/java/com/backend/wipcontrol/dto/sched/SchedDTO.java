package com.backend.wipcontrol.dto.sched;

import com.backend.wipcontrol.dto.story.StoryBacklogDTO;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;

@Getter
@Setter
public class SchedDTO {

    private Long id;

    private LocalDate date;

    private Time hour;

    private StoryBacklogDTO storyBacklog;
}
