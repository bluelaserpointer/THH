package structure;

import java.io.Serializable;

import core.GHQ;
import physics.Dynam;
import physics.HasDynam;

public class StructureScript<T extends Structure> implements Serializable{
	private static final long serialVersionUID = -4624130557133104038L;
	public String getName() {
		return GHQ.NOT_NAMED;
	}
	//generation
	public final void create(HasDynam user) {
		create(user,user.getDynam());
	}
	public void create(HasDynam user,Dynam baseDynam) {}
	
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
