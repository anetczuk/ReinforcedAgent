/**
 * 
 */
package agents.general.math;

/**
 * @author bob
 *
 */
public class RangedAVG implements AVG {

	int rangeSize;
	/// StdAVG avg = null;
	CircularBuffer values = null;
	
	
	public RangedAVG(int size) {
		rangeSize = size;
		/// avg = new StdAVG(size);
		values = new CircularBuffer();
	}

	public RangedAVG(int size, double avgVal) {
		rangeSize = size;
		/// avg = new StdAVG(size, avgVal);
		
		values = new CircularBuffer();
		for(int i=0; i<size; ++i) {
			values.put(avgVal);
		}
	}

	public int size() {
		return values.size();
	}
	
	public double value() {
		return calculate();
		
//		if ( avg.value > 10E6 ) {
//			avg.value = calculate();
//		}
//		return avg.value;
	}
	
	public double calculate() {
		double ret = 0.0;
		int size = size();
		for(int i=0; i<size; ++i) {
			ret += (values.get(i) / size);
		}
		return ret;		
	}
	
	public void add(double newValues) {
		final int sizeDiff = rangeSize - values.size();
		if (sizeDiff > 0) {
			for(int i=0; i<sizeDiff; ++i) {
				values.put(newValues);
			}
			return ;
		}
		values.add(newValues);
		
//		double oldVal = values.add(newValues);
//		double diff = newValues - oldVal;
//		avg.changeFraction( diff );
	}
	
}
