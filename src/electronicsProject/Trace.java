package electronicsProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// class to create log files for debugging
public class Trace {
	
	// list of group of classes wanting to create a log file
	static List<String> classesGroup = new ArrayList<String>();
	
	
	// function used to create a log file
	public static void write(String output, String filename, String caller) {
		
		// delete trace once per class caller
		if(!classesGroup.contains(caller)) {
			classesGroup.add(caller);
			
			File file = new File(filename);
			try {
				Files.deleteIfExists(file.toPath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		PrintWriter writer = null;
		try {
			FileWriter fileWriter = new FileWriter(filename, true); //Set true for append mode
		    writer = new PrintWriter(fileWriter);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println(output);
		
		writer.close();
	}
}
