package xyz.marsavic.gfxlab.graphics3d.raytracers;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.ColorFunctionT;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Hit;
import xyz.marsavic.gfxlab.graphics3d.Ray;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;
import xyz.marsavic.gfxlab.graphics3d.solids.HalfSpace;


public class RayTracerTest implements ColorFunctionT {
	
	Ball ball = Ball.cr(Vec3.xyz(0, 0, 3), 2);
	HalfSpace halfSpace = HalfSpace.pn(Vec3.xyz(0, -2, 0), Vec3.EY);
			
	@Override
	public Color at(double t, Vector p) {
		Ray ray = Ray.pd(Vec3.ZERO, Vec3.zp(1, p));
		
		Hit hit1 = ball.firstHit(ray);
		Hit hit2 = halfSpace.firstHit(ray);
		
		double d = Math.min(hit1.t(), hit2.t());
		return Color.gray(1 / (1 + d));
	}
}
