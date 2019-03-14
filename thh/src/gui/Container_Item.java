package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import core.GHQ;
import paint.RectPaint;
import unit.Item;

public class Container_Item extends Container<Item>{
	public Container_Item(String group, RectPaint backgroundPaint, RectPaint cellPaint, int x, int y, int cellSize, int cellXAmount, int cellYAmount, ArrayList<Item> items) {
		super(group, backgroundPaint, cellPaint, x, y, cellSize, cellXAmount, cellYAmount, items);
	}
	
	@Override
	public void idle() {
		for(int xi = 0;xi < cellXAmount;xi++) {
			for(int yi = 0;yi < cellYAmount;yi++) {
				final int PAINT_X = super.x + xi*CELL_SIZE,PAINT_Y = super.y + yi*CELL_SIZE;
				if(cellPaint != null)
					cellPaint.paint(PAINT_X, PAINT_Y, CELL_SIZE, CELL_SIZE);
				final int ID = xi + yi*cellXAmount;
				if(ID < contents.size()) {
					final Item item = contents.get(ID);
					if(item != null) {
						item.getPaintScript().paint(PAINT_X + CELL_SIZE/2, PAINT_Y + CELL_SIZE/2, (int)(CELL_SIZE*0.8));
						final Graphics2D G2 = GHQ.getGraphics2D();
						G2.setColor(Color.GRAY);
						G2.drawString(String.valueOf(item.getAmount()), PAINT_X + CELL_SIZE - 23, PAINT_Y + CELL_SIZE - 9);
						G2.setColor(Color.BLACK);
						G2.drawString(String.valueOf(item.getAmount()), PAINT_X + CELL_SIZE - 24, PAINT_Y + CELL_SIZE - 10);
					}
				}
			}
		}
	}

}
