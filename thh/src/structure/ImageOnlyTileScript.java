package structure;

import paint.ImageFrame;
import paint.RectPaint;

public class ImageOnlyTileScript extends StructureScript<Tile>{
	private static final long serialVersionUID = -2321346080405596602L;
	private final RectPaint paintScript;
	public ImageOnlyTileScript(int imageIID) {
		paintScript = new ImageFrame(imageIID);
	}
	@Override
	public String getName() {
		return "ImageOnly_TileScript";
	}
	@Override
	public void paint(Tile tile,boolean doAnimation) {
		tile.putPaint(paintScript);
	}
	
}
