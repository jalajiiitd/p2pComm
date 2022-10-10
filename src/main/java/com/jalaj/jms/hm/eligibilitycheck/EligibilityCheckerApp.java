package com.jalaj.jms.hm.eligibilitycheck;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.jalaj.jms.hm.eligibilitycheck.listeners.EligibilityCheckListener;

public class EligibilityCheckerApp {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		InitialContext context = new InitialContext();
		Queue requestQueue = (Queue) context.lookup("queue/requestQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			
			JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
//			JMSConsumer consumer2 = jmsContext.createConsumer(requestQueue);
//			for(int i=1; i<=10; i+=2) {
//				System.out.println("Consumer 1: "+consumer1.receive());
//				System.out.println("Consumer 2: "+consumer2.receive());
//			}
			consumer.setMessageListener(new EligibilityCheckListener());
			Thread.sleep(10000);
		}
			
	}

}
