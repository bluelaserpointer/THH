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
    public BufferedReader switchBuffReader;
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
		System.out.println(fileName);
		InputStreamReader reader = generateReader(filePath);
	    buffReader = new BufferedReader(reader);
	    String currLine = "";
		currLine = readLine(buffReader);
		parseLine(currLine);
	
	}

	/**
	 * Main method for looping through the lines
	 * @param line Current parsing line
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void parseLine(String currLine){
		System.out.println("In parseLine1");
		System.out.println(currLine);
//		boolean flag = false;
//		if(currLine == "#ENDLINE" || currLine == null)
//		{
//			return;
//		}
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
			nextLine(currLine);
//			flag = true;
		}
		
		
//		if(!flag)
//		{
//			currLine = readLine(buffReader);
//			parseLine(currLine);
//		}
		
		// System.out.println("After parseFunc");
//		System.out.println("In parseLine2");
//		System.out.println(currLine);
//		currLine = readLine(buffReader);
//		System.out.println(currLine);
//		System.out.println(this.currentFile);
//		try {
//			if((currLine).charAt(0) != '#')
//			{
////				currLine = readLine();
//				
//				nextLine(currLine);
//			}else {
//				if(isSkippableFunc(currLine))
//				{
////					currLine = readLine();
////					nextLine(currLine);	
//					try {
//						parseFunc(currLine);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	private boolean isSkippableFunc(String currText)
	{
		String[] parsedLine = currText.split("#");
		
		switch(parsedLine[1])
		{
			case "OPTIONS":
			case "BG":
			case "IF":
				return true;
			default:
				return false;
		}
	}
	
	public void nextLine(String currText)
	{
		PersonEnum person = findPersonWithName(parseColonHead(currText));
		TCGame.gamePageSwitcher.setDialogue(parseColonContent(currText), person);
	}

	public PersonEnum findPersonWithName(String name)
	{
		for(PersonEnum person: PersonEnum.values())
		{
			if(person.name.contentEquals(name))
			{
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
		System.out.println("In parseFunc " + parsedLine[1]);
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
				nextLine(readLine(buffReader));
				break;
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
		parseLine(readLine(buffReader));
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
		System.out.println("Generating rand: " + min + " " + max);
		return (int)(Math.random()*(max-min+1)+min);
	}
	
	private void parseAtk(String funcLine)
	{
		String[] parsedLine = funcLine.split("#");
		
		int d = Integer.parseInt(parsedLine[3]);
		int ac = Integer.parseInt(parsedLine[5]);
		int type;
		String person = parsedLine[6];
		switch(person)
		{
			case "警长":
				if(TCGame.resource.getCurrentItemName().equals("左轮手枪"))
				{
					type = 1;
				}else {
					type = 3;
				}
				break;
			case "年轻人":
				type = 2;
				break;
			case "敌人":
				type = 5;
				break;
			default:
				type = 3;
				break;
		}
		
		System.out.println("---------------");
		System.out.println(TCGame.resource.getCurrentItemName());
		System.out.println(type);
		type = 3;
		System.out.println("---------------");	
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
			// 1, 2
			textIdx = parseDmgTextIdx(damage, d);
			
			// 1: Player attacks, 2: YoungMan attacks, 3: Enemy attacks
			if(person.equals("警长"))
			{
				// Gun
				if(type == 1)
				{
					if(rounds > 1)
					{
						randIdx = rand(2,4);
					}
					else
					{
						randIdx = 1;
					}
				}
				else
				{
					// Beer
					randIdx = rand(1,3);
				}
				
				enemyDeath = (TCGame.resource.delEnemeyHp(damage) == false);
			}else if(type == 2) {
				randIdx = 1;
				
				enemyDeath = (TCGame.resource.delEnemeyHp(damage) == false);
			}else {
				textIdx = 1;
				randIdx = rand(1,3);
				TCGame.resource.addBoxWithName("伤口");
			}
		}else {
			textIdx = 3;
			if(person.equals("警长"))
			{
				if(type == 1)
				{
					// Gun
					if(rounds > 1)
						randIdx = rand(2,5);
					else
						randIdx = 1;
				}else {
					// Beer
					randIdx = rand(1,3);
				}
				
			}else if(type == 2)
			{
				randIdx = 1;
			}else {
				randIdx = rand(1,3);
			}
		}
		
//		4-1-1-1 234
//			2-1 234
//			3-1 2345
//			4-12
//		4-2-1-1
//			2-1
//			3-1
//		4-3-1-1
//			2-1
//			3-1

		System.out.println(currentFile + "-" + textIdx + "-" + randIdx);
		if(enemyDeath)
		{
			if(type == 1)
				setScriptManager(4 + "-" + 1 + "-" + 4 + "-" + rand(1, 2));
			else
				setScriptManager(4 + "-" + 3 + "-" + 4 + "-" + 1);
		}
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
	private void parseIf(String funcLine){
		String[] parsedLine = funcLine.split("#");
		// Mocking result (true or false)
		System.out.println("In parseIf fulFill: " + funcLine);
		
		if(!fulfill(parsedLine))
			skipIf(Integer.parseInt(parsedLine[2]));
	}
	
	private boolean parseDisable(String funcLine){
		String[] parsedLine = funcLine.split("#");
		return !fulfill(parsedLine);
	}
	
	/**
	 * Checks if the statement is fulfilled
	 * @param funcLine
	 * @return
	 */	
	private boolean fulfill(String[] funcLine) {
		
		//		#IF#HAS#BOX#123
		//		#IF#SELECT#BOX#123
		
		String itemName = funcLine[4];
		System.out.println(itemName);
		System.out.println("---------");
		for(String str: funcLine)
		{
			System.out.println(str);
		}
		System.out.println("---------");
		if(funcLine[5].equals("WEAPON"))
		{
			if(funcLine[3].equals("HAS"))
			{
				System.out.println("*****In has box");
				
				if(TCGame.resource.hasBoxWithName(itemName))
				{
					System.out.println("*****Has item");
					return true;
				}
				
			}else if(funcLine[3].equals("SELECT"))
			{
				System.out.println("*****In select box");
				
				TCGame.resource.setCurrentItemName("左轮手枪");
				System.out.println(TCGame.resource.getCurrentItemName());
				if(TCGame.resource.getCurrentItemName().equals(itemName))
				{
					System.out.println("*****Item selected");
					return true;
				}
						
			}else
			{
				System.out.println("HAS SOMETHING ELSE");
			}
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
	
	
	/**
	 * Skips the statements within if
	 * @throws IOException
	 */
	private void skipIf(int index)
	{
		String currLine = "";
		String matchIf = "#ENDIF#" + index;
		while(!(currLine = readLine(buffReader)).contains(matchIf)){}
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
	private void parseOptions()
	{
		boolean disabled = false;
		
		String currLine = "";
		optionsInit();
		
		System.out.println("In parseOptions");

		while(!(currLine = readLine(buffReader)).contains("#ENDOPTIONS"))
		{
			System.out.println("while loop");
			System.out.println(currLine);
			if(currLine.charAt(0) == '#')
			{
				if(currLine.contains("#OPTION"))
				{
					if(!disabled)
					{
						System.out.println("Enabled option added: " + currLine);
						currentOptions.add(currLine);
						System.out.println("Options added");
					}else {
						System.out.println("Disabled option added: " + currLine);
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
				}else {
					disabled = false;
				}
				
				continue;
			}
			if(currLine.equals("#END"))
			{
				continue;
			}
		}
		
		// Pauses for player to send signal, indexes must be inside the existing options
        System.out.println("Before generating option buttons");
        System.out.println(currLine);
		if(currentOptions.size() > 0 || disabledOptions.size() > 0)
		{
        	        
	        List<String> sendOptions = new ArrayList();
	        List<Boolean> optionStatus = new ArrayList();
	        
 	        for(String currOption: currentOptions)
   	        {
   	        	String filePath = parsePath(currentFile + "-" +  currOption.substring(7));
   				
   			    InputStreamReader reader = generateReader(filePath);
   			    BufferedReader tmpReader = new BufferedReader(reader);
   			    String tmp = "";
   			    tmp = readLine(tmpReader);
   			    tmp = readLine(tmpReader);
   			    sendOptions.add(parseColonContent(tmp));
   			    optionStatus.add(true);
   	        }
 	        
// 	       System.out.println("-----------"); 
 	       for(String currOption: disabledOptions)
  	        {
  	        	String filePath = parsePath(currentFile + "-" +  currOption.substring(7));
  				
  			    InputStreamReader reader = generateReader(filePath);
  			    BufferedReader tmpReader = new BufferedReader(reader);
  			    String tmp = "";
  			    tmp = readLine(tmpReader);
  			    tmp = readLine(tmpReader);
  			    sendOptions.add(parseColonContent(tmp));
  			    
//  			    System.out.println(parseColonContent(tmp));
  			    
  			    optionStatus.add(false);
  	        }
// 	       System.out.println("-----------");
	        
// 	        optionsGroup.txt
// 	        
// 	        static flag = true;
// 	        if(flag == true)
// 	        {
// 	        	旁白
// 	        }
//        	
// 	        opt1
// 	        opt2
// 	        endline
// 	        
// 	        chooseOption() {
// 	        	flag = false;
// 	        }
// 	        
// 	        useBox(Box box){
// 	        	
// 	        	use... => () {
// 	        		currItem = box;
// 	        	}
// 	        	
// 	        	setScriptManager(currFile);
// 	        }
 	        
 	     
			TCGame.gamePageSwitcher.generateOptions(sendOptions, optionStatus);
		}
	}

	public void currentItemChange()
	{
		this.setScriptManager(currentFile);
	}
	
	private InputStreamReader generateReader(String filePath)
	{
		FileInputStream fin;
		try {
			fin = new FileInputStream(filePath);
		    InputStreamReader reader = new InputStreamReader(fin);
		    return reader;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String parseColonContent(String currLine)
	{
		// System.out.println(currLine);
		if(currLine.contains(":"))
		{
			return currLine.split(":")[1];	
		} else if (currLine.contains("：")) {
			return currLine.split("：")[1];
		} 
		return "";
		
	}
	
	private String parseColonHead(String currLine)
	{
		if(currLine.contains(":"))
		{
			return currLine.split(":")[0];	
		}else if (currLine.contains("：")) {
			return currLine.split("：")[0];
		}
		return "";
	}
	
	/**
	 * Chooses option
	 * @param index
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void chooseOption(int index)
	{
		// System.out.println("Choosing option " + index);
		
		setScriptManager(currentFile + "-" + index);
		
		// Scripts after optionGroup
		parseLine(readLine(buffReader));
	}

	
	private void parseList(String funcLine)
	{
		String[] parsedLine = funcLine.split("#");
		
		storyMechanicManager.addVisitor(parsedLine[3], Integer.parseInt(parsedLine[5]), parsedLine[6]);
	}

	private void parseAdd(String funcLine)
	{
		
		nextLine(readLine(buffReader));
	}
	
	public String readLine(BufferedReader reader)
	{
		try {
			return reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
