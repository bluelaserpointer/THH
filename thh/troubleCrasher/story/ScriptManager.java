package troubleCrasher.story;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Parsing scripts
public class ScriptManager {

	// STARTLINE, OPTIONS, OPTION, ADD, DELETE, IF, LIST, END
	// public int type;
	
    public BufferedReader buffReader; 
    private String pathToScripts = "troubleCrasher/story/scripts/";
    private String currentFile = "";
    
    public List<String> currentOptions = new ArrayList<>();
	
	public ScriptManager() {}
	
	public ScriptManager(String fileName) throws IOException {
		this.currentFile = fileName;
		this.parseFile(fileName);
	}
	
	public String parsePath(String name)
	{
		return pathToScripts + name + ".txt";
	}
		
	/**
	 * Basic method for parsing a single file
	 * @param file Current parsing file
	 * @throws IOException 
	 */
	public void parseFile(String fileName) throws IOException {
		String filePath = parsePath(fileName);
		
	    FileInputStream fin = new FileInputStream(filePath);
	    InputStreamReader reader = new InputStreamReader(fin);
	    
	    buffReader = new BufferedReader(reader);
	    String currLine = "";
	    while((currLine = buffReader.readLine())!=null){
	    	try {
				parseLine(currLine);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    buffReader.close();
	}
	
	/**
	 * Main method for looping through the lines
	 * @param line Current parsing line
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void parseLine(String currLine) throws IOException, InterruptedException {
		System.out.println(currLine);
		if(currLine.charAt(0) == '#')
		{
			parseFunc(currLine);
		}
		if((currLine = buffReader.readLine()) != null)
		{
			waitLine();
			parseLine(currLine);
		}
	}
	
	/**
	 * Simply waits for user signal, such as touching screen
	 * @throws InterruptedException 
	 */
	private void waitLine() throws InterruptedException
	{
		Thread.currentThread();
		Thread.sleep(0);
	}
	
	
	/**
	 * Parses function
	 * @param funcLine
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void parseFunc(String funcLine) throws IOException, InterruptedException
	{		
		String[] parsedLine = funcLine.split("#");
		// System.out.println("In parseFunc");
		switch(parsedLine[1])
		{
			case "OPTIONS":
				parseOptions();
				break;
			case "IF":
				parseIf(funcLine);
				break;
			
			// TODO: Haven't started yet
			case "STARTLINE":
			case "ENDLINE":
				break;	
			case "LIST":
				break;
			case "ADD":
				break;
			case "DELETE":
				break;
			default:
				if(!funcLine.contains("##"))
				{
					System.out.println("!!ERROR!!: " + funcLine);
					break;
				}
				System.out.println("!!COMMENT!!: " + funcLine);
				break;
		}
	}
	
	
	/**
	 * Parses if statements
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private void parseIf(String funcLine) throws IOException, InterruptedException {
		String[] parsedLine = funcLine.split("#");
		// Mocking result (true or false)
		// System.out.println("In parseIf fulFill: " + funcLine);
		Scanner scan = new Scanner(System.in);  //创建Scanner扫描器来封装System类的in输入流
        int fulfill = scan.nextInt();
		
		if(!mockFulfillIf(fulfill))
			skipIf(Integer.parseInt(parsedLine[2]));
	}
	

	/**
	 * Checks if the statement is fulfilled
	 * @param funcLine
	 * @return
	 */
	private boolean fulfillIf(String funcLine) {
		return false;
	}
	
	/** Checks if the statement is fulfilled
	 * @param funcLine
	 * @return
	 */
	private boolean mockFulfillIf(int mock) {
		return mock == 1;
	}
	
	
	/**
	 * Skips the statements within if
	 * @throws IOException
	 */
	private void skipIf(int index) throws IOException
	{
		String currLine = "";
		String matchIf = "#ENDIF#" + index;
		// System.out.println("In skipIf");
		while(!(currLine = buffReader.readLine()).contains(matchIf))
		{
			System.out.println(currLine);
		}
	}
	
	private void optionsInit()
	{
		this.currentOptions.clear();
	}
	
	
	/**
	 * Parses option groups, redirects to another file
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private void parseOptions() throws IOException, InterruptedException {
		int index = 0;
		
		String currLine = "";
		optionsInit();
		
		// System.out.println("In parseOptions");
		while(!(currLine = buffReader.readLine()).contains("#ENDOPTIONS"))
		{
			if(currLine.charAt(0) == '#')
			{
				if(currLine.contains("#OPTION"))
				{
					// Add nextLine to string array
					currentOptions.add(buffReader.readLine());
					index++;
					continue;
				}else if(currLine.contains("#IF"))
				{
					parseIf(currLine);
					continue;
				}
			}
			if(currLine == "#END")
			{
				continue;
			}	
		}
		
		// Pauses for player to send signal, indexes must be inside the existing options
//		System.out.println("size " + currentOptions.size());
		if(currentOptions.size() > 0)
		{
			Scanner scan = new Scanner(System.in);  //创建Scanner扫描器来封装System类的in输入流
	        int optionIndex = scan.nextInt();
	        chooseOption(optionIndex);
		}
	}

	/**
	 * Chooses option
	 * @param index
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void chooseOption(int index) throws IOException, InterruptedException
	{
		System.out.println("Choosing option " + index);
		
		ScriptManager scriptManager = new ScriptManager(currentFile + "-" + index);
		
		// Scripts after optionGroup
		parseLine(buffReader.readLine());
	}
	
}
