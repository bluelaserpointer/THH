package paint;

import core.GHQ;

public class ImageFrame implements PaintScript{
	private final int IMAGE_IID;
	public ImageFrame(int imageIID) {
		IMAGE_IID = imageIID;
	}
	public ImageFrame(String imageURL) {
		IMAGE_IID = GHQ.loadImage(imageURL);
	}
	@Override
	public void paint(int x,int y) {
		GHQ.drawImageTHH_center(IMAGE_IID, x, y);
	}
	@Override
	public void paint(int x,int y,int w,int h) {
		GHQ.drawImageTHH_center(IMAGE_IID, x, y, w, h);
	}
}
