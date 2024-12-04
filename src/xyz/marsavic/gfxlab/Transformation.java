package xyz.marsavic.gfxlab;


import xyz.marsavic.functions.F1;
import xyz.marsavic.gfxlab.graphics3d.Ray;


public interface Transformation extends F1<Vec3, Vec3> {
	
	/** The default implementation is correct only for affine transformations!
	 * The correct way for general transformations would be to use the gradient. */
	default Ray at(Ray r) {
		return Ray.pq(at(r.p()), at(r.p().add(r.d())));
	}
	
	default Transformation then(Transformation outer) {
		return p -> outer.at(at(p));
	}
	
	
	// --------------------
	
	
	Transformation identity = p -> p;

	
	/** Returns a linear transformation which transforms unit vectors the same way as this transform. */
	default Affine linearize() {
		return Affine.unitVectors(
			at(Vec3.EX),
			at(Vec3.EY),
			at(Vec3.EZ)
		);
	}
	
	
	default Affine gradient(Vec3 p) {
		// ?
		final double eps = 0x1p-16;
		
		Vec3 a = at(p);
		return Affine.unitVectors(
				(at(p.add(Vec3.EX.mul(eps))).sub(a)).div(eps),
				(at(p.add(Vec3.EY.mul(eps))).sub(a)).div(eps),
				(at(p.add(Vec3.EZ.mul(eps))).sub(a)).div(eps)
		);
	}

	
	static Transformation chain(Transformation... transformations) {
		Transformation res = Transformation.identity;
		for (Transformation t : transformations) {
			res = res.then(t);
		}
		return res;
	}
	
	static Transformation chain(Affine... transformations) {
		Transformation res = Affine.IDENTITY;
		for (Transformation t : transformations) {
			res = res.then(t);
		}
		return res;
	}
}
