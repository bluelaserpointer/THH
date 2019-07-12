package paint.animation;

import java.util.HashMap;

import core.GHQ;
import paint.ImageFrame;
import paint.dotRect.DotRectPaint;

public class SerialImageFrame implements DotRectPaint{
	private static final long serialVersionUID = -4932870277698726733L;
	protected final ImageFrame[] IMAGE_IF;
	protected int SPAN;
	private static final HashMap<String, ImageFrame> createdList = new HashMap<String, ImageFrame>();
	
	//init
	public SerialImageFrame(int spanFrame, String... imageURL) {
		IMAGE_IF = new ImageFrame[imageURL.length];
		for(int i = 0;i < imageURL.length;++i) {
			IMAGE_IF[i] = new ImageFrame(imageURL[i]);
			if(!createdList.containsKey(imageURL[i]))
				createdList.put(imageURL[i], IMAGE_IF[i]);
		}
		SPAN = spanFrame;
	}
	
	//main role
	protected ImageFrame decideImage() {
		return IMAGE_IF[GHQ.nowFrame() % (IMAGE_IF.length*SPAN) / SPAN];
	}
	@Override
	public void dotPaint(int x, int y) {
		decideImage().dotPaint(x, y);
	}
	@Override
	public void rectPaint(int x, int y, int w, int h) {
		decideImage().rectPaint(x, y, w, h);
	}
	
	//information
	@Override
	public int width() {
		return decideImage().width();
	}
	@Override
	public int height() {
		return decideImage().height();
	}
}
