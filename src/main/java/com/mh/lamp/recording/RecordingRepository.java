package com.mh.lamp.recording;

import com.mh.lamp.cue.Cue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordingRepository extends JpaRepository<Recording, Integer> {
}
