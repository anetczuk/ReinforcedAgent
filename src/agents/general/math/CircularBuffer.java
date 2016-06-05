/**
 * 
 */
package agents.general.math;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bob
 *
 */
public class CircularBuffer {
	List<Double> queue = new ArrayList<Double>();
	
	
	CircularBuffer() {
	}
	
	CircularBuffer(int size) {
		//fill
		for(int i=0; i<size; ++i) {
			queue.add(0.0);
		}
	}
	
	public int size() {
		return queue.size();
	}
	
	public double get(int i) {
		return queue.get(i);
	}
	
	public void put(double val) {
		queue.add(val);
	}
	
	public double add(double val) {
//		if (queue.size() < qSize) {
//			//just add
//			queue.add(val);
//			return 0.0;
//		}
		
		//pop
		double rem = queue.remove(0);
		queue.add(val);
		return rem;
	}
	
}
