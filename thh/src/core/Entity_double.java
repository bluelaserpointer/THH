package core;

public abstract class Entity_double {
	public final DynamInteractable source; //an information source of user
	public final Dynam dynam = new Dynam();
	public final int INITIAL_FRAME;
	public final double INITIAL_X,INITIAL_Y;
	public Entity_double() {
		this.source = DynamInteractable.BlankDI;
		INITIAL_FRAME = GHQ.getNowFrame();
		INITIAL_X = dynam.getX();
		INITIAL_Y = dynam.getY();
	}
	public Entity_double(DynamInteractable source) {
		this.source = source;
		INITIAL_FRAME = 0;
		INITIAL_X = 0;
		INITIAL_Y = 0;
	}
	public Entity_double(DynamInteractable source,Dynam dynam) {
		this.source = source;
		this.dynam.initBySample(dynam);
		INITIAL_FRAME = GHQ.getNowFrame();
		INITIAL_X = dynam.getX();
		INITIAL_Y = dynam.getY();
	}
	public Entity_double(DynamInteractable source,Dynam dynam,int nowFrame) {
		this.source = source;
		this.dynam.initBySample(dynam);
		INITIAL_FRAME = nowFrame;
		INITIAL_X = dynam.getX();
		INITIAL_Y = dynam.getY();
	}
	abstract public boolean defaultIdle();
	abstract public void defaultPaint();
}