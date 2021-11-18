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
package accelerate.com.kafka.image;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

/** Hjälpklass för att jobba med bilder
 * @author Mikael
 * @version 0.1
 * @since 0.1
*/
public class ImageHelper {

	
	public static void main(String[] args) throws IOException {
	}

	
	/**
	* Kan används för att läsa upp en fil som "base64" 
	*
	* @param  	filePath 	Speca topicen du vill läsa ifrån
	* 						offset i den topic du läser i....
	* @return  	String		En sträng, base64
	*/
	public String getBase64StringFromImage(String filePath) throws IOException {
			
		 // Get file as bytes
		 byte[] bytes = Files.readAllBytes(Paths.get(filePath));
			
		 // Encode into base64...
		 String encodedString = Base64.getEncoder().encodeToString(bytes);	
			
		 // Return...
		 return encodedString;
	}
	 
	/**
	* Skapa en BILDFIL från en fil med BASE64 
	*
	* @param  	filePathIn 		Infil
	* @param  	filePathOut 	Utfil
	* @return  	Void		
	*/
	public void createJpegFromBase64File(String filePathIn, String filePathOut ) throws IOException {
			
		 BufferedReader reader = new BufferedReader(new FileReader(filePathIn));
		 StringBuilder builder = new StringBuilder();
		 final char[] buffer = new char[8192];
		 
		 int n;
		 
		 while ((n = reader.read(buffer)) > 0) {
			 builder.append(buffer, 0, n);
		  }
		  
		 // Convert into 
		 byte[] decodedBytes = Base64.getDecoder().decode(builder.toString());
				
		 // Write...
		 try (FileOutputStream fos = new FileOutputStream(filePathOut)) {
			 fos.write(decodedBytes);
			 }
		  
	}	 
	 
	 
	/**
	* Skriv ner en BASE64 sträng till fil på din lokala dator. 
	*
	* @param  	encodedString 	Strängen som ska skrivas
	* @param  	filePath 		Utfil
	* @return  	Void		
	*/
	public void writeBase64StringIntoFile(String encodedString, String filePath) throws IOException {
			
		 // Convert into 
		 byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
			
		 // Write...
		 try (FileOutputStream fos = new FileOutputStream(filePath)) {
			 fos.write(decodedBytes);
		 }
		 
	 }
	 

	/**
	* Skriv ner en sträng till fil på din lokala dator. 
	*
	* @param  	raw 		Strängen som ska skrivas
	* @param  	filePath 	Utfil
	* @return  	Void		
	*/
	public void writeRawStringIntoFile(String raw, String filePath) throws IOException {
			
		 BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		    writer.write(raw);
		    
		    writer.close();
	 
	 }

	/**
	* Appenda sträng till en FIL. Finns den så appendas, annars skapas den
	* och skriver din sträng i den. 
	*
	* @param  	raw 		Strängen som ska appendas
	* @param  	filePath 	Utfil
	* @return  	Void		
	*/
	 public void writeRawStringAppendFile(String raw, String filePath) throws IOException {
			
		 BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true));
		    writer.write(raw);
		    
		    writer.close();
	 
	 }

	
	

	
	
	
	
}
