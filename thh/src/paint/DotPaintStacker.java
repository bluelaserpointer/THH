package paint;

import java.util.ArrayList;

public class DotPaintStacker implements DotPaint{
	private static final long serialVersionUID = -832458123024960846L;
	private final ArrayList<DotPaint> scripts = new ArrayList<DotPaint>();
	private final DotPaint[] LAYER_PAINTS;
	
	//init
	public DotPaintStacker(DotPaint...layerPaints) {
		LAYER_PAINTS = layerPaints;
	}
	//main role
	@Override
	public void dotPaint(int x, int y) {
		for(DotPaint script : scripts)
			script.dotPaint(x, y);
	}
	@Override
	public void dotPaint_capSize(int x, int y, int maxSize) {
		final int BIGGEST_SIZE = DotPaint.getMaxSize(scripts);
		if(BIGGEST_SIZE > maxSize) {
			final double RATE = maxSize/BIGGEST_SIZE;
			for(DotPaint ver : scripts)
				ver.dotPaint_rate(x, y, RATE);
		}else {
			for(DotPaint ver : scripts)
				ver.dotPaint(x, y);
		}
	}
	@Override
	public void dotPaint_rate(int x, int y, double rate) {
		for(DotPaint ver : scripts)
			ver.dotPaint_rate(x, y, rate);
	}
	
	//control
	public <T extends DotPaint>T addScript(T dotPaint){
		scripts.add(dotPaint);
		return dotPaint;
	}
	public boolean removeScript(DotPaint dotPaint){
		return scripts.remove(dotPaint);
	}
	public void clear() {
		scripts.clear();
	}
	
	//information
	public int getScriptsAmount() {
		return scripts.size();
	}
	public int getLayerAmount() {
		return LAYER_PAINTS.length;
	}

}
