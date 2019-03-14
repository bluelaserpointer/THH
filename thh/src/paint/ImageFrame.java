package paint;

import core.GHQ;

public class ImageFrame implements RectPaint,DotPaint{
	private static final long serialVersionUID = -1537274221051413163L;
	private final int IMAGE_IID;
	private final int IMAGE_W,IMAGE_H;
	private final double BASE_ANGLE;
	public ImageFrame(String imageURL, double angle) {
		IMAGE_IID = GHQ.loadImage(imageURL);
		IMAGE_W = GHQ.getImageWByID(IMAGE_IID);
		IMAGE_H = GHQ.getImageHByID(IMAGE_IID);
		BASE_ANGLE = angle;
	}
	public ImageFrame(String imageURL) {
		IMAGE_IID = GHQ.loadImage(imageURL);
		IMAGE_W = GHQ.getImageWByID(IMAGE_IID);
		IMAGE_H = GHQ.getImageHByID(IMAGE_IID);
		BASE_ANGLE = 0.0;
	}
	@Override
	public void paint(int x,int y) {
		GHQ.drawImageGHQ_center(IMAGE_IID, x, y, BASE_ANGLE);
	}
	@Override
	public void paint(int x,int y,double angle) {
		GHQ.drawImageGHQ_center(IMAGE_IID, x, y, BASE_ANGLE + angle);
	}
	@Override
	public void paint(int x,int y,int maxSize) {
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
	public void paint(int x,int y,int w,int h) {
		GHQ.drawImageGHQ(IMAGE_IID, x, y, w, h, BASE_ANGLE);
	}
	@Override
	public int getDefaultW() {
		return IMAGE_W;
	}
	@Override
	public int getDefaultH() {
		return IMAGE_H;
	}
}
