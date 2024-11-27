package xyz.marsavic.gfxlab.graphics3d.solids;

import xyz.marsavic.functions.F1;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Hit;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Ray;
import xyz.marsavic.gfxlab.graphics3d.Solid;
import xyz.marsavic.utils.Numeric;


public class Ball implements Solid {
	
	private final Vec3 c;
	private final double r;
	private final F1<Material, Vector> mapMaterial;
	
	// transient
	private final double rSqr;
	
	
	/** Negative r will make the ball inverted (the resulting solid is a complement of a ball). */
	private Ball(Vec3 c, double r, F1<Material, Vector> mapMaterial) {
		this.c = c;
		this.r = r;
		rSqr = r * r;
		this.mapMaterial = mapMaterial;
	}
	
	public static Ball cr(Vec3 c, double r, F1<Material, Vector> mapMaterial) {
		return new Ball(c, r, mapMaterial);
	}
	
	public static Ball cr(Vec3 c, double r) {
		return cr(c, r, Material.DEFAULT);
	}
	
	
	public Vec3 c() {
		return c;
	}
	
	public double r() {
		return r;
	}
	
	
	@Override
	public Hit firstHit(Ray ray, double afterTime) {
		Vec3 e = c().sub(ray.p());                                // Vector from the ray origin to the ball center
		
		double dSqr = ray.d().lengthSquared();
		double l = e.dot(ray.d()) / dSqr;
		double mSqr = l * l - (e.lengthSquared() - rSqr) / dSqr;
		
		if (mSqr > 0) {
			double m = Math.sqrt(mSqr);
			if (l - m > afterTime) return new HitBall(ray, l - m);
			if (l + m > afterTime) return new HitBall(ray, l + m);
		}
		return Hit.AtInfinity.axisAligned(ray.d(), r < 0);
	}
	
	
	class HitBall extends Hit.RayT {
		
		protected HitBall(Ray ray, double t) {
			super(ray, t);
		}
		
		@Override
		public Vec3 n() {
			return ray().at(t()).sub(c());
		}
		
		@Override
		public Vec3 n_() {
			return n().div(r);
		}
		
		@Override
		public Vector uv() {
			Vec3 n = n();
			return new Vector(
					Numeric.atan2T(n.z(), n.x()), // [0..1]
					4 * Numeric.asinT(n.y() / r)  // [-1..1]
			);
		}
		
		@Override
		public Material material() {
			return mapMaterial.at(uv());
		}
	}
	
}
