package com.mh.lamp.app.cue;

import com.mh.lamp.recording.Recording;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "app")
@Data
public class App {
    @Id
    @GeneratedValue
    private Integer id;
    @OneToOne
    private Recording selectedRecording;

    public App() {
    }

}
