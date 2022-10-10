package com.jalaj.jms.hm.clinicals;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.jalaj.jms.hm.model.Patient;

public class ClinicalsApp {

	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		Queue requestQueue = (Queue) context.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) context.lookup("queue/replyQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			
			ObjectMessage objectMessage = jmsContext.createObjectMessage();
			
			Patient patient = new Patient();
			patient.setId(123);
			patient.setName("Bob");
			patient.setInsuranceProvider("Blue Cross Blue Shield");
			patient.setCopay(100d);
			patient.setAmountToBePaid(200d);
			
			objectMessage.setObject(patient);
			
			producer.send(requestQueue, objectMessage);
			
			
//			for(int i=1; i<=10; i++) {
//				producer.send(requestQueue, objectMessage);
//			}
			
			
			
			JMSConsumer consumer = jmsContext.createConsumer(replyQueue);
			MapMessage replyMessage = (MapMessage) consumer.receive(30000);
			System.out.println("Eligibility = "+replyMessage.getBoolean("eligible"));
			
		}

	}

}
