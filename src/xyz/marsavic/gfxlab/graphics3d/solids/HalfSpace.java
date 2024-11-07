package xyz.marsavic.gfxlab.graphics3d.solids;

import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.GeometryUtils;
import xyz.marsavic.gfxlab.graphics3d.Hit;
import xyz.marsavic.gfxlab.graphics3d.Ray;
import xyz.marsavic.gfxlab.graphics3d.Solid;


public class HalfSpace implements Solid {
	
	/** A half-space defined by a point p on the bounding plane, and two non-collinear vectors, e and f, parallel to the bounding plane. */
	private HalfSpace(Vec3 p, Vec3 e, Vec3 f) {
	}
	
	
	/** A half-space defined by a point p on the bounding plane, and two non-collinear vectors, e and f, parallel to the bounding plane. */
	public static HalfSpace pef(Vec3 p, Vec3 e, Vec3 f) {
		return new HalfSpace(p, e, f);
	}
	
	
	/** A half-space defined by a three points on the bounding plane. */
	public static HalfSpace pqr(Vec3 p, Vec3 q, Vec3 r) {
		return pef(p, q.sub(p), r.sub(p));
	}
	
	
	/** A half-space defined by a point p on a bounding plane, and a normal vector to the bounding plane. */
	public static HalfSpace pn(Vec3 p, Vec3 n) {
		double nl = n.length();
		Vec3 e = GeometryUtils.normal(n).normalizedTo(nl);
		Vec3 f = n.cross(e).normalizedTo(nl);
		return new HalfSpace(p, e, f);
	}
	
	
	@Override
	public Hit firstHit(Ray ray, double afterTime) {
		// TODO
		return Hit.AtInfinity.axisAligned(ray.d(), false);
	}	
	
}
