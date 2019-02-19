package structure;

import java.awt.Color;
import java.io.Serializable;

import core.Dynam;
import core.DynamInteractable;
import core.GHQ;

public class StructureScript<T extends Structure> implements Serializable{
	private static final long serialVersionUID = -4624130557133104038L;
	public static final StructureScript<Tile> WHITE_WALL = new StructureScript<Tile>() {
		private static final long serialVersionUID = -8765648642542906805L;
		@Override
		public void paint(Tile tile,boolean doAnimation) {
			tile.fill(Color.WHITE);
			tile.draw(Color.LIGHT_GRAY, GHQ.stroke3);
		}
	};
	public String getName() {
		return GHQ.NOT_NAMED;
	}
	//generation
	public final void create(DynamInteractable user) {
		create(user,user.getDynam());
	}
	public void create(DynamInteractable user,Dynam baseDynam) {}
	
	//role
	public void idle(T structure) { //Include painting
		if(structure.defaultIdle())
			paint(structure);
	}
	public void paint(T structure,boolean doAnimation) {
		structure.defaultPaint();
	}
	public final void paint(T structure) {
		paint(structure,true);
	}
}
