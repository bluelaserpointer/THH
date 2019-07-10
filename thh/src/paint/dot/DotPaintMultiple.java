package paint.dot;

public class DotPaintMultiple implements DotPaint{
	private static final long serialVersionUID = -4214430765465074156L;
	private final DotPaint[] SCRIPTS;
	private final int BIGGEST_SIZE;
	public DotPaintMultiple(DotPaint...dotPaints) {
		BIGGEST_SIZE = DotPaint.getMaxSize(SCRIPTS = dotPaints);
	}
	@Override
	public void dotPaint(int x, int y) {
		for(DotPaint ver : SCRIPTS)
			ver.dotPaint(x, y);
	}
	@Override
	public void dotPaint_capSize(int x, int y, int maxSize) {
		if(BIGGEST_SIZE > maxSize) {
			final double RATE = maxSize/BIGGEST_SIZE;
			for(DotPaint ver : SCRIPTS)
				ver.dotPaint_rate(x, y, RATE);
		}else {
			for(DotPaint ver : SCRIPTS)
				ver.dotPaint(x, y);
		}
	}
	@Override
	public void dotPaint_rate(int x, int y, double rate) {
		for(DotPaint ver : SCRIPTS)
			ver.dotPaint_rate(x, y, rate);
	}
}
