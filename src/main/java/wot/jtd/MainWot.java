package wot.jtd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MainWot {

	public static void main(String[] args) throws IOException {
		File file = new File("/Users/cimmino/Desktop/tds");
		File[] tds = file.listFiles();
		int counter=0;
		JsonArray interactions = new JsonArray();
		for(File td:tds) {
			String parsedTD = readFile(td);
			try {
			JsonObject tdJson = JTD.parseJson(parsedTD);
			if(tdJson.has("interactions")) {
				counter++;
				interactions.addAll(tdJson.getAsJsonArray("interactions"));
			}else if(tdJson.has("interaction")) {
				counter++;
				interactions.addAll(tdJson.getAsJsonArray("interaction"));
			}else {
				System.out.println("WARNING: "+td.getName());
			}
			}catch(Exception e) {
				System.out.println("ERROR WITH: "+td.getName());
			}
		}
		System.out.println(counter+" out of "+tds.length);
		FileWriter fw = new FileWriter("/Users/cimmino/Desktop/td-iot-frame.csv");

		for(int index=0; index < interactions.size(); index++) {
			JsonObject interaction = interactions.get(index).getAsJsonObject();
			if(interaction.has("@type")) {
				JsonArray types  = interaction.getAsJsonArray("@type");
				for(int count=0; count<types.size();count++) {
					String type = types.get(count).getAsString();
					JsonObject copyInteraction = interaction.deepCopy();
					copyInteraction.remove("@type");
					if(type.contains("Property")) {
						fw.write("properties_______"+copyInteraction+"\n");
					}if(type.contains("Action")) {
						fw.write("actions_______"+copyInteraction+"\n");
					}if(type.contains("Event")) {
						fw.write("events_______"+copyInteraction+"\n");
					}
				}
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
