/**
 * 
 */
package agents.general.math.function;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author bob
 *
 */
public abstract class DiscreteKey {

	public abstract String print();
	
	public abstract void store(BufferedWriter out) throws IOException;
	
}
