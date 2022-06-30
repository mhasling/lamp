package com.mh.lamp.cue;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "cue")
@Data
public class Cue {
    @Id
    private Double number;
    private Instant receivedTime;
    // In seconds
    private Long videoTime;

    public Cue() {
    }

    public Cue(Double number, Instant time) {
        this.number = number;
        this.receivedTime = time;
    }

}
