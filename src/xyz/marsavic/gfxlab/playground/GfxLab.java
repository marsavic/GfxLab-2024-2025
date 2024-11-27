package xyz.marsavic.gfxlab.playground;

import xyz.marsavic.elements.HasOutput;
import xyz.marsavic.functions.F1;
import xyz.marsavic.gfxlab.*;
import xyz.marsavic.gfxlab.aggregation.AggregatorFrameLast;
import xyz.marsavic.gfxlab.aggregation.AggregatorOnDemand;
import xyz.marsavic.gfxlab.aggregation.EAggregator;
import xyz.marsavic.gfxlab.graphics3d.raytracers.RayTracerSimple;
import xyz.marsavic.gfxlab.graphics3d.scenes.SceneTest1;
import xyz.marsavic.gfxlab.gui.UtilsGL;
import xyz.marsavic.gfxlab.playground.colorfunctions.Blobs;
import xyz.marsavic.gfxlab.tonemapping.ColorTransform;
import xyz.marsavic.gfxlab.tonemapping.colortransforms.Identity;
import xyz.marsavic.gfxlab.tonemapping.matrixcolor_to_colortransforms.AutoSoft;
import xyz.marsavic.resources.Resource;

import static xyz.marsavic.elements.ElementF.e;


public class GfxLab {
	
	public HasOutput<F1<Resource<Matrix<Integer>>, Integer>> sink;
	
	
	public GfxLab() {
//		setup2D();
		setupRaytracing();
	}
	
	
	private void setupRaytracing() {
		//                       nFrames   width     height
		var eSize = e(Vec3::new, e(1.0), e(640.0), e(640.0));
		
		sink =
				e(Fs::frFrameToneMapping,
						new EAggregator(
								e(AggregatorFrameLast::new),
								e(Fs::transformedColorFunction,
										e(RayTracerSimple::new,
												e(SceneTest1::new)												
										),
										e(TransformationsFromSize.toGeometric, eSize)
								),
								eSize,
								e(0xA6A08E5C173D29FL)
						),
						e(Fs::frToneMapping,
//								e(ColorTransform::asColorTransformFromMatrixColor, e(new Identity()))
								e(AutoSoft::new, e(0x1p-4), e(1.0))
						)
				);
	}
	
	
	private void setup2D() {
		//                       nFrames   width     height
		var eSize = e(Vec3::new, e(640.0), e(640.0), e(640.0));
		
		sink =
				e(Fs::frFrameToneMapping,
						new EAggregator(
								e(AggregatorOnDemand::new),
								e(Fs::transformedColorFunction,
//										e(ColorFunctionExample::new),
//										e(Gradient::new),
//										e(OkLab::new),

//										e(ScanLine::new),
//										e(GammaTest::new),

//										e(Spirals::new),
										e(Blobs::new, e(5), e(0.1), e(0.2)),

//										e(TransformationsFromSize.toUnitBox, eSize)
//										e(TransformationsFromSize.toIdentity, eSize)
										e(TransformationsFromSize.toGeometric, eSize)
								),
								eSize,
								e(0xA6A08E5C173D29FL)
						),
						e(Fs::frToneMapping,
								e(ColorTransform::asColorTransformFromMatrixColor, e(new Identity()))
//								e(AutoSoft::new, e(0x1p-4), e(1.0))
						)
				);
	}
	
}


class Fs {
	
	public static ColorFunction transformedColorFunction(ColorFunction colorFunction, Transformation transformation) {
		return p -> colorFunction.at(transformation.at(p));
	}
	
	public static F1<Resource<Matrix<Integer>>, Integer> frFrameToneMapping(
			F1<Resource<Matrix<Color>>, Integer> frFrame,
			F1<Resource<Matrix<Integer>>, Resource<Matrix<Color>>> frToneMapping
	) {
		return iFrame -> frToneMapping.at(frFrame.at(iFrame));
	}
	
	
	// Contract: When a resource is a parameter of a "pure" function, that means it will be released (consumed) inside the function.
	// TODO Better: Only pass a resource inside an encapsulating method (like f and a, but it calls passed function with the resource), and do reference counting
	//  eg:
	//      var result = resource.do(r -> function(r));
	//

	public static F1<Resource<Matrix<Integer>>, Resource<Matrix<Color>>> frToneMapping(
			F1<ColorTransform, Matrix<Color>> f_ColorTransform_MatrixColor
	) {
		return input -> {
			var r = input.f(mC -> {
				ColorTransform f = f_ColorTransform_MatrixColor.at(mC);
				var rMatI = UtilsGL.matricesInt.borrow(mC.size(), true);
				rMatI.a(mI -> mI.fill((x, y) -> f.at(mC.get(x, y)).code()));
				return rMatI;
			});
			input.release(); // CONSUMING INPUT!!!
			return r;
		};
	}

}
