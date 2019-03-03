package paint;

import core.GHQ;

public class ImageFrame implements RectPaint,DotPaint{
	private static final long serialVersionUID = -1537274221051413163L;
	private final int IMAGE_IID;
	public ImageFrame(int imageIID) {
		IMAGE_IID = imageIID;
	}
	public ImageFrame(String imageURL) {
		IMAGE_IID = GHQ.loadImage(imageURL);
	}
	@Override
	public void paint(int x,int y) {
		GHQ.drawImageGHQ_center(IMAGE_IID, x, y);
	}
	@Override
	public void paint(int x,int y,int w,int h) {
		GHQ.drawImageGHQ(IMAGE_IID, x, y, w, h);
	}
	@Override
	public int getDefaultW() {
		return GHQ.getImageWByID(IMAGE_IID);
	}
	@Override
	public int getDefaultH() {
		return GHQ.getImageHByID(IMAGE_IID);
	}
}
