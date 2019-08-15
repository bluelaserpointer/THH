package paint;

import java.awt.Image;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import core.GHQ;
import core.LoadRequester;
import paint.dotRect.DotRectPaint;

/**
 * A popular PaintScript subclass which loads image resource and display it.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class ImageFrame implements DotRectPaint{
	private static final long serialVersionUID = -1537274221051413163L;
	private transient Image IMAGE;
	private URL image_url;
	private static final HashMap<URL, Image> preloadedImageMap = new HashMap<URL, Image>();
	private static final HashMap<String, ImageFrame> createdIFMap = new HashMap<String, ImageFrame>();
	
	/////////////
	//init
	/////////////
	private ImageFrame(String urlStr) {
		GHQ.addLoadRequester(new LoadRequester() {
			@Override
			public void loadResource() {
				IMAGE = loadImage(image_url = GHQ.hq.getClass().getResource("/" + urlStr));
			}
		});
	}
	public void loadFromSave() {
		IMAGE = loadImage(image_url);
	}
	/**
	* Load the image file.
	* @param url 
	* @return ImageFrame
	* @since alpha1.0
	*/
	public static Image loadImage(URL url) {
		if(url == null) {
			System.out.println("received a null image url");
			return GHQ.hq.createImage(1, 1);
		}
		if(preloadedImageMap.containsKey(url))
			return preloadedImageMap.get(url);
		Image image;
		try{
			image = GHQ.hq.createImage((ImageProducer)url.getContent());
			GHQ.hq.tracker.addImage(image,1);
			preloadedImageMap.put(url, image);
		}catch(IOException | NullPointerException e){ //異常-読み込み失敗
			if(url.toString() != null && !url.toString().isEmpty())
				GHQ.warningBox("Image " + url + " is not found and could not be loaded.Error code: " + url.toString(),"ImageLoadingError");
			else
				GHQ.warningBox("Image " + url + " is not found and could not be loaded.", "ImageLoadingError");
			return GHQ.hq.createImage(1, 1);
		}
		return image;
	}
	public static ImageFrame create(String imageURL) {
		if(createdIFMap.containsKey(imageURL))
			return createdIFMap.get(imageURL);
		final ImageFrame NEW_IF = new ImageFrame(imageURL);
		createdIFMap.put(imageURL, NEW_IF);
		return NEW_IF;
	}
	public static final void preloadImageFolder(File folder) {
		for(File imageFile : folder.listFiles()) {
			final String FILE_NAME = imageFile.getName();
			if(FILE_NAME.endsWith(".png") || FILE_NAME.endsWith(".jpeg") || FILE_NAME.endsWith(".jpg") || FILE_NAME.endsWith(".gif")) {
				try {
					loadImage(imageFile.toURI().toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//main role
	@Override
	public void dotPaint(int x, int y) {
		GHQ.drawImageGHQ_center(IMAGE, x, y);
	}
	@Override
	public void rectPaint(int x,int y,int w,int h) {
		GHQ.drawImageGHQ(IMAGE, x, y, w, h);
	}
	
	//information
	@Override
	public int width() {
		return IMAGE.getWidth(null);
	}
	@Override
	public int height() {
		return IMAGE.getHeight(null);
	}
}
