/**
 * 
 */
package agents.general.math;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author bob
 *
 */
public class StdAVG extends Value {
	public int count = 0;
	
	
	public StdAVG() {
		super(0.0);
	}

	public StdAVG(int size) {
		super(0.0);
		count = size;
	}
	
	public StdAVG(double value) {
		super(value);
		count = 1;
	}
	
	public StdAVG(int size, double avg) {
		super( avg );
		count = size;
	}

	public void set(double newValue) {
		super.set(newValue);
		count = 1;
	}
	
	public void add(double nextValue) {
		average(nextValue);
	}

	public void average(double nextValue) {
		value = value * count / (count+1);
		value += nextValue / (count+1);
		++count;
		
//		double newSum = value * count + nextValue;
//		++count;
//		value = newSum / count;	
	}
	
	public void changeValue(double change) {
		value += change;
	}
	
	public void changeFraction(double change) {
		value += (change / count);
	}
	
	public String print() {
		StringBuilder builder = new StringBuilder();

		builder.append( super.print() );
		builder.append( " " );
		builder.append( Integer.toString(count) );
		
		return builder.toString();		
	}
	
	public void store(BufferedWriter out) throws IOException {
		super.store(out);
		out.write( " " );
		out.write( Integer.toString(count) );
	}

}
