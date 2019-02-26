package vegetation;

/**
 * Vegetation with idle method.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class CensoredVegetation extends Vegetation{

	public CensoredVegetation(int imageIID,int x,int y,int w,int h) {
		super(imageIID,x,y,w,h);
	}
	public CensoredVegetation(String imageURL,int x,int y,int w,int h) {
		super(imageURL,x,y,w,h);
	}
	public CensoredVegetation(int imageIID,int x,int y) {
		super(imageIID,x,y);
	}
	public CensoredVegetation(String imageURL,int x,int y) {
		super(imageURL,x,y);
	}
	public CensoredVegetation(CensoredVegetation sample) {
		super(sample);
	}
	
	public void Idle() {
		
	}
}
