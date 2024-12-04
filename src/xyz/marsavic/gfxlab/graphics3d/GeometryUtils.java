package xyz.marsavic.gfxlab.graphics3d;


import xyz.marsavic.gfxlab.Vec3;

public class GeometryUtils {
	
	/** An orthogonal vector to v. */
	public static Vec3 normal(Vec3 v) {
		if (v.x() != 0 || v.y() != 0) {
			return Vec3.xyz(-v.y(), v.x(), 0);
		} else {
			return Vec3.EX;
		}
	}

	
	public static Vec3 reflected(Vec3 n, Vec3 v) {
		return n.mul(2 * v.dot(n) / n.lengthSquared()).sub(v);
	}

	public static Vec3 reflectedN(Vec3 n_, Vec3 v) {
		return n_.mul(2 * v.dot(n_)).sub(v);
	}
	
}
