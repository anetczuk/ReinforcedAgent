/**
 * 
 */
package agents.general.state;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import agents.general.math.function.DiscreteKey;


/**
 * @author bob
 *
 */
public class DiscreteState extends DiscreteKey {

	public int value[] = null;
	
	
	public DiscreteState() {
		value = new int[0];
	}
	
	public DiscreteState(int tSize) {
		value = new int[tSize];
	}

	public DiscreteState(DiscreteState state) {
		final int vSize = state.value.length;
		value = new int[vSize]; 
		for(int i=0; i<vSize; ++i) {
			value[i] = state.value[i];
		}
	}

	public void reset() {
		final int vSize = value.length;
		for(int i=0; i<vSize; ++i) {
			value[i] = 0;
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;
//		if (super.equals(object) == true) {
//			//the same reference
//			return true;
//		}
		
		/// different references - check content
		
		if (object instanceof DiscreteState == false) {
			return false;
		}
		
		DiscreteState typedOb = (DiscreteState) object;
		if (Arrays.equals(value, typedOb.value) == false) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( value );
	}

	@Override
	public String print() {
		StringBuilder builder = new StringBuilder();
		
		builder.append( "[" );
		final int vSize = value.length;
		for(int i=0; i<vSize; ++i) {
			builder.append( Integer.toString( value[i] ) );
			if (i+1<vSize) {
				//not last element
				builder.append( ", " );
			}
		}
		builder.append( "]" );
		
		return builder.toString();
	}

	@Override
	public void store(BufferedWriter out) throws IOException {
		final int vSize = value.length;
		for(int i=0; i<vSize; ++i) {
			out.write( Integer.toString( value[i] ) );
			if (i+1<vSize) {
				//not last element
				out.write( " " );
			}
		}
	}
}
