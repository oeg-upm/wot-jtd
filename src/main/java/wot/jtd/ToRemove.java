package wot.jtd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import wot.jtd.exception.SchemaValidationException;
import wot.jtd.model.Thing;

public class ToRemove {

	public static void main(String[] args) throws IOException, SchemaValidationException {
		File file = new File("./src/test/resources/linksmart/original/tds.json");
		String directoryTDs = readFile(file);
		JsonObject directoryJson = JTD.parseJson(directoryTDs);
		JsonArray tds = directoryJson.get("items").getAsJsonArray();
		
		 
		
		for(int index=0; index < tds.size(); index++) {
			JsonObject td = tds.get(index).getAsJsonObject();
			FileWriter fw = new FileWriter("./src/test/resources/linksmart/td-"+index+".json");
			try {
				fw.write(td.toString());
			}catch(Exception e) {
				e.printStackTrace();
				break;
			}
			fw.close();
		}

		 
		

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
