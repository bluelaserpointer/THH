package vegetation;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import core.HasBoundingBox;
import paint.HasDotPaint;
import paint.DotPaint;

public class Vegetation implements Serializable,HasBoundingBox,HasDotPaint{
	private static final long serialVersionUID = -5536970507937704287L;
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;
	
	protected final DotPaint paintScript;
	protected int x,y;

	//init
	public Vegetation(DotPaint paintScript,int x,int y) {
		UNIQUE_ID = ++nowMaxUniqueID;
		this.paintScript = paintScript;
		this.x = x;
		this.y = y;
	}
	public Vegetation(Vegetation sample) {
		UNIQUE_ID = ++nowMaxUniqueID;
		this.paintScript = sample.paintScript;
		this.x = sample.x;
		this.y = sample.y;
	}
	//idle
	public void idle() {
		
	}
	public void paint() {
		if(paintScript == null)
			return;
		paintScript.paint(x, y);
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
		final int W = paintScript.getDefaultW(),H = paintScript.getDefaultH();
		return new Rectangle2D.Double(x - W/2, y - H/2, W, H);
	}
	@Override
	public final DotPaint getPaintScript() {
		return paintScript;
	}
}
