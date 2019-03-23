package paint;

/**
 * Subclasses of this has a accessible {@link DotPaint} instance.
 * Mainly this interface is implemented by stage objects.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public interface HasDotPaint {
	/**
	 * Return the DotPaint instance of this object.
	 * @return {@link DotPaint}
	 */
	public abstract DotPaint getPaintScript();
}
