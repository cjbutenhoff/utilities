/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wp.utils.kafka;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ImportResource;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

/**
 ** Producer uses XML configuration.
 * @author wangh
 */
@Component
@ImportResource("outbound-kafka-integration.xml")
public class KafkaProducer {

    @Autowired
    @Qualifier("inputToKafka")
    MessageChannel messageChannel;
    private Log log = LogFactory.getLog(getClass());

    public void produce(String message) throws Exception {

        messageChannel.send(new GenericMessage<>(message));

    }
}
