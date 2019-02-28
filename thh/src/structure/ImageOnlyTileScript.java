package structure;

import paint.ImageFrame;
import paint.PaintScript;

public class ImageOnlyTileScript extends StructureScript<Tile>{
	private static final long serialVersionUID = -2321346080405596602L;
	private final PaintScript paintScript;
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
