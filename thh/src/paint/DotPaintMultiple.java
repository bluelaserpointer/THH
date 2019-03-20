package paint;

public class DotPaintMultiple implements DotPaint{
	private static final long serialVersionUID = -4214430765465074156L;
	private final DotPaint[] SCRIPTS;
	private final int BIGGEST_SIZE;
	public DotPaintMultiple(DotPaint...dotPaints) {
		SCRIPTS = dotPaints;
		//biggest size
		int biggestSize = 0;
		for(DotPaint ver : SCRIPTS) {
			final int BIGGER_SIZE = Math.max(ver.getDefaultW(), ver.getDefaultH());
			if(biggestSize < BIGGER_SIZE)
				biggestSize = BIGGER_SIZE;
		}
		BIGGEST_SIZE = biggestSize;
	}
	@Override
	public void dotPaint(int x, int y) {
		for(DotPaint ver : SCRIPTS)
			ver.dotPaint(x, y);
	}
	@Override
	public void dotPaint_resize(int x, int y, int maxSize) {
		if(BIGGEST_SIZE > maxSize) {
			final double RATE = maxSize/BIGGEST_SIZE;
			for(DotPaint ver : SCRIPTS)
				ver.dotPaint_resize(x, y, RATE);
		}else {
			for(DotPaint ver : SCRIPTS)
				ver.dotPaint(x, y);
		}
	}
	@Override
	public void dotPaint_resize(int x, int y, double rate) {
		for(DotPaint ver : SCRIPTS)
			ver.dotPaint_resize(x, y, rate);
	}
}
