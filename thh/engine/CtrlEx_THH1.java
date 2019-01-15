package engine;

import static java.awt.event.KeyEvent.*;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.BitSet;

import stage.ControlExpansion;
import thh.Chara;
import thh.THH;

public class CtrlEx_THH1 extends ControlExpansion{
	private final Engine_THH1 engine;
	public CtrlEx_THH1(Engine_THH1 engine) {
		this.engine = engine;
	}
	
	//mouse
	@Override
	public void mousePressed(MouseEvent e) {
		if(!THH.isNoStopEvent())
			return;
		changeCommndBool(e.getButton(),true);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		changeCommndBool(e.getButton(),false);
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}
	
	//key
	final long SPELL_TAP_DUR = 200L;
	
	private static final int COMMAND_AMOUNT = 6;
	final int commandKeys[] = {VK_A,VK_W,VK_D,VK_S,MouseEvent.BUTTON1,NONE,VK_SHIFT};
	static final int LEFT = 0,UP = 1,RIGHT = 2,DOWN = 3,SHOT = 4,SPELL = 5,LEAP = 6;
	final String commandNames[] = {"MOVE_LEFT","MOVE_UP","MOVE_RIGHT","MOVE_DOWN","SHOT","SPELL","LEAP"};
	private final BitSet commandBools = new BitSet(COMMAND_AMOUNT);
	private long key_1_time,key_2_time,key_3_time,key_4_time;
	int spellUser;
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(!THH.isNoStopEvent())
			return;
		final int KEY_CODE = e.getKeyCode();
		changeCommndBool(KEY_CODE,true);
		final Chara[] battleCharaClass = engine.getUserChara();
		switch(KEY_CODE){
		case VK_1:
			commandBools.set(SPELL);
			spellUser = 0;
			if(battleCharaClass.length >= 1 && !THH.isExpired_time(key_1_time,SPELL_TAP_DUR))
				battleCharaClass[0].spellOrder = true;
			key_1_time = THH.getNowTime();
			break;
		case VK_2:
			commandBools.set(SPELL);
			spellUser = 1;
			if(battleCharaClass.length >= 2 && !THH.isExpired_time(key_2_time,SPELL_TAP_DUR))
				battleCharaClass[1].spellOrder = true;
			key_2_time = THH.getNowTime();
			break;
		case VK_3:
			commandBools.set(SPELL);
			spellUser = 2;
			if(battleCharaClass.length >= 3 && !THH.isExpired_time(key_3_time,SPELL_TAP_DUR))
				battleCharaClass[2].spellOrder = true;
			key_3_time = THH.getNowTime();
			break;
		case VK_4:
			commandBools.set(SPELL);
			spellUser = 3;
			if(battleCharaClass.length >= 4 && !THH.isExpired_time(key_4_time,SPELL_TAP_DUR))
				battleCharaClass[3].spellOrder = true;
			key_4_time = THH.getNowTime();
			break;
		case VK_F5:
			Engine_THH1.editMode = false;
		case VK_F6:
			if(THH.isFreezeScreen())
				Engine_THH1.editMode = !Engine_THH1.editMode;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		changeCommndBool(e.getKeyCode(),false);
	}
	
	//control
	@Override
	public void reset() {
		commandBools.clear();
		key_1_time = key_2_time = key_3_time = key_4_time = NONE;
	}
	public boolean getCommandBool(int commandID) {
		return commandBools.get(commandID);
	}
	public boolean pullCommandBool(int commandID) {
		if(commandBools.get(commandID)) {
			commandBools.clear(commandID);
			return true;
		}else
			return false;
	}
	private final void changeCommndBool(int code,boolean onoff) {
		for(int i = 0;i < commandKeys.length;i++) {
			if(commandKeys[i] == code) {
				commandBools.set(i,onoff);
				break;
			}
		}
	}
}
