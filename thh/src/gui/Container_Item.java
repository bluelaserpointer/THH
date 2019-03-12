package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import core.GHQ;
import paint.HasRectPaint;
import paint.RectPaint;
import unit.Item;

public class Container_Item extends Container<Item>{
	public Container_Item(String group, RectPaint backgroundPaint, RectPaint cellPaint, int x, int y, int cellW, int cellH, int cellXAmount, int cellYAmount, ArrayList<Item> items) {
		super(group, backgroundPaint, cellPaint, x, y, cellW, cellH, cellXAmount, cellYAmount, items);
	}
	
	@Override
	public void paint() {
		for(int xi = 0;xi < cellXAmount;xi++) {
			for(int yi = 0;yi < cellYAmount;yi++) {
				final int PAINT_X = super.x + xi*CELL_W,PAINT_Y = super.y + yi*CELL_H;
				if(cellPaint != null)
					cellPaint.paint(PAINT_X, PAINT_Y, CELL_W, CELL_H);
				final int ID = xi + yi*cellXAmount;
				if(ID < contents.size()) {
					final Item item = contents.get(ID);
					if(item != null) {
						((HasRectPaint)item).getPaintScript().paint(PAINT_X + 10, PAINT_Y + 10, CELL_W - 20, CELL_H - 20);
						final Graphics2D G2 = GHQ.getGraphics2D();
						G2.setColor(Color.GRAY);
						G2.drawString(String.valueOf(item.getAmount()), PAINT_X + CELL_W - 23, PAINT_Y + CELL_H - 9);
						G2.setColor(Color.BLACK);
						G2.drawString(String.valueOf(item.getAmount()), PAINT_X + CELL_W - 24, PAINT_Y + CELL_H - 10);
					}
				}
			}
		}
	}

}
