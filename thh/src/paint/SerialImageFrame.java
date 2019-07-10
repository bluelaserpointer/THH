package paint;

import java.util.HashMap;

import core.GHQ;
import paint.dotRect.DotRectPaint;

public class SerialImageFrame implements DotRectPaint{
	private static final long serialVersionUID = -4932870277698726733L;
	protected final ImageFrame[] IMAGE_IF;
	protected double baseAngle;
	protected int SPAN;
	private static final HashMap<String, ImageFrame> createdList = new HashMap<String, ImageFrame>();
	
	//init
	public SerialImageFrame(double angle, int spanFrame, String... imageURL) {
		IMAGE_IF = new ImageFrame[imageURL.length];
		for(int i = 0;i < imageURL.length;++i) {
			IMAGE_IF[i] = new ImageFrame(imageURL[i]);
			if(!createdList.containsKey(imageURL[i]))
				createdList.put(imageURL[i], IMAGE_IF[i]);
		}
		baseAngle = angle;
		SPAN = spanFrame;
	}
	public SerialImageFrame(int spanFrame, String... imageURL) {
		IMAGE_IF = new ImageFrame[imageURL.length];
		for(int i = 0;i < imageURL.length;++i) {
			IMAGE_IF[i] = new ImageFrame(imageURL[i]);
			if(!createdList.containsKey(imageURL[i]))
				createdList.put(imageURL[i], IMAGE_IF[i]);
		}
		SPAN = spanFrame;
	}
	public static ImageFrame createNew(String imageURL) {
		final ImageFrame IMAGE_FRAME = createdList.get(imageURL);
		return IMAGE_FRAME != null ? IMAGE_FRAME : new ImageFrame(imageURL);
	}
	/*public static ImageFrame createNew(String imageURL, double angle) {
		final ImageFrame IMAGE_FRAME = createNew(imageURL);
		IMAGE_FRAME.baseAngle = angle;
		return IMAGE_FRAME;
	}*/
	
	//main role
	protected ImageFrame decideImage() {
		return IMAGE_IF[GHQ.nowFrame() % (IMAGE_IF.length*SPAN) / SPAN];
	}
	@Override
	public void dotPaint(int x,int y) {
		decideImage().dotPaint_turn(x, y, baseAngle);
	}
	@Override
	public void dotPaint_capSize(int x,int y,int maxSize) {
		decideImage().dotPaint_capSize(x, y, maxSize);
	}
	@Override
	public void dotPaint_rate(int x, int y, double rate) {
		decideImage().dotPaint_rate(x, y, rate);
	}
	@Override
	public void rectPaint(int x,int y,int w,int h) {
		decideImage().rectPaint(x, y, w, h);
	}
	
	//information
	@Override
	public int getDefaultW() {
		return decideImage().getDefaultW();
	}
	@Override
	public int getDefaultH() {
		return decideImage().getDefaultH();
	}
}
