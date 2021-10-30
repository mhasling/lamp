package com.mh.lamp.recording;

import com.mh.lamp.cue.Cue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer id;
    private String videoUrl;
    @OneToMany
    @JoinColumn(name = "cue_id")
    private List<Cue> cueList;

//    private List<SyncPoint> syncPoints;
}
