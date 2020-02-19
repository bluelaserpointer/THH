package paint;

import java.util.LinkedList;

public class Layer implements HasPaint {
	private final LinkedList<HasPaint> paintLayers = new LinkedList<HasPaint>();
	public Layer() {}
	public Layer(HasPaint...hasPaints) {
		setLayers(hasPaints);
	}
	@Override
	public void paint() {
		for(HasPaint ver : paintLayers)
			ver.paint();
	}
	//control
	public Layer setLayers(HasPaint...hasPaints) {
		paintLayers.clear();
		for(HasPaint ver : hasPaints)
			paintLayers.add(ver);
		return this;
	}
	public Layer appendLayer(HasPaint hasPaint) {
		paintLayers.add(hasPaint);
		return this;
	}
	public Layer patialSort(HasPaint...hasPaints) {
		//TODO: complete patialSort
		return this;
	}
	public LinkedList<HasPaint> layerList() {
		return paintLayers;
	}
}
