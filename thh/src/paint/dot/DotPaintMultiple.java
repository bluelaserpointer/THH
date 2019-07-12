package paint.dot;

public class DotPaintMultiple implements DotPaint{
	private static final long serialVersionUID = -4214430765465074156L;
	private final DotPaint[] SCRIPTS;
	private final int BIGGEST_SIZE, BIGGEST_W, BIGGEST_H;
	public DotPaintMultiple(DotPaint...dotPaints) {
		BIGGEST_SIZE = DotPaint.getMaxSize(SCRIPTS = dotPaints);
		int wBiggest = 0, hBiggest = 0;
		for(DotPaint ver : SCRIPTS) {
			final int W = ver.width(), H = ver.height();
			if(wBiggest < W)
				wBiggest = W;
			if(hBiggest < H)
				hBiggest = H;
		}
		BIGGEST_W = wBiggest;
		BIGGEST_H = hBiggest;
	}
	@Override
	public void dotPaint(int x, int y) {
		for(DotPaint ver : SCRIPTS)
			ver.dotPaint(x, y);
	}
	@Override
	public int sizeOfBigger() {
		return BIGGEST_SIZE;
	}
	@Override
	public int width() {
		return BIGGEST_W;
	}
	@Override
	public int height() {
		return BIGGEST_H;
	}
}
