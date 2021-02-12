package wot.jtd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import wot.jtd.model.Thing;

public class Main {

	public static void main(String[] args) throws IOException {
		File file = new File("./src/test/resources/linksmart/tds.json");
		String directoryTDs = readFile(file);
		JsonObject directoryJson = JTD.parseJson(directoryTDs);
		JsonArray tds = directoryJson.get("items").getAsJsonArray();
		FileWriter fw = new FileWriter("/Users/cimmino/Desktop/td-frame.csv");
		 
		
		for(int index=0; index < tds.size(); index++) {
			JsonObject td = tds.get(index).getAsJsonObject();
			try {
				
				if(td.has("properties")) {
					JsonObject elements = td.getAsJsonObject("properties");
					elements.keySet().forEach(key -> {
						try {
							fw.write("properties_______"+elements.get(key)+"\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
					
				}if(td.has("actions")) {
					JsonObject elements = td.getAsJsonObject("actions");
					elements.keySet().forEach(key -> {
						try {
							fw.write("actions_______"+elements.get(key)+"\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
					
				}if(td.has("events")) {
					JsonObject elements = td.getAsJsonObject("events");
					elements.keySet().forEach(key -> {
						try {
							fw.write("events_______"+elements.get(key)+"\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				}
	
			}catch(Exception e) {
				e.printStackTrace();
				break;
			}
		}

		 
		fw.close();

	}
	
	
	private static String readFile(File myObj) {
		StringBuilder builder = new StringBuilder(); 
		try {
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        builder.append(data);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return builder.toString();
	}

}
