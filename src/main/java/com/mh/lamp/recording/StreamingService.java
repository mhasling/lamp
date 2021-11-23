package com.mh.lamp.recording;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class StreamingService {

    private static final String FORMAT = "/Users/alexandramannix/Downloads/videos2/%s";

    public Mono<Resource> getVideo(String title) {
        return Mono.fromSupplier(() -> new FileSystemResource(String.format(FORMAT, title)));
    }
    public boolean saveVideo(MultipartFile file) {
        String title = file.getOriginalFilename();
        File f = new File(String.format(FORMAT, title));
        if (!f.exists()) {
            FileOutputStream f1 = null;
            try {
                f1 = new FileOutputStream(f);
                f1.write(file.getBytes());
                f1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}