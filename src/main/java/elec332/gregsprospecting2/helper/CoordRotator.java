package elec332.gregsprospecting2.helper;

/**
 * Created by gcewing.
 */
public class CoordRotator {

	public double sinP, cosP, sinY, cosY;
	public double x, y, z;
	
	public CoordRotator(double pitch, double yaw) {
		sinP = Math.sin(pitch);
		cosP = Math.cos(pitch);
		sinY = Math.sin(yaw);
		cosY = Math.cos(yaw);
	}
	
	public void rotate(double xs, double ys, double zs) {
		double xp = xs;
		double yp = cosP * ys - sinP * zs;
		double zp = sinP * ys + cosP * zs;
		
		z = cosY * zp - sinY * xp;
		x = sinY * zp + cosY * xp;
		y = yp;
	}
}
