package paint;

import core.GHQ;

public class SerialImageFrameAlpha extends SerialImageFrame{
	private static final long serialVersionUID = -3305478535136347723L;

	private int startFrame;
	
	public SerialImageFrameAlpha(int spanFrame, String... imageURLs) {
		super(spanFrame, imageURLs);
	}
	
	public void animate() {
		startFrame = GHQ.nowFrame();
	}
	
	private float decideAlpha() {
		if(GHQ.getPassedFrame(startFrame) > IMAGE_IF.length*SPAN)
			return 1.0f;
		else
			return (float)(GHQ.getPassedFrame(startFrame) % (IMAGE_IF.length*SPAN) % SPAN)/SPAN;
	}
	private int decideImageID() {
		if(GHQ.getPassedFrame(startFrame) > IMAGE_IF.length*SPAN)
			return IMAGE_IF.length - 1;
		else {
			return GHQ.getPassedFrame(startFrame) % (IMAGE_IF.length*SPAN) / SPAN;
		}
	}
	@Override
	public void dotPaint(int x,int y) {
		GHQ.setImageAlpha(decideAlpha());
		final int ID = decideImageID();
		if(ID + 1 < IMAGE_IF.length) {
			GHQ.setImageAlpha(1.0f - decideAlpha());
			IMAGE_IF[ID + 1].dotPaint_turn(x, y, baseAngle);
		}
		IMAGE_IF[ID].dotPaint_turn(x, y, baseAngle);
		GHQ.setImageAlpha();
	}
	@Override
	public void dotPaint_capSize(int x,int y,int maxSize) {
		GHQ.setImageAlpha(decideAlpha());
		final int ID = decideImageID();
		if(ID + 1 < IMAGE_IF.length) {
			GHQ.setImageAlpha(1.0f - decideAlpha());
			IMAGE_IF[ID + 1].dotPaint_capSize(x, y, maxSize);
		}
		IMAGE_IF[ID].dotPaint_capSize(x, y, maxSize);
		GHQ.setImageAlpha();
	}
	@Override
	public void dotPaint_rate(int x, int y, double rate) {
		GHQ.setImageAlpha(decideAlpha());
		final int ID = decideImageID();
		if(ID + 1 < IMAGE_IF.length) {
			GHQ.setImageAlpha(1.0f - decideAlpha());
			IMAGE_IF[ID + 1].dotPaint_rate(x, y, rate);
		}
		IMAGE_IF[ID].dotPaint_rate(x, y, rate);
		GHQ.setImageAlpha();
	}
	@Override
	public void rectPaint(int x,int y,int w,int h) {
		final int ID = decideImageID();
		for(int i = 0;i < ID;++i)
			IMAGE_IF[i].rectPaint(x, y, w, h);
		GHQ.setImageAlpha(decideAlpha());
		IMAGE_IF[ID].rectPaint(x, y, w, h);
		GHQ.setImageAlpha();
	}
}
