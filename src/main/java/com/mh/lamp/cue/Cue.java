package com.mh.lamp.cue;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "cue")
@Data
public class Cue {
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    private Integer id;
    private Double number;
    private Instant time;

    public Cue() {
    }

    public Cue(Double number, Instant time) {
        this.number = number;
        this.time = time;
    }

}
