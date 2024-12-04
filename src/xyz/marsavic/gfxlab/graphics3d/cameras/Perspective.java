package xyz.marsavic.gfxlab.graphics3d.cameras;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.graphics3d.Camera;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Ray;
import xyz.marsavic.utils.Numeric;

public class Perspective implements Camera {
	
	private final double k;
	
	public Perspective() {
		this(1);
	}
	
	public Perspective(double k) {
		this.k = k;
	}
	
	
	public static Perspective fov(double phi) {
		return new Perspective(1 / Numeric.tanT(phi/2));
	}
	
	@Override
	public Ray exitingRay(Vector p) {
		return Ray.pd(Vec3.ZERO, Vec3.zp(k, p));
	}
	
}
