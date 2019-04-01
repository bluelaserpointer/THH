package physicis;

import static java.lang.Math.sqrt;

/**
 * A subclass of {@link Dynam} which is able to count its physical distance.<br>
 * Note that distance count is only available for {@link DstCntDynam#move()} and {@link DstCntDynam#approach(HasDynam, double)}/{@link DstCntDynam#approach(double, double, double)}.<br>
 * Such as {@link DstCntDynam#setXY(double, double)} don't affect the distance count.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class DstCntDynam extends Dynam{
	private static final long serialVersionUID = -617770729132898247L;
	
	protected double movedDistance;
	
	//init
	public DstCntDynam() {
		super();
	}
	public DstCntDynam(Dynam sample) {
		super(sample);
	}
	
	//control
	@Override
	public void setAllBySample(Dynam sample) {
		super.setAllBySample(sample);
		if(sample instanceof DstCntDynam)
			movedDistance = ((DstCntDynam) sample).movedDistance;
	}
	public void setAllBySampleAndInitDistCnt(Dynam sample) {
		super.setAllBySample(sample);
		movedDistance = 0;
	}
	@Override
	public void clear() {
		super.clear();
		movedDistance = 0.0;
	}
	@Override
	public void move() {
		if(xSpd != 0 || ySpd != 0) {
			x += xSpd;
			y += ySpd;
			movedDistance += sqrt(xSpd*xSpd + ySpd*ySpd);
		}
	}
	public void approach(double dstX,double dstY,double speed) {
		final double DX = dstX - x,DY = dstY - y;
		final double DISTANCE = sqrt(DX*DX + DY*DY);
		if(DISTANCE <= speed) {
			x = dstX;
			y = dstY;
			movedDistance += DISTANCE - speed;
		}else {
			final double RATE = speed/DISTANCE;
			x += DX*RATE;
			y += DY*RATE;
			movedDistance += speed;
		}
	}
	
	//information
	public double getMovedDistance() {
		return movedDistance;
	}
}
