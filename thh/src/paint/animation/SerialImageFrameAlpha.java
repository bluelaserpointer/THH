package paint.animation;

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
		if(GHQ.getPassedFrame(startFrame) >= IMAGE_IF.length*SPAN)
			return 1.0f;
		else
			return (float)(GHQ.getPassedFrame(startFrame)%SPAN)/SPAN;
	}
	private int decideImageID() {
		if(GHQ.getPassedFrame(startFrame) >= IMAGE_IF.length*SPAN)
			return IMAGE_IF.length - 1;
		else {
			return GHQ.getPassedFrame(startFrame) / SPAN;
		}
	}
	@Override
	public void dotPaint(int x, int y) {
		final int ID = decideImageID();
		for(int i = 0;i < ID;++i)
			IMAGE_IF[i].dotPaint(x, y);
		GHQ.setImageAlpha(decideAlpha());
		IMAGE_IF[ID].dotPaint(x, y);
		GHQ.setImageAlpha();
	}
	@Override
	public void rectPaint(int x, int y, int w, int h) {
		final int ID = decideImageID();
		for(int i = 0;i < ID;++i)
			IMAGE_IF[i].rectPaint(x, y, w, h);
		GHQ.setImageAlpha(decideAlpha());
		IMAGE_IF[ID].rectPaint(x, y, w, h);
		GHQ.setImageAlpha();
	}
}
