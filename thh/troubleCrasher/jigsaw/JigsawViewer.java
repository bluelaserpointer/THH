package troubleCrasher.jigsaw;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GHQ;
import gui.GUIParts;

public class JigsawViewer extends GUIParts {
	private JigsawBoard board;
	
	//init
	public JigsawViewer(JigsawBoard board) {
		this.board = board;
	}
	public JigsawViewer(int xGrids, int yGrids) {
		this.board = new JigsawBoard(xGrids, yGrids);
	}
	public JigsawViewer setBoard(JigsawBoard board) {
		this.board = board;
		return this;
	}
	
	//idle
	@Override
	public void paint() {
		super.paint();
		for(Jigsaw jigsaw : board.jigsaws()) {
			jigsaw.paint(left(), top());
		}
		final Graphics2D g2 = GHQ.getG2D(Color.WHITE, GHQ.stroke3);
		for(int xi = 0; xi < board.xGrids; ++xi) {
			for(int yi = 0; yi < board.yGrids; ++yi) {
				g2.drawRect(left() + xi*JigsawEnum.JIGSAW_GRID_SIZE, top() + yi*JigsawEnum.JIGSAW_GRID_SIZE, JigsawEnum.JIGSAW_GRID_SIZE, JigsawEnum.JIGSAW_GRID_SIZE);
			}
		}
	}
	
	//info
	public JigsawBoard board() {
		return board;
	}
}
