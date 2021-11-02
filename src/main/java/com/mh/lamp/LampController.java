package com.mh.lamp;

import com.mh.lamp.cue.Cue;
import com.mh.lamp.cue.CueRepository;
import com.mh.lamp.recording.Recording;
import com.mh.lamp.recording.RecordingRepository;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
public class LampController {

    private CueRepository cueRepository;
    private RecordingRepository recordingRepository;

    public LampController(CueRepository cueRepository, RecordingRepository recordingRepository) {
        this.cueRepository = cueRepository;
        this.recordingRepository = recordingRepository;
    }

    @GetMapping("/test")
    public void testMessage() {
        UnicastSendingMessageHandler handler =
                new UnicastSendingMessageHandler("localhost", 9000);

        String payload = "Hello world";
        handler.handleMessage(MessageBuilder.withPayload(payload).build());
    }

    @GetMapping("/getCues")
    public List<Cue> getCues() {
        return cueRepository.findAll();
    }

    @GetMapping("createCue")
    public void createCue(Double cueNumber)
    {
        Cue cue = new Cue(cueNumber, Instant.now());
        cueRepository.save(cue);
    }

    @GetMapping("getCueTime")
    public void getCueTime(@RequestParam(name = "cueNumber") Double cueNumber, @RequestParam(name = "recordingId") Integer recordingId)
    {
        cueRepository.getById(cueNumber);
        // Get recording to get the sync points
        recordingRepository.getById(recordingId);
    }

    @GetMapping("createRecording")
    public void createRecording(Recording recording)
    {
        recordingRepository.save(recording);
    }

    @GetMapping("getRecordings")
    public List<Recording> getRecordings(String recordingName)
    {
        return recordingRepository.findAll();
    }

}
