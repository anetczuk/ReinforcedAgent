/**
 * 
 */
package agents.general.math.function;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import agents.general.math.Value;

/**
 * @author bob
 *
 */
public class DiscreteList<ValueType extends Value> {

	protected List<ValueType> values = new ArrayList<ValueType>();
	
	
//	public DiscreteList(final int elementSize) {
//		for( int i=0; i<elementSize; ++i) {
//			values.add( createElement() );
//		}
//	}
//	
//	protected abstract ValueType createElement();

	
	public int size() {
		return values.size();
	}
	
	public ValueType get(int index) {
		return values.get(index);
	}

	public void set(int action, double newVal) {
		Value val = values.get(action);
		val.set(newVal);
	}
	
	public void push(ValueType newValue) {
		values.add(newValue);
	}
	
	public void add(int index, double newValue) {
		ValueType val = values.get(index);
		val.add(newValue);
	}
	
	public Iterator<ValueType> iterator() {
		return values.iterator();
	}
	
	public double maxValue() {
		double best = values.get(0).value();
		for (int i = 1; i < values.size(); ++i) {
			double val = values.get(i).value();
			if (val > best) {
				best = val;
			}
		}
		return best;
	}

	public int bestByMaxValue() {
		int bestIndex = 0;
		double bestFactor = values.get(bestIndex).value();

		for (int i = 1; i < values.size(); ++i) {
			double val = values.get(i).value();
			if (val > bestFactor) {
				bestFactor = val;
				bestIndex = i;
			}
		}

		return bestIndex;
	}

	public double sum() {
		double ret = 0.0;
		for (int i = 0; i < values.size(); ++i) {
			ret += values.get(i).value();
		}
		return ret;
	}
	
	public int select(double rval) {
		double rem = rval;
		int vSize = values.size();
		for (int i = 0; i < vSize; ++i) {
			Value p = values.get(i);
			rem -= p.value();
			if (rem < 0.00001) {
				return i;
			}
		}
		return vSize - 1;
	}
	
	public String print() {
		StringBuilder builder = new StringBuilder();
		
		for( int i =0; i<values.size(); ++i) {
			builder.append( "[" );
			builder.append( Integer.toString(i) );
			builder.append( ", " );
			
			Value pair = values.get(i);
			builder.append( pair.print() );
			builder.append( "] " );
		}
		
		return builder.toString();
	}

	public void store(BufferedWriter out) throws IOException {
		int vSize = values.size();
		for (int i=0; i<vSize; ++i) {
			Value val = values.get(i);
			val.store(out);
			out.write( " " );
		}
	}
		
}
