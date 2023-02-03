/**
 * Copyright 2020 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package accelerate.com.kafka.client;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.SerializationException;

import accelerate.com.kafka.image.ImageHelper;
import accelerate.com.kafka.client.accelerateKafkaHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/** Hjälpklass för att skapa en KAFKA CONSUMER
 * @author Mikael "Lucky Lenny" Lennehag med en del hjälp....
 * @version 0.1
 * @since 0.1
*/
public class accelerateKafkaConsumer {
  
	
	String _myTopic = "topic-FIXME";
	
	
	/**
	* Documentation... 
	*
	* @param  args 	Default for main method
	* @return      	Nix
	*/
	public static void main(final String[] args) throws Exception {

		// Start to receive messages
		// receiveMessagesTyped(.............
	}
  
  
	/**
	* Använd denna för att ta emot TYPADE meddelanden. Kan kompletteras med bra
	* funktionalitet som att flytta offset, skriva till disk för lagra filer osv. 
	*
	* @param  	topic 			Speca topicen du vill läsa ifrån
	* @param  	targetOffset 	Kanske behövs stöd för att läsa från en viss
	* 							offset i den topic du läser i....
	* @return  	Nope...
	*/
	public static void receiveMessagesTyped(String topic, long targetOffset) throws Exception {

		// Set properties
	    final Properties props = accelerateKafkaHelper.setProperties();
	    final Pattern offsetPattern = Pattern.compile("\\w*offset*\\w[ ]\\d+");
	    final Pattern partitionPattern = Pattern.compile("\\w*" + topic + "*\\w[-]\\d+");
	  	long currentOffset = -1;
	  	long prevOffset = -1;
	    
	    // Skapa en CONSUMER för denna BOS, dvs rätt datastruktur för våra
	  	// meddelanden som skickas fram och tillbaka
	    final Consumer<String, DataRecordBOS> consumer = new KafkaConsumer<String, DataRecordBOS>(props);
	
	    
	    // Offset - Jump back in time? Kanske en bra ide att göra här. 
	    if (targetOffset > -1 )
	    {
	    	//accelerateKafkaHelper..
	    }
	    // Skapa prenumeration med den TOPIC som du specat in i metoden.
	    consumer.subscribe(Arrays.asList(topic));
	    
	    // OK.... DAX att läsa från vår topic
	    try {
	    	while (true) {
		    	try {
		    		// Hämta data från "current" offset, dvs där du står.
		    		ConsumerRecords<String, DataRecordBOS> records = consumer.poll(1000);
		    		
	            	// Loopa igenom varje svar och konvertera till en BOS record..
		    		for (ConsumerRecord<String, DataRecordBOS> record : records) {
		    			String key = record.key();
		    			DataRecordBOS value = record.value();
		
		    			// Skriva ut i loggen?
		    			
		    			// Ner på disk
		    			
	            	}
	
	            	
		    		// Dax att avsluta?? Vill du BARA läsa ut det som fanns och avsluta så 
		    		// markera bort koden nedan... Då kommer konsumenten fortsätta vänta på 
		    		// nya meddelanden i oändlighet.
		    		currentOffset = consumer.position(new TopicPartition(topic, 0));
		    		
		    		// Slutet???
		    		if (currentOffset == prevOffset) {
		    			System.out.println("We have reached the end.....");
		    			break;
		    		}
		    		// Nix, kör vidare....
		    		else {
		    			prevOffset = currentOffset;
			    		System.out.println("Next offset position: " + consumer.position(new TopicPartition(topic, 0)));
		    		}
	
		    	}
		    	catch (SerializationException e) {
	                String text = e.getMessage();
	                
	                // Specialkod för att hantera meddelanden som INTE passar vår BOS record.
	                // Gör man inte detta så kommer konsumenten att avsluta...
	                Matcher mPart = partitionPattern.matcher(text);
	                Matcher mOff = offsetPattern.matcher(text);
	
	                mPart.find();
	                Integer partition = Integer.parseInt(mPart.group().replace(topic + "-", ""));
	                mOff.find();
	                Long offset = Long.parseLong(mOff.group().replace("offset ", ""));
	                
	                // Logga ut felet... sen fortsätter vi.
	                System.out.println(String.format("'TRASIGT MEDDELANDE' {0}, offset {1} .. kör vidare", partition, offset));
	                consumer.seek(new TopicPartition(topic, partition), offset + 1);
	                
	            }
	    	}
	    }
	   	finally {
			consumer.close();
		}
	}

  
	/**
	* Använd denna för att ta emot meddelanden av vilken typ som helst. Värt att testa för andra
	* typer av meddelanden. 
	*
	* @param  	topic 		Speca topicen du vill läsa ifrån
	* @return  	Nope...
	*/
	public static void receiveMessagesJSON(String topic) throws Exception {
	    
		// Set properties
	    final Properties props = accelerateKafkaHelper.setProperties();
	    final Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);
	    consumer.subscribe(Arrays.asList(topic));
	    
	    try {
	    	while (true) {
		    	try {
		    		ConsumerRecords<String, String> records = consumer.poll(100);
		    		for (ConsumerRecord<String, String> record : records)
		                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
		    	}
		    	catch (SerializationException e) {
	                String text = e.getMessage();
	                
	                System.out.println(text);
	              
	            }
	    	}
	    }
	   	finally {
			consumer.close();
		}
	  }  
  

}
