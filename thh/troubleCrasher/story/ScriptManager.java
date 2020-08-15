package troubleCrasher.story;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.Game;
import troubleCrasher.engine.TCGame;
import troubleCrasher.person.*;

public class ScriptManager {

	// STARTLINE, OPTIONS, OPTION, ADD, DELETE, IF, LIST, END
	// public int type;
	
    public BufferedReader buffReader; 
    private String pathToScripts = "troubleCrasher/story/scripts/";
    private String currentFile = "";
	private StoryMechanicManager storyMechanicManager = TCGame.storyMechanicManager;
	
	private int rounds = 0;
	
    public List<String> currentOptions = new ArrayList();
    public List<String> disabledOptions = new ArrayList();
    
	public ScriptManager() {}
	
	public ScriptManager(String fileName){
		this.currentFile = fileName;
		this.parseFile(fileName);
	}
	
	private void setScriptManager(String fileName)
	{
		this.currentFile = fileName;
		parseFile(fileName);
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
	public void parseFile(String fileName) {
		String filePath = parsePath(fileName);
		
	    FileInputStream fin;
		try {
			fin = new FileInputStream(filePath);
		    InputStreamReader reader = new InputStreamReader(fin);
		    buffReader = new BufferedReader(reader);
		    String currLine = "";
		    try {
				currLine = buffReader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			parseLine(currLine);
		} catch (FileNotFoundException e) {
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
	public void parseLine(String currLine){
		System.out.println(currLine);
		if(currLine.charAt(0) == '#')
		{
			try {
				parseFunc(currLine);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			//	String[] parsedLine = currLine.split("：");
			System.out.println("In else");
			nextLine(currLine);
		}
	}
	
	public void nextLine(String currText)
	{
		PersonEnum person = findPersonWithName(parseColonHead(currText));
		TCGame.gamePageSwitcher.setDialogue(parseColonContent(currText), person);
	}

	public PersonEnum findPersonWithName(String name)
	{
		System.out.println("In findPersonWithName");
		for(PersonEnum person: PersonEnum.values())
		{
			System.out.println(person.name);
			if(person.name.contentEquals(name))
			{
				System.out.println("Match");
				return person;
			}
		}
		return null;
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
		System.out.println("In parseFunc");
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
			case "ATK":
				parseAtk(funcLine);
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
		TCGame.resource.getAll();
	}

	private void parseJump(String funcLine) throws IOException, InterruptedException
	{
		String[] parsedLine = funcLine.split("#");
		
		System.out.println("Jumping to " + parsedLine[2]);
		
		setScriptManager(parsedLine[2]);
				
		// Scripts after optionGroup
		parseLine(buffReader.readLine());
	}
	
	private boolean parseDelete(String funcLine)
	{
		String[] parsedLine = funcLine.split("#");
		
		System.out.println("Deleting item");
		
		String delresource = parsedLine[3];
		int delQuantity = Integer.parseInt(parsedLine[4]);
		
		switch(delresource)
		{
			case "STAMINA":
				return TCGame.resource.delStamina(delQuantity);
			case "MONEY":
				return TCGame.resource.delMoney(delQuantity);
			case "HP":
				return TCGame.resource.delHp(delQuantity);
			case "ENEMYHP":
				return TCGame.resource.delEnemeyHp(delQuantity);
			default:
				return false;
		}
	}
	
	private void parseDc(String funcLine) throws IOException, InterruptedException
	{
		String[] parsedLine = funcLine.split("#");
		
		int dc = Integer.parseInt(parsedLine[2]);
		
		System.out.println("Parsing DC");
		
		if(dc > 6)
		{
			chooseOption(1);
		}else {
			chooseOption(2);
		}
	}
	
	private int rand(int min, int max)
	{
		return (int)(Math.random()*(max-min+1)+min);
	}
	
	private void parseAtk(String funcLine)
	{
		String[] parsedLine = funcLine.split("#");
		
		int d = Integer.parseInt(parsedLine[3]);
		int ac = Integer.parseInt(parsedLine[5]);
		int type;
		switch(parsedLine[6])
		{
			case "警长":
				type = 1;
				break;
			case "年轻人":
				type = 2;
				break;
			case "敌人":
				type = 3;
				break;
			default:
				type = 3;
				break;
		}
				
//		#ATK#D#20#AC#10#警长
				
		// true hit, false miss
		// 1: critical, 2: normal, 3: miss
		rounds++;
		
		boolean hit = rand(1, 20) > ac;
		int textIdx;
		int randIdx;
		boolean enemyDeath = false;
		boolean playerDeath = false;
		if(hit)
		{
			int damage = parseDmg(d);
			// 1, 2, 3
			textIdx = parseDmgTextIdx(damage, d);
			
			// 1: Player attacks, 2: YoungMan attacks, 3: Enemy attacks
			if(type == 1)
			{
				if(rounds > 1)
					randIdx = rand(2,4);
				else
					randIdx = 1;
				
				enemyDeath = (TCGame.resource.delEnemeyHp(damage) == false);
			}else if(type == 2) {
				randIdx = 1;
				
				enemyDeath = (TCGame.resource.delEnemeyHp(damage) == false);
			}else {
				randIdx = 1;
				TCGame.resource.addBoxWithName("伤口");
			}
		}else {
			textIdx = 3;
			if(type == 1)
			{
				if(rounds > 1)
					randIdx = rand(2,5);
				else
					randIdx = 1;
				
			}else if(type == 2)
			{
				randIdx = 1;
			}else {
				randIdx = 1;
			}
		}
		
		
//		4-1-1-1 234
//			2-1 234
//			3-1 2345
//			4-12
//			
//		4-2-1-1
//			2-1
//			3-1
//			
//		4-3-1-1
//			2-1
//			3-1

		System.out.println(currentFile + "-" + textIdx + "-" + randIdx);
		System.out.println(enemyDeath);
		if(enemyDeath)
			setScriptManager(4 + "-" + 1 + "-" + 4 + "-" + rand(1, 2));
		else
			setScriptManager(currentFile + "-" + textIdx + "-" + randIdx);
	}
		
	private int parseDmg(int dmg)
	{
		return rand(1, dmg);
	}
	
	private int parseDmgTextIdx(int dmg, int max)
	{
		if(dmg >= max/2)
		{
			return 1;
		}else {
			return 2;
		}
	}
	
	private void parseBg(String funcLine)
	{
		String[] parsedLine = funcLine.split("#");
				
		int bg = Integer.parseInt(parsedLine[2]);
		
		System.out.println("Parsing BG");
		
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
		System.out.println("In parseIf fulFill: " + funcLine);
		
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
			if(TCGame.resource.hasBoxWithName(itemName))
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
//		String[] parsedLine = funcLine.split("#");
//		Scanner scan = new Scanner(System.in);  //创建Scanner扫描器来封装System类的in输入流
//        int fulfill = scan.nextInt();
		System.out.println("TODO: Disabled");
//		return fulfill(parsedLine);
		return false;
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
		
		System.out.println("In parseOptions");
		while(!(currLine = buffReader.readLine()).contains("#ENDOPTIONS"))
		{
			System.out.println(currLine);
			if(currLine.charAt(0) == '#')
			{
				if(currLine.contains("#OPTION"))
				{
					if(!disabled)
					{
						currentOptions.add(currLine);
						System.out.println("Options added");
					}else {
						disabledOptions.add(currLine);
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
        System.out.println("Before if");

		if(currentOptions.size() > 0)
		{
	        System.out.println("Before generateOptions");
	        	        
	        List<String> sendOptions = new ArrayList();
	        
 	        for(String currOption: currentOptions)
   	        {
   	        	String filePath = parsePath(currentFile + "-" +  currOption.substring(7));
   				
   			    FileInputStream fin = new FileInputStream(filePath);
   			    InputStreamReader reader = new InputStreamReader(fin);
   			    buffReader = new BufferedReader(reader);
   			    String tmp = "";
   			    tmp = buffReader.readLine();
   			    tmp = buffReader.readLine();
   			    sendOptions.add(parseColonContent(tmp));
   	        }
	        
			TCGame.gamePageSwitcher.generateOptions(sendOptions);
			
	        // TODO: Needs to make available and unavailable options different
		}
	}

	private String parseColonContent(String currLine)
	{
		if(currLine.contains(":"))
		{
			return currLine.split(":")[1];	
		}else {
			return currLine.split("：")[1];
		}
		
	}
	
	private String parseColonHead(String currLine)
	{
		if(currLine.contains(":"))
		{
			return currLine.split(":")[0];	
		}else {
			return currLine.split("：")[0];
		}
		
	}
	
	/**
	 * Chooses option
	 * @param index
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void chooseOption(int index)
	{
		System.out.println("Choosing option " + index);
		
		setScriptManager(currentFile + "-" + index);
		
		// Scripts after optionGroup
		try {
			parseLine(buffReader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private void parseList(String funcLine)
	{
		String[] parsedLine = funcLine.split("#");
		
		storyMechanicManager.addVisitor(parsedLine[3], Integer.parseInt(parsedLine[5]), parsedLine[6]);
	}

	private void parseAdd(String funcLine)
	{
		//  TCGame.resource.
		//	return false;
	}
}
