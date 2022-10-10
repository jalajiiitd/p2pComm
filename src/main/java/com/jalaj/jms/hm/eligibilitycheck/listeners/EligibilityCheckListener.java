package com.jalaj.jms.hm.eligibilitycheck.listeners;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.jalaj.jms.hm.model.Patient;

public class EligibilityCheckListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		ObjectMessage objectMessage = (ObjectMessage) message;
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			
			InitialContext context = new InitialContext();
			Queue replyQueue = (Queue) context.lookup("queue/replyQueue");
			MapMessage replyMessage = jmsContext.createMapMessage();
			
			Patient p = (Patient) objectMessage.getObject();
			String insuranceProvider = p.getInsuranceProvider();
			
			System.out.println("insurance provider = "+insuranceProvider);
			System.out.println("patient copay is = "+p.getCopay());
			System.out.println("amount to be paid = "+p.getAmountToBePaid());
			
			
			if(insuranceProvider.equals("Blue Cross Blue Shield") || insuranceProvider.equals("United Health")) {
				
				if(p.getCopay() < 40 && p.getAmountToBePaid() < 1000) {
					replyMessage.setBoolean("eligible", true);
				}
				else
					replyMessage.setBoolean("eligible", false);
			}
			
			System.out.println(replyMessage.getBoolean("eligible"));
			JMSProducer producer = jmsContext.createProducer();
			producer.send(replyQueue, replyMessage);
			
			
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}

}
