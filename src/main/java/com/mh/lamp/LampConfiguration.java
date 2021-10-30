package com.mh.lamp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Configuration
public class LampConfiguration {

    @Bean
    public IntegrationFlow processUniCastUdpMessage() {
        return IntegrationFlows
                .from(new UnicastReceivingChannelAdapter(9000))
                .handle("UDPServer", "handleMessage")
                .get();
    }


}
