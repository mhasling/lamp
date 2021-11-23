package com.mh.lamp;

import com.mh.lamp.app.cue.AppRepository;
import com.mh.lamp.cue.Cue;
import com.mh.lamp.cue.CueRepository;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ConsoleListener
{
    private CueRepository cueRepository;
    private AppRepository appRepository;

    public ConsoleListener(CueRepository cueRepository, AppRepository appRepository) {
        this.cueRepository = cueRepository;
        this.appRepository = appRepository;
    }

    public void handleMessage(Message message)
    {
        String data = new String((byte[]) message.getPayload());
        String[] eosStrinTx = data.split(" ");
        if (eosStrinTx.length > 0 && eosStrinTx[1].equals("Cue") && Double.parseDouble(eosStrinTx[2]) == 1) {
            Cue cue = new Cue(Double.parseDouble(eosStrinTx[3]), Instant.now());
            cueRepository.save(cue);
        }
        System.out.print(data);
    }
}
