package com.mh.lamp;

import com.mh.lamp.cue.Cue;
import com.mh.lamp.cue.CueRepository;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static java.time.LocalTime.now;

@RestController
public class LampController {

    private CueRepository cueRepository;

    public LampController(CueRepository cueRepository) {
        this.cueRepository = cueRepository;
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
    public void createCue()
    {
        Cue cue = new Cue((double) cueRepository.count(), Instant.now());
        cueRepository.save(cue);
    }
}
