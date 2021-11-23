package com.mh.lamp.recording;

import com.mh.lamp.cue.Cue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "recording")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recording {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String videoTitle;
    @OneToMany
    @JoinColumn(name = "cue_id")
    private List<Cue> cueList;

}
