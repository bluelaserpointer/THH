package vegetation;

import java.awt.geom.Rectangle2D;

import core.HasBoundingBox;
import paint.HasRectPaint;
import paint.RectPaint;

public class Vegetation implements HasBoundingBox,HasRectPaint{
	protected final RectPaint paintScript;
	private int x,y,w,h;

	//init
	public Vegetation(RectPaint paintScript,int x,int y,int w,int h) {
		this.paintScript = paintScript;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public Vegetation(RectPaint paintScript,int x,int y) {
		this.paintScript = paintScript;
		this.x = x;
		this.y = y;
		this.w = paintScript.getDefaultW();
		this.h = paintScript.getDefaultH();
	}
	public Vegetation(Vegetation sample) {
		this.paintScript = sample.paintScript;
		this.x = sample.x;
		this.y = sample.y;
		this.w = sample.w;
		this.h = sample.h;
	}
	//idle
	public void idle() {
		
	}
	public void paint() {
		if(paintScript == null)
			return;
		paintScript.paint(x, y, w, h);
	}
	//control
	@Override
	public Vegetation clone() {
		return new Vegetation(this);
	}
	public void setXY(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	//information
	@Override
	public Rectangle2D getBoundingBox() {
		return new Rectangle2D.Double(x, y, w, h);
	}
	@Override
	public final RectPaint getPaintScript() {
		return paintScript;
	}
}
