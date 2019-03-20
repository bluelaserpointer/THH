package paint;

public class RectPaintMultiple implements RectPaint{
	private static final long serialVersionUID = -4214430765465074156L;
	private final RectPaint[] SCRIPTS;
	public RectPaintMultiple(RectPaint...RectPaints) {
		SCRIPTS = RectPaints;
	}
	@Override
	public void rectPaint(int x, int y, int w,int h) {
		for(RectPaint ver : SCRIPTS)
			ver.rectPaint(x, y, w, h);
	}
	@Override
	public void rectPaint(int x, int y, int size) {
		for(RectPaint ver : SCRIPTS)
			ver.rectPaint(x, y, size);
	}
}
