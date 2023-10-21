package cap.util;

public class MathUtil
{
	
	public static final int Coarse(int value, int coarse) { return Math.floorDiv(value, coarse) * coarse; }
	public static final int FastCoarse(int value, int bitmask) { return value & (~bitmask); }
	
	public static final int LinearIncrease(int value, int target, int slope)
	{
		int diff = target - value;
		
		if (Math.abs(diff) < slope)	return target;
		else						return value + (int) Math.copySign(slope, diff);
	}
	
	public static final float LinearIncrease(float value, float target, float slope)
	{
		float diff = target - value;
		
		if (Math.abs(diff) < slope)	return target;
		else						return value + Math.copySign(slope, diff);
	}
	
}
