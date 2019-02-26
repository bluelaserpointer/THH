package vegetation;

import java.awt.geom.Rectangle2D;

import core.GHQ;
import core.HasBoundingBox;

public class Vegetation implements HasBoundingBox{
	private String imageURL;
	private int imageIID;
	private int x,y,w,h;

	public Vegetation(int imageIID,int x,int y,int w,int h) {
		this.imageURL = "(NONE)";
		this.imageIID = imageIID;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public Vegetation(String imageURL,int x,int y,int w,int h) {
		this.imageURL = imageURL;
		imageIID = GHQ.loadImage(imageURL);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public Vegetation(int imageIID,int x,int y) {
		this.imageURL = "(NONE)";
		this.imageIID = imageIID;
		this.x = x;
		this.y = y;
		this.w = this.h = GHQ.NONE;
	}
	public Vegetation(String imageURL,int x,int y) {
		this.imageURL = imageURL;
		imageIID = GHQ.loadImage(imageURL);
		this.x = x;
		this.y = y;
		this.w = this.h = GHQ.NONE;
	}
	public Vegetation(Vegetation sample) {
		this.imageURL = sample.imageURL;
		this.imageIID = sample.imageIID;
		this.x = sample.x;
		this.y = sample.y;
		this.w = sample.w;
		this.h = sample.h;
	}
	@Override
	public Vegetation clone() {
		return new Vegetation(this);
	}
	public void setImage(String imageURL) {
		this.imageURL = imageURL;
		imageIID = GHQ.loadImage(imageURL);
	}
	public void setImage(int imageIID) {
		this.imageURL = "(NONE)";
		this.imageIID = imageIID;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setXY(int x,int y) {
		this.x = x;
		this.y = y;
	}
	public void paint() {
		if(imageIID == GHQ.NONE)
			return;
		if(w == GHQ.NONE)
			GHQ.drawImageGHQ_center(imageIID, x, y);
		else
			GHQ.drawImageGHQ_center(imageIID, x, y, w, h);
	}
	
	//information
	@Override
	public Rectangle2D getBoundingBox() {
		if(w == GHQ.NONE) {
			final int W = GHQ.getImageWByID(imageIID),H = GHQ.getImageHByID(imageIID);
			return new Rectangle2D.Double(x - W/2, y - H/2, W, H);
		}else
			return new Rectangle2D.Double(x, y, w, h);
	}
}
