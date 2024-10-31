package xyz.marsavic.gfxlab.aggregation;

import xyz.marsavic.functions.A0;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.ColorFunction;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.gui.UtilsGL;
import xyz.marsavic.resources.Resource;
import xyz.marsavic.time.Profiler;
import xyz.marsavic.utils.Hashing;
import xyz.marsavic.utils.OnGC;
import xyz.marsavic.utils.Utils;


public class AggregatorFrameLast extends Aggregator {
	
	private final ColorFunction colorFunction;
	private final int nFrames;
	private final Vector sizeFrame;
	private final long seed;
	
	
	private record IFrameAggregate(int iFrame, Aggregate aggregate) {}
	
	private IFrameAggregate iFrameAggregateLast = null; // Must not be changed outside synchronized blocks.
	private final Profiler profilerLoop = UtilsGL.profiler(this, "add sample");
	private final A0 aStopLoop;
	
	
	
	public AggregatorFrameLast(ColorFunction colorFunction, Vec3 size, long seed) {
		this.colorFunction = colorFunction;
		nFrames = (int) size.x();
		sizeFrame = size.p12();
		this.seed = seed;
		
		changeActiveAggregate(0);
		
		aStopLoop = Utils.daemonLoop(this::addSample);
		OnGC.setOnGC(this, aStopLoop);
	}
	
	
	private synchronized IFrameAggregate changeActiveAggregate(int iFrame_) {
		IFrameAggregate iFrameAggregateLastOld = iFrameAggregateLast;
		iFrameAggregateLast = new IFrameAggregate(iFrame_, new Aggregate(colorFunction, iFrame_, sizeFrame, Hashing.mix(seed, iFrame_)));
		
		if (iFrameAggregateLastOld != null) {
			iFrameAggregateLastOld.aggregate.release();
		}
		
		return iFrameAggregateLast;
	}
	
	
	@Override
	public Resource<Matrix<Color>> rFrame(int iFrame) {
		int iFrame_ = Math.floorMod(iFrame, nFrames);
		
		IFrameAggregate iFrameAggregate;
		synchronized (this) {
			iFrameAggregate = (iFrame_ == iFrameAggregateLast.iFrame) ?
					iFrameAggregateLast :
					changeActiveAggregate(iFrame_);
		}
		
		return iFrameAggregate.aggregate.avgAtLeastOne();
	}
	
	
	private void addSample() {
		profilerLoop.measure(() -> {
			iFrameAggregateLast.aggregate.addSample();
			fireInvalidated(); // TODO Fire EventInvalidatedFrame instead
		});
	}
	
	
	
	@Override
	public synchronized void release() {
		aStopLoop.execute();
		iFrameAggregateLast.aggregate.release();
	}
}
