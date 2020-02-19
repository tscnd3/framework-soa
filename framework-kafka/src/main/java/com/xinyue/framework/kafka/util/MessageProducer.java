package com.xinyue.framework.kafka.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {
	
	private static Logger logger = LoggerFactory.getLogger(MessageProducer.class);
	
	@Autowired
	private KafkaTemplate<?, String> kafkaTemplate;
	    
	public void send(String topic,String message){
		kafkaTemplate.send(topic, message);
		logger.info("MessageProducer: send: message is: [" + message + "]");
	}

}
