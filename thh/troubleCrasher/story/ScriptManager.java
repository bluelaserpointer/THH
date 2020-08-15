package troubleCrasher.story;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.Game;
import troubleCrasher.engine.TCGame;
import troubleCrasher.resource.Resource;

// Parsing scripts
public class ScriptManager {

	// STARTLINE, OPTIONS, OPTION, ADD, DELETE, IF, LIST, END
	// public int type;
	
    public BufferedReader buffReader; 
    private String pathToScripts = "troubleCrasher/story/scripts/";
    private String currentFile = "";
    private Resource resource = TCGame.resource;
	private StoryMechanicManager storyMechanicManager = TCGame.storyMechanicManager;

    public List<String> currentOptions = new ArrayList();
    public List<String> disabledOptions = new ArrayList();

    
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
	    currLine = buffReader.readLine();
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
	
	/**
	 * Main method for looping through the lines
	 * @param line Current parsing line
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void parseLine(String currLine) throws IOException, InterruptedException {
		
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
			case "BG":
				parseBg(funcLine);
				break;
			case "IF":
				parseIf(funcLine);
				break;
			case "DELETE":
				parseDelete(funcLine);
				break;
			case "JUMP":
				parseJump(funcLine);
				break;
			case "DC":
				parseDc(funcLine);
				break;
			case "AC":
				parseAc(funcLine);
				break;
				
			// TODO: Haven't started yet
			case "LIST":
				parseList(funcLine);
				break;
			case "ADD":
				parseAdd(funcLine);
				break;
			case "STARTLINE":
			case "ENDLINE":
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
	
	
	private void parseJump(String funcLine) throws IOException, InterruptedException
	{
		String[] parsedLine = funcLine.split("#");
		
		System.out.println("Jumping to " + parsedLine[2]);
		
		setScriptManager(new ScriptManager(parsedLine[2]));
				
		// Scripts after optionGroup
		parseLine(buffReader.readLine());
	}
	
	private boolean parseDelete(String funcLine)
	{
		String[] parsedLine = funcLine.split("#");
		
		String delResource = parsedLine[3];
		int delQuantity = Integer.parseInt(parsedLine[4]);
		
		switch(delResource)
		{
			case "STAMINA":
				return resource.delStamina(delQuantity);
			case "MONEY":
				return resource.delMoney(delQuantity);
			case "HP":
				return resource.delHp(delQuantity);
			case "ENEMYHP":
				return resource.delEnemeyHp(delQuantity);
			default:
				return false;
		}
	}
	
	private void parseDc(String funcLine) throws IOException, InterruptedException
	{
		String[] parsedLine = funcLine.split("#");
		
		int dc = Integer.parseInt(parsedLine[2]);
		
		if(dc > 6)
		{
			chooseOption(1);
		}else {
			chooseOption(2);
		}
	}
	
	private void parseAc(String funcLine)
	{
		String[] parsedLine = funcLine.split("#");
		
		int dc = Integer.parseInt(parsedLine[2]);
		
		resource.setEnemyAc(dc);
	}
	
	private void parseBg(String funcLine)
	{
		String[] parsedLine = funcLine.split("#");
		
		int bg = Integer.parseInt(parsedLine[2]);
		
		// TODO: Needs to change background here.
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
		if(!fulfill(parsedLine))
			skipIf(Integer.parseInt(parsedLine[2]));
	}
	
	/**
	 * Checks if the statement is fulfilled
	 * @param funcLine
	 * @return
	 */
	private boolean fulfill(String[] funcLine) {
		String itemName = funcLine[5];
		if(funcLine[3] == "HAS" && funcLine[4] == "BOX")
		{
			if(resource.hasBoxWithName(itemName))
			{
				return true;
			}
		}else {
			System.out.println("HAS SOMETHING ELSE");
		}
		return false;
	}
	
	/** Checks if the statement is fulfilled
	 * @param funcLine
	 * @return
	 */
	private boolean mockFulfill(int mock) {
		return mock == 1;
	}
	
	private boolean parseDisable(String funcLine) throws NumberFormatException, IOException {
		String[] parsedLine = funcLine.split("#");
		Scanner scan = new Scanner(System.in);  //创建Scanner扫描器来封装System类的in输入流
        int fulfill = scan.nextInt();
		return fulfill(parsedLine);
	}
	
	/**
	 * Skips the statements within if
	 * @throws IOException
	 */
	private void skipIf(int index) throws IOException
	{
		String currLine = "";
		String matchIf = "#ENDIF#" + index;
		while(!(currLine = buffReader.readLine()).contains(matchIf)){}
	}

	private void optionsInit()
	{
		this.currentOptions.clear();
		this.disabledOptions.clear();
	}
	
	/**
	 * Parses option groups, redirects to another file
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private void parseOptions() throws IOException, InterruptedException {
		boolean disabled = false;
		
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
					if(!disabled)
					{
						currentOptions.add(buffReader.readLine());
					}else {
						disabledOptions.add(buffReader.readLine());
					}
				}else if(currLine.contains("#IF"))
				{
					parseIf(currLine);
				}else if(currLine.contains("#DISABLE"))
				{
					disabled = parseDisable(currLine);
				}else if(currLine.contains("#ENDDISABLE"))
				{
					disabled = false;
				}
				
				continue;
			}
			if(currLine == "#END")
			{
				continue;
			}	
		}
		
		// Pauses for player to send signal, indexes must be inside the existing options
		if(currentOptions.size() > 0)
		{
			Scanner scan = new Scanner(System.in);  //创建Scanner扫描器来封装System类的in输入流
	        int optionIndex = scan.nextInt();
	        chooseOption(optionIndex);
	        // TODO: Needs to make available and unavailable options different
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
		
		setScriptManager(new ScriptManager(currentFile + "-" + index));
		
		// Scripts after optionGroup
		parseLine(buffReader.readLine());
	}
	
	public void setScriptManager(ScriptManager scriptManager)
	{
		TCGame.setScriptManager(scriptManager);
	}
	
	
	private void parseList(String funcLine)
	{
		String[] parsedLine = funcLine.split("#");
		
		storyMechanicManager.addVisitor(parsedLine[3], Integer.parseInt(parsedLine[5]), parsedLine[6]);
	}

	private void parseAdd(String funcLine)
	{
		//	return false;
	}
}
