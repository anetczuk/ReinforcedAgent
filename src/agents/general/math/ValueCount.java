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
public class ValueCount extends Value {
	public int count = 0;
	
	
	public ValueCount() {
		super(0.0);
	}
	
	public ValueCount(double value) {
		super(value);
		count = 1;
	}

	public int count() {
		return count;
	}
	
	public void set(double newValue) {
		super.set(newValue);
		++count;
	}
	
	public void add(double nextValue) {
		set( value + nextValue );
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
