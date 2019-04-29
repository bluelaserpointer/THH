package paint;

import java.util.HashMap;

import core.GHQ;

/**
 * A popular PaintScript subclass which loads image resource and display it.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class ImageFrame implements RectPaint,DotPaint{
	private static final long serialVersionUID = -1537274221051413163L;
	private final int IMAGE_IID;
	private double baseAngle;
	private static final HashMap<String, ImageFrame> createdList = new HashMap<String, ImageFrame>();
	
	//init
	public ImageFrame(String imageURL, double angle) {
		IMAGE_IID = GHQ.loadImage(imageURL);
		baseAngle = angle;
		if(!createdList.containsKey(imageURL))
			createdList.put(imageURL, this);
	}
	public ImageFrame(String imageURL) {
		IMAGE_IID = GHQ.loadImage(imageURL);
		if(!createdList.containsKey(imageURL))
			createdList.put(imageURL, this);
	}
	public static ImageFrame createNew(String imageURL) {
		final ImageFrame IMAGE_FRAME = createdList.get(imageURL);
		return IMAGE_FRAME != null ? IMAGE_FRAME : new ImageFrame(imageURL);
	}
	public static ImageFrame createNew(String imageURL, double angle) {
		final ImageFrame IMAGE_FRAME = createNew(imageURL);
		IMAGE_FRAME.baseAngle = angle;
		return IMAGE_FRAME;
	}
	
	//main role
	@Override
	public void dotPaint(int x,int y) {
		GHQ.drawImageGHQ_center(IMAGE_IID, x, y, baseAngle);
	}
	@Override
	public void dotPaint_turn(int x,int y,double angle) {
		GHQ.drawImageGHQ_center(IMAGE_IID, x, y, baseAngle + angle);
	}
	@Override
	public void dotPaint_capSize(int x,int y,int maxSize) {
		final int IMAGE_W = GHQ.getImageWByID(IMAGE_IID),IMAGE_H = GHQ.getImageHByID(IMAGE_IID);
		if(IMAGE_W > IMAGE_H && IMAGE_W > maxSize) {
			final double RATE = (double)maxSize/(double)IMAGE_W;
			GHQ.drawImageGHQ_center(IMAGE_IID, x, y, (int)(IMAGE_W*RATE), (int)(IMAGE_H*RATE));
		}else if(IMAGE_H > maxSize) {
			final double RATE = (double)maxSize/(double)IMAGE_H;
			GHQ.drawImageGHQ_center(IMAGE_IID, x, y, (int)(IMAGE_W*RATE), (int)(IMAGE_H*RATE));
		}else
			GHQ.drawImageGHQ_center(IMAGE_IID, x, y);
	}
	@Override
	public void dotPaint_rate(int x, int y, double rate) {
		final int IMAGE_W = GHQ.getImageWByID(IMAGE_IID),IMAGE_H = GHQ.getImageHByID(IMAGE_IID);
		GHQ.drawImageGHQ_center(IMAGE_IID, x, y, (int)(IMAGE_W*rate), (int)(IMAGE_H*rate));
	}
	@Override
	public void rectPaint(int x,int y,int w,int h) {
		GHQ.drawImageGHQ(IMAGE_IID, x, y, w, h, baseAngle);
	}
	
	//information
	@Override
	public int getDefaultW() {
		return GHQ.getImageWByID(IMAGE_IID);
	}
	@Override
	public int getDefaultH() {
		return GHQ.getImageHByID(IMAGE_IID);
	}
}
