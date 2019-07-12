package paint;

import java.util.HashMap;

import core.GHQ;
import core.LoadRequester;
import paint.dotRect.DotRectPaint;

/**
 * A popular PaintScript subclass which loads image resource and display it.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class ImageFrame implements DotRectPaint{
	private static final long serialVersionUID = -1537274221051413163L;
	private int IMAGE_IID;
	private static final HashMap<String, ImageFrame> createdList = new HashMap<String, ImageFrame>();
	
	//init
	public ImageFrame(String imageURL) {
		GHQ.addLoadRequester(new LoadRequester() {
			@Override
			public void loadResource() {
				IMAGE_IID = GHQ.loadImage(imageURL);
			}
		});
		if(!createdList.containsKey(imageURL))
			createdList.put(imageURL, this);
	}
	public static ImageFrame createNew(String imageURL) {
		final ImageFrame IMAGE_FRAME = createdList.get(imageURL);
		return IMAGE_FRAME != null ? IMAGE_FRAME : new ImageFrame(imageURL);
	}
	
	//main role
	@Override
	public void dotPaint(int x, int y) {
		GHQ.drawImageGHQ_center(IMAGE_IID, x, y);
	}
	@Override
	public void rectPaint(int x,int y,int w,int h) {
		GHQ.drawImageGHQ(IMAGE_IID, x, y, w, h);
	}
	
	//information
	@Override
	public int width() {
		return 0;
	}
	@Override
	public int height() {
		return 0;
	}
}
