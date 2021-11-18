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

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.Producer;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.common.errors.TopicExistsException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;


/** Hjälpklass för att skapa en KAFKA PRODUCENT
 * @author Mikael med en del hjälp....
 * @version 0.1
 * @since 0.1
*/
public class accelerateKafkaProducer {

	

	/**
	* Documentation... 
	*
	* @param  args 	Default for main method
	* @return      	Nix
	*/
	public static void main(final String[] args) throws Exception {
		
	
	}	
	
	/**
	* Använd denna för att skicka TYPADE meddelanden till en viss TOPIC.
	*
	* @param  	topic 		Speca topicen du vill SKRIVA till
	* @param  	record 		Preappat format för BOSen. Här finns allt du behöver..eller??	 						
	* @param  	key 		Meddelandets nyckel	 						
	* @return  	Nope...
	*/
	public static void sendMessagesTyped(String topic, DataRecordBOS record, String key ) throws Exception {
  	  
		// Properties for sending to the TOPIC...
		final Properties props = accelerateKafkaHelper.loadConfig("java.config");
	    props.put(ProducerConfig.ACKS_CONFIG, "all");
	    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
	    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");
	
	    // Skapa producenten med vår BOS typ
	    Producer<String, DataRecordBOS> producer = new KafkaProducer<String, DataRecordBOS>(props);
	
	    System.out.printf("Producing record: %s\t%s%n", key, record);
	    
	    // SKICKA!!!!
	    producer.send(new ProducerRecord<String, DataRecordBOS>(topic, key, record), new Callback() {
          
	    	@Override
	    	public void onCompletion(RecordMetadata m, Exception e) {
            if (e != null) {
            	e.printStackTrace();
            	} else {
            		System.out.printf("Produced record to topic %s partition [%d] @ offset %d%n", m.topic(), m.partition(), m.offset());
            	}
	    	}
	    });

	    producer.flush();
	    producer.close();
	}


	/**
	* Använd denna för att skicka JSON meddelanden till en viss TOPIC. Typen är då sträng
	*
	* @param  	topic 		Speca topicen du vill SKRIVA till
	* @param  	msg 		Ditt meddelande som en STRÄNG	* 						
	* @return  	Nope...
	*/
	public static void sendMessagesJSON(String topic, String msg, String key) throws Exception {
	  	  
		// Properties for sending to the TOPIC...
		final Properties props = accelerateKafkaHelper.loadConfig("java.config");
	    props.put(ProducerConfig.ACKS_CONFIG, "all");
	    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
	    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");
	
	   // Skapa upp "blank" producent
	    Producer<String, String> producer = new KafkaProducer<String, String>(props);
	
	    System.out.printf("Producing record: %s\t%s%n", key, msg);
	    
	    // SKICKA!!!!
	    producer.send(new ProducerRecord<String, String>(topic, key, msg), new Callback() {
          
	    	@Override
	    	public void onCompletion(RecordMetadata m, Exception e) {
            if (e != null) {
            	e.printStackTrace();
            	} else {
            		System.out.printf("Produced record to topic %s partition [%d] @ offset %d%n", m.topic(), m.partition(), m.offset());
            	}
	    	}
	    });

	    producer.flush();
	    producer.close();
	}	
	
	
	

}
