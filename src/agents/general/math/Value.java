/**
 * 
 */
package agents.general.math;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author bob
 *
 */
public class Value {

	protected double value = 0.0;
	
	
	public Value(double val) {
		value = val;
	}

	public double value() {
		return value;
	}
	
	public void set(double val) {
		value = val;
	}

	public void add(double val) {
		value += val;
	}
	
	public void multiply(double val) {
		value *= val;
	}

	public String print() {
		StringBuilder builder = new StringBuilder();

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		DecimalFormat format = new DecimalFormat("##.0000", symbols);
		
		String formatted = String.format("%7s", format.format(value));
		builder.append( formatted );
		
		return builder.toString();		
	}
	
	public void store(BufferedWriter out) throws IOException {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		DecimalFormat df = new DecimalFormat("##.0000", symbols);
		out.write( df.format(value) );
	}
}
