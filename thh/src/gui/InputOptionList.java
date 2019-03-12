package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import core.GHQ;

public class InputOptionList extends GUIParts{
	private final TitledLabel label;
	private int nowListLength;
	private final int listViewMaxLength;
	private int hoveredPosition = GHQ.NONE;
	private String hoveredString = "";
	private final ArrayList<String> optionList = new ArrayList<String>();
	public InputOptionList(TitledLabel label,int listViewMaxLength) {
		super(label.getGroup(), null, label.x, label.y + label.h, label.w, label.h, true);
		this.label = label;
		this.listViewMaxLength = listViewMaxLength;
	}
	public InputOptionList(TitledLabel label) {
		super(label.getGroup(), null, label.x, label.y + label.h, label.w, label.h, true);
		this.label = label;
		this.listViewMaxLength = GHQ.MAX;
	}
	//init
	public void addWord(String word) {
		optionList.add(word);
	}
	public void addWord(String... words) {
		for(String word : words)
			optionList.add(word);
	}
	public void addWord(Collection<? extends String> words) {
		optionList.addAll(words);
	}
	//main role
	@Override
	public void paint() {
		if(!label.isInputMode())
			return;
		//listLength update
		final String TEXT = label.getText();
		final String matchedOptions[] = new String[optionList.size()];
		nowListLength = 0;
		for(String option : optionList) {
			if(option.startsWith(TEXT)) {
				matchedOptions[nowListLength++] = option;
				if(nowListLength == listViewMaxLength)
					break;
			}
		}
		//paint
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(Color.WHITE);
		final int H = h*nowListLength;
		G2.fillRect(x, y, w, H);
		//hovered position emphasize
		if(hoveredPosition != GHQ.NONE) {
			hoveredString = matchedOptions[hoveredPosition];
			G2.setColor(Color.GRAY);
			G2.fillRect(x, y + h*hoveredPosition, w, h);
		}
		//frame
		G2.setColor(Color.BLACK);
		G2.setStroke(GHQ.stroke3);
		G2.drawRect(x, y, w, H);
		//text
		G2.setFont(GHQ.basicFont);
		for(int i = 0;i < nowListLength;i++)
			G2.drawString(matchedOptions[i], x + 3, y + h*i + 20);
	}
	@Override
	public void clicked() {
		if(hoveredPosition != GHQ.NONE)
			label.setText(hoveredString);
	}
	@Override
	public void mouseOvered() {
		hoveredPosition = (GHQ.getMouseScreenY() - y)/h;
	}
	@Override
	public void outsideMouseOvered() {
		hoveredPosition = GHQ.NONE;
	}
	@Override
	public boolean isMouseEntered() {
		final int H = h*nowListLength;
		return (label.isInputMode() || hoveredPosition != GHQ.NONE) && GHQ.isMouseInArea_Screen(x + w/2, y + H/2, w, H);
	}
	//control
	public void updatePosition() {
		x = label.x;
		y = label.y + h;
	}
	public void removeWord(String word) {
		optionList.remove(word);
	}
}
