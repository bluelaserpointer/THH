package action;

import thh.THH;

public class Action{
	public final ActionSource SOURCE;
	
	public String name;
	public final int initialFrame;
	public final int[]
		frame,
		meaning;
	public final double[]
		x,y;
	
	public Action(ActionSource source) {
		this.SOURCE = source;
		initialFrame = THH.getNowFrame();
		name = ActionInfo.name;
		frame = ActionInfo.getFrameArray();
		meaning = ActionInfo.getMeaningArray();
		x = ActionInfo.getXArray();
		y = ActionInfo.getYArray();
	}
	
	public final int getNowPos() {
		final int passedFrame = THH.getPassedFrame(initialFrame);
		for(int i = 0;i < frame.length;i++) {
			if(passedFrame > frame[i])
				return i;
		}
		return THH.NONE;
	}
}
