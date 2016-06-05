/**
 * 
 */
package agents.general.state;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.rlcommunity.rlglue.codec.taskspec.TaskSpec;
import org.rlcommunity.rlglue.codec.taskspec.ranges.DoubleRange;
import org.rlcommunity.rlglue.codec.types.Observation;

/**
 * @author bob
 *
 */
public class StateTranslator {
	
	public RangeTranslator observation[] = null;
	int totalObservationStates = 1;
	
	public int actionsSize;
	
	
	public StateTranslator(TaskSpec tso) {
		final int obsNum = tso.getNumContinuousObsDims();
		observation = new RangeTranslator[obsNum];
		int iter = 0;
		if (iter < obsNum) {
			/// cart pos
			DoubleRange range = tso.getContinuousObservationRange(iter);
			observation[iter] = new RangeTranslator( 10, range);
			++iter;
		}
		if (iter < obsNum) {
			/// cart speed
			DoubleRange range = tso.getContinuousObservationRange(iter);
			observation[iter] = new RangeTranslator( 10, range);
			++iter;
		}
		if (iter < obsNum) {
			/// pole pos
			DoubleRange range = tso.getContinuousObservationRange(iter);
			observation[iter] = new RangeTranslator( 10, range);
			++iter;
		}
		if (iter < obsNum) {
			/// pole speed
			DoubleRange range = tso.getContinuousObservationRange(iter);
			observation[iter] = new RangeTranslator( 6, range);
			++iter;
		}
		final int bNum = 10;
		for(; iter<obsNum; ++iter) {
			DoubleRange range = tso.getContinuousObservationRange(iter);
			observation[iter] = new RangeTranslator( bNum, range);
		}
		
		for(int i=0; i<obsNum; ++i) {
			totalObservationStates *= observation[i].size;
		}
		
		actionsSize = tso.getDiscreteActionRange(0).getRangeSize();
	}

	public int totalObservations() {
		return totalObservationStates;
	}
	
	public DiscreteState convert(Observation o) {
		final int tSize = observation.length;
		
		DiscreteState state = new DiscreteState( tSize );
		for(int i=0; i<tSize; ++i) {
			double obsVal = o.getDouble(i);
			RangeTranslator trans = observation[i];
			state.value[i] = trans.translate( obsVal );
		}

		return state;
	}

	public boolean increase(DiscreteState state) {
		final int tSize = observation.length;
		for(int i=0; i<tSize; ++i) {
			int sSize = observation[i].size;
			++state.value[i];
			if (state.value[i] < sSize) {
				return true;
			}
			state.value[i] = 0;
		}
		/**
		 * Loop terminated. All states set to zero.
		 * No more states.
		 */
		// 
		return false;
	}

	public void store(String filename) {
		FileWriter fstream = null;
		BufferedWriter out = null;
        try {
			final File file = new File( filename );
			final File parent_directory = file.getParentFile();
			if (null != parent_directory) {
			    parent_directory.mkdirs();
			}
			fstream = new FileWriter(file);
        	out = new BufferedWriter(fstream);

			store(out);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//Close the output stream
			try {
				out.close();
				fstream.close();
			} catch (IOException e) {
			}
		}
	}

	private void store(BufferedWriter out) throws IOException {
		out.write("# ");		
		final int tSize = observation.length;
		for(int i=0; i<tSize; ++i) {
			RangeTranslator trans = observation[i];
			out.write( Integer.toString(trans.size) );
			out.write( " " );
		}
		out.write( " " );
		out.write( Integer.toString(actionsSize) );
		out.write( "\n" );
	}
	
	public String print() {
		StringBuilder builder = new StringBuilder();
		
		final int tSize = observation.length;
		for(int i=0; i<tSize; ++i) {
			RangeTranslator trans = observation[i];
			builder.append( Integer.toString(trans.size) );
			builder.append( " " );
		}
		
		builder.append( "(" );
		builder.append( Integer.toString( totalObservationStates ) );
		builder.append( "), " );
		
		builder.append( Integer.toString( actionsSize ) );
		
		return builder.toString();
	}
	
	
	//=======================================================
	
	
	public static class RangeTranslator {
		
		public DoubleRange range;
		public int size;

		
		public RangeTranslator(int statesNum, DoubleRange doubleRange) {
			range = doubleRange;
			size = statesNum;
		}
		
		public RangeTranslator(int statesNum, double min, double max) {
			this(statesNum, new DoubleRange(min, max));
		}

		public int translate(double value) {
			double ref = value - range.getMin();
			double factor = ref / range.getRangeSize();
			return (int) (size * factor);
		}
	}

}
