package troubleCrasher.jigsaw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import core.GHQ;
import gui.GUIParts;

public class JigsawViewer extends GUIParts {
	private JigsawBoard board;
	private Jigsaw hookingJigsaw = new Jigsaw(JigsawEnum.HOLLOW1);
	private Jigsaw disposedJigsaw;
	
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
		if(hookingJigsaw != null) {
			hookingJigsaw.paint(GHQ.mouseScreenX(), GHQ.mouseScreenY());
		}
	}
	@Override
	public boolean clicked(MouseEvent e) { //leftClick:place or take, rightClick:rotate the hooking jigsaw
		final boolean consumed = super.clicked(e);
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(hookingJigsaw != null) { //拿进
				placeHookingJigsaw();
			} else { //拿出
				if(board().isBlocked((GHQ.mouseScreenX() - left()) / JigsawEnum.JIGSAW_GRID_SIZE, (GHQ.mouseScreenY() - top()) / JigsawEnum.JIGSAW_GRID_SIZE)) {
					
				}
			}
		}
		return consumed;
	}
	
	//info
	public JigsawBoard board() {
		return board;
	}
	public Jigsaw hookingJigsaw() {
		return hookingJigsaw;
	}
	public Jigsaw disposedJigsaw() {
		return disposedJigsaw;
	}
	
	public void hookJigsaw(Jigsaw jigsaw) {
		this.hookingJigsaw = jigsaw;
		if(jigsaw != null)
			hookingJigsaw.setGridPos(0, 0);
	}
	public void removeHookingJigsaw() {
		this.hookJigsaw(null);
	}
	public void disposeHookJigsaw() {
		disposedJigsaw = hookingJigsaw;
		removeHookingJigsaw();
	}
	public void hookDisposedJigsaw() {
		this.hookJigsaw(disposedJigsaw);
		disposedJigsaw = null;
	}
	public void placeHookingJigsaw() {
		if(hookingJigsaw == null)
			return;
		final int gridPosX = (GHQ.mouseScreenX() - left())/JigsawEnum.JIGSAW_GRID_SIZE;
		final int gridPosY = (GHQ.mouseScreenY() - top())/JigsawEnum.JIGSAW_GRID_SIZE;
		hookingJigsaw.setGridPos(gridPosX, gridPosY);
		if(board().jigsawsIntersects(hookingJigsaw)) {
			hookingJigsaw.setGridPos(0, 0);
			placeFailed();
		} else {
			board().setJigsaw(hookingJigsaw);
			placeSucceed();
			hookingJigsaw = null;
		}
	}
	public void placeFailed() {
		//TODO: sounds prohibited SE
	}
	public void placeSucceed() {
		//TODO: sounds succeed SE
	}
}
