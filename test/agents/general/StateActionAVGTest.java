/**
 * 
 */
package agents.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import agents.general.math.StdAVG;
import agents.general.math.Value;
import agents.general.math.function.DiscreteList;
import agents.general.state.DiscreteState;
import agents.general.state.StateActionValue;


/**
 * @author bob
 *
 */
public class StateActionAVGTest {

	@Test
	public void pair_average_zero() {
		StdAVG pair = new StdAVG();
		assertEquals(0.0, pair.value(), 0.00001 );
		
		pair.average(1.0);
		assertEquals(1.0, pair.value(), 0.00001 );
		
		pair.average(5.0);
		assertEquals(3.0, pair.value(), 0.00001 );
		
		pair.average(0.0);
		assertEquals(2.0, pair.value(), 0.00001 );
	}
	
	@Test
	public void pair_average_one() {
		StdAVG pair = new StdAVG(1.0);
		assertEquals(1.0, pair.value(), 0.00001 );
		
		pair.average(1.0);
		assertEquals(1.0, pair.value(), 0.00001 );
		
		pair.average(4.0);
		assertEquals(2.0, pair.value(), 0.00001 );
		
		pair.average(0.0);
		assertEquals(1.5, pair.value(), 0.00001 );
	}
	
	@Test
	public void size_default() {
		StateActionValue function = new StateActionValue(2);
		assertEquals(0, function.size() );
	}
	
	@Test
	public void size_same_reference() {
		StateActionValue function = new StateActionValue(2);
		
		DiscreteState state = new DiscreteState();
		function.getActions(state);
		assertEquals(1, function.size() );
		
		function.getActions(state);
		assertEquals(1, function.size() );
	}
	
	@Test
	public void size_same_state() {
		StateActionValue function = new StateActionValue(2);
		
		DiscreteState stateA = new DiscreteState();
		function.getActions(stateA);
		assertEquals(1, function.size() );
		
		DiscreteState stateB = new DiscreteState();
		function.getActions(stateB);
		assertEquals(1, function.size() );
	}

	@Test
	public void getActions_nullState() {
		StateActionValue values = new StateActionValue(2);
		
		DiscreteList<Value> actions = values.getActions(null);
		assertNotNull( actions );
	}
	
}
