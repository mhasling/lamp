package com.mh.lamp;

import com.mh.lamp.cue.Cue;
import com.mh.lamp.cue.CueRepository;
import com.mh.lamp.recording.Recording;
import com.mh.lamp.recording.RecordingContentStore;
import com.mh.lamp.recording.RecordingRepository;
import com.mh.lamp.recording.StreamingService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class LampController {

    private CueRepository cueRepository;
    private RecordingRepository recordingRepository;
    private RecordingContentStore recordingContentStore;
    private StreamingService service;

    private Integer selectedRecordingId;

    public LampController(CueRepository cueRepository, RecordingRepository recordingRepository,
                          RecordingContentStore recordingContentStore, StreamingService service) {
        this.cueRepository = cueRepository;
        this.recordingRepository = recordingRepository;
        this.recordingContentStore = recordingContentStore;
        this.service = service;
    }

    @GetMapping("/testMessage")
    public void testMessage() {
        UnicastSendingMessageHandler handler =
                new UnicastSendingMessageHandler("localhost", 9000);
        String payload = "Cue 1 31";
        handler.handleMessage(MessageBuilder.withPayload(payload).build());
    }

    @GetMapping("/getCues")
    public List<Cue> getCues(@RequestParam(name = "recordingId") Integer recordingId) {
        if (recordingRepository.existsById(recordingId)) {
            Recording recording = recordingRepository.getById(recordingId);
            if (recording.getCueList() != null && !recording.getCueList().isEmpty()) {
//                Instant syncPoint = recording.getCueList().get(0).getTime();
//                for (Cue cue : recording.getCueList()) {
//                    cue.setVideoTime(Duration.between(syncPoint, cue.getTime()).getSeconds());
//                }
                return recording.getCueList();
            }
        }
        return new ArrayList<>();
    }

    @GetMapping("createCue")
    public void createCue(@RequestParam(name = "number") Double cueNumber,
                          @RequestParam(name = "recordingId", required = false) Integer recordingId)
    {
        Cue cue = new Cue(cueNumber, Instant.now());
        cueRepository.save(cue);

        Recording recording = recordingRepository.getById(recordingId != null ? recordingId : selectedRecordingId);
        recording.getCueList().add(cue);
        recordingRepository.save(recording);
    }

    @PostMapping("createCue")
    public void createCuePost(@RequestBody Cue cue,
                              @RequestParam(name = "recordingId", required = false) Integer recordingId)

    {
        cueRepository.save(cue);
        Recording recording = recordingRepository.getById(recordingId != null ? recordingId : selectedRecordingId);
        recording.getCueList().add(cue);
        recordingRepository.save(recording);
    }

    @GetMapping("deleteCue")
    public boolean deleteCue(@RequestParam(name = "number") Double cueNumber,
                          @RequestParam(name = "recordingId", required = false) Integer recordingId)
    {
        Recording recording = recordingRepository.getById(recordingId != null ? recordingId : selectedRecordingId);
        recording.getCueList().removeIf(c -> c.getNumber().equals(cueNumber));
        recordingRepository.save(recording);
        return true;
    }

    @GetMapping("getCueTime")
    public void getCueTime(@RequestParam(name = "cueNumber") Double cueNumber, @RequestParam(name = "recordingId") Integer recordingId)
    {
        cueRepository.getById(cueNumber);
        // Get recording to get the sync points
        recordingRepository.getById(recordingId);
    }

    @PostMapping("createRecording")
    public List<Recording> createRecording(@RequestBody Recording recording)
    {
        recordingRepository.save(recording);
        return recordingRepository.findAll();
    }

    @GetMapping("startRecording")
    public boolean startRecording(@RequestParam(name = "recordingId") Integer recordingId)
    {
        if (recordingRepository.existsById(recordingId)) {
            selectedRecordingId = recordingId;
            return true;
        }
        return false;
    }

    @PostMapping("addVideo")
    public ResponseEntity<?> addVideo(@RequestParam Integer recordingId,
                                   @RequestParam(name = "file") MultipartFile file) throws IOException {
        Optional<Recording> recording = recordingRepository.findById(recordingId);
        if (recording.isPresent()) {
            service.saveVideo(file);
            recording.get().setVideoTitle(file.getOriginalFilename());
            recordingRepository.save(recording.get());
            return new ResponseEntity<Object>(HttpStatus.OK);
        }
        return null;
    }

    @GetMapping("getRecordings")
    public List<Recording> getRecordings()
    {
        return recordingRepository.findAll();
    }

    @GetMapping(value = "getVideo/{recordingId}", produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable(name = "recordingId", required = false) Integer recordingId,
                                   @RequestHeader("Range") String range) {
        Optional<Recording> recording = recordingRepository.findById(recordingId);
        if (recording.isPresent()) {
            return service.getVideo(recording.get().getVideoTitle());
        }
        return null;
    }



}
