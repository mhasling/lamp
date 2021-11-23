package com.mh.lamp;

import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
@EnableFilesystemStores
public class LampConfiguration {

    @Bean
    public IntegrationFlow processUniCastUdpMessage() {
        return IntegrationFlows
                .from(new UnicastReceivingChannelAdapter(9000))
                .handle("UDPServer", "handleMessage")
                .get();
    }

    @Bean
    File filesystemRoot() {
        try {
            return Files.createTempDirectory("").toFile();
        } catch (IOException ioe) {}
        return null;
    }

    @Bean
    public FileSystemResourceLoader fileSystemResourceLoader() {
        return new FileSystemResourceLoader(filesystemRoot().getAbsolutePath());
    }


}
