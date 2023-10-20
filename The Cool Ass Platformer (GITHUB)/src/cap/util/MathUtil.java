package cap.util;

public class MathUtil
{
	
	public static final int Coarse(int value, int coarse) { return Math.floorDiv(value, coarse) * coarse; }
	public static final int FastCoarse(int value, int bitmask) { return value & (~bitmask); }
	
}
