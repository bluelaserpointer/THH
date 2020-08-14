package troubleCrasher.story;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

// Parsing scripts
public class ScriptManager {

	// STARTLINE, OPTIONS, OPTION, ADD, DELETE, IF, LIST, END
	// public int type;
	
    public BufferedReader buffReader; 
	
	public ScriptManager() {}
		
	/**
	 * Basic method for parsing a single file
	 * @param file Current parsing file
	 * @throws IOException 
	 */
	public void parseFile(String filePath) throws IOException {
	    FileInputStream fin = new FileInputStream(filePath);
	    InputStreamReader reader = new InputStreamReader(fin);
	    buffReader = new BufferedReader(reader);
	    String currLine = "";
	    while((currLine = buffReader.readLine())!=null){
	    	parseLine(currLine);
	    }
	    buffReader.close();
	}
	
	/**
	 * Main method for looping through the lines
	 * @param line Current parsing line
	 * @throws IOException 
	 */
	public void parseLine(String currLine) throws IOException {
		if(currLine.charAt(0) == '#')
		{
			parseFunc(currLine);
		}
		if((currLine = buffReader.readLine()) != null)
			parseLine(currLine);
	}
	
	
	/**
	 * Parses function
	 * @param funcLine
	 * @throws IOException
	 */
	public void parseFunc(String funcLine) throws IOException
	{		
		String[] parsedLine = funcLine.split("#");
		
		switch(parsedLine[0])
		{
			case "OPTIONS":
				parseOptions();
				break;
			case "IF":
				parseIf(funcLine);
				break;
			
			// TODO: Haven't started yet
			case "LIST":
				break;
			case "ADD":
				break;
			case "DELETE":
				break;
		}
	}
	
	
	/**
	 * Parses if statements
	 * @throws IOException 
	 */
	private void parseIf(String funcLine) throws IOException {
		String currLine = buffReader.readLine();
		if(fulfillIf(funcLine))
		{
			// If statement is fulfilled
			parseLine(currLine);
		}else {
			skipIf();
		}
	}
	

	/**
	 * Checks if the statement is fulfilled
	 * @param funcLine
	 * @return
	 */
	private boolean fulfillIf(String funcLine) {
		return false;
	}
	
	
	/**
	 * Skips the statements within if
	 * @throws IOException
	 */
	private void skipIf() throws IOException
	{
		String currLine = "";
		while((currLine = buffReader.readLine()).split("#")[0] != "ENDIF")
		{
			System.out.println(currLine);
		}
	}
	
	
	/**
	 * Parses option groups, redirects to another file
	 */
	private void parseOptions() {
		// Redirects to another file
	}

}
