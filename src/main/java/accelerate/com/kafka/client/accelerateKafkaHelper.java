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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;

/** Lite blandade funktioner som kan vara bra under BOSen
 * @author Mikael
 * @version 0.1
 * @since 0.1
*/
public class accelerateKafkaHelper {

	// Sätt denna till din egen grupp. 
	static String _myGroupID = "mikaels-kafka-group";
	

	/**
	* Sätt alla properties med denna metod. 
	*
	* @param  	configFile 	Din konfigfil
	* @return  	Properties	Properties från den lokala propertyfilen	
	*/		  
	public static Properties setProperties() throws Exception {
		  
		// Load properties from a local configuration file
		Properties props = loadConfig("java.config");
		
		// Add additional properties.
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonDeserializer");
		props.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, DataRecordBOS.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, _myGroupID);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		
		return props;
	}

	/**
	* Ladda upp properties med denna hjälpklass
	*
	* @param  	configFile 	Din konfigfil
	* @return  	Properties	Properties från den lokala propertyfilen	
	*/		  
	public static Properties loadConfig(String configFile) throws IOException {
		if (!Files.exists(Paths.get(configFile))) {
			throw new IOException(configFile + " not found.");
		}
		final Properties cfg = new Properties();
		try (InputStream inputStream = new FileInputStream(configFile)) {
			cfg.load(inputStream);
		}
		return cfg;
	}
	  
	  
	/**
	* Hjälpmetod för att ändra OFFSET i din läsning. Används med fördel
	* från konsumenten...
	*
	* @param  	myTopic 		DIn TOPIC att flytta offseten i
	* @param  	consumer 		Din consumer objektet
	* @param  	targetOffset 	Offset som ett nummer
	* @return  	Properties	Properties från den lokala propertyfilen	
	*/		  
	public static void changeOffset(String myTopic, Consumer<String, DataRecordBOS> consumer, long targetOffset) 
	{
		Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
		OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(targetOffset);
		TopicPartition topicPartition_0 = new TopicPartition(myTopic, 0);
		offsets.put(topicPartition_0, offsetAndMetadata);
	    consumer.commitSync(offsets);
	  }
	 

}
