package paint;

public interface RectPaint extends PaintScript{
	public static final RectPaint BLANK_SCRIPT = new RectPaint() {
		private static final long serialVersionUID = -4558496325553265908L;
		@Override
		public void paint(int x, int y, int w, int h) {}
	};
	public abstract void paint(int x,int y,int w,int h);
}
