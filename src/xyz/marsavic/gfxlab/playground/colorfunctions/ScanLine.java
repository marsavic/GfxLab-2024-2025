package xyz.marsavic.gfxlab.playground.colorfunctions;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.ColorFunctionT;


public class ScanLine implements ColorFunctionT {
	
	@Override
	public Color at(double t, Vector p) {
		int k = Math.floorMod((long) (16 * t), 640);
		return (p.xInt() == k || p.yInt() == k) ? Color.WHITE : Color.BLACK;
	}
	
}
