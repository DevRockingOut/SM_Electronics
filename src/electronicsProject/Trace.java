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

public class Trace {
	
	static List<String> callers = new ArrayList<String>();
	
	public static void write(String output, String filename, String caller) {
		
		if(!callers.contains(caller)) {
			callers.add(caller);
			
			// delete trace once per class caller
			File file = new File(filename);
			try {
				boolean result = Files.deleteIfExists(file.toPath());
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
