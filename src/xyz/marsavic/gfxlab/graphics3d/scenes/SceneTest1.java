package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Light;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;
import xyz.marsavic.gfxlab.graphics3d.solids.Group;
import xyz.marsavic.gfxlab.graphics3d.solids.HalfSpace;
import xyz.marsavic.utils.Numeric;

import java.util.Collections;

public class SceneTest1 extends Scene.Base {
	
	public SceneTest1() {
		Ball ball = Ball.cr(Vec3.xyz(0, 0, 4), 2,
				uv -> Numeric.mod(uv.dot(Vector.xy(5, 4))) < 0.2 ?
						Material.matte(Color.okhcl(uv.y(), 0.125, 0.75)) :
						Material.matte(0.1)
		);
		
		HalfSpace halfSpace = HalfSpace.pn(Vec3.xyz(0, -2, 0), Vec3.EY,
				uv -> Material.matte(uv.add(Vector.xy(0.05, 0.05)).mod().min() < 0.1 ? 0.8 : 1)
		);
		
		solid = Group.of(ball, halfSpace);
		
		Collections.addAll(lights(),
				Light.pc(Vec3.xyz(-3, 3, 2), Color.hsb(0, 0.5, 3)),
				Light.pc(Vec3.xyz(0, 0, 0), Color.gray(0.1))
		);
	}
}
