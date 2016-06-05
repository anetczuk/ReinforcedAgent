/**
 * 
 */
package agents.general;

import static org.junit.Assert.*;

import org.junit.Test;

import agents.general.state.DiscreteState;

/**
 * @author bob
 *
 */
public class DiscreteStateTest {

	@Test
	public void equals_null() {
		DiscreteState state = new DiscreteState();
		
		assertNotEquals( state, null );
	}
	
	@Test
	public void equals_type() {
		DiscreteState state = new DiscreteState();
		
		assertNotEquals( state, new Integer(0) );
	}
	
	@Test
	public void equals_same_ref() {
		DiscreteState state = new DiscreteState();
		
		assertEquals( state, state );
	}
	
	@Test
	public void equals_same_value() {
		DiscreteState stateA = new DiscreteState();
		DiscreteState stateB = new DiscreteState();
		
		assertEquals( stateA, stateB );
	}
	
	@Test
	public void equals_diff_value() {
		DiscreteState stateA = new DiscreteState(1);
		DiscreteState stateB = new DiscreteState(1);
		stateB.value[0] = stateA.value[0] + 1;
		
		assertNotEquals(stateA, stateB );
	}
	
	@Test
	public void hashCode_same_ref() {
		DiscreteState state = new DiscreteState();
		
		assertEquals( state.hashCode(), state.hashCode() );
	}
	
	@Test
	public void hashCode_same_value_empty() {
		DiscreteState stateA = new DiscreteState();
		DiscreteState stateB = new DiscreteState();
		
		assertEquals( stateA.hashCode(), stateB.hashCode() );
	}
	
	@Test
	public void hashCode_same_value() {
		DiscreteState stateA = new DiscreteState(5);
		DiscreteState stateB = new DiscreteState(5);
		
		assertEquals( stateA.hashCode(), stateB.hashCode() );
	}
	
	@Test
	public void hashCode_diff_value() {
		DiscreteState stateA = new DiscreteState(3);
		DiscreteState stateB = new DiscreteState(3);
		stateB.value[0] = stateA.value[0] + 1;
		
		assertNotEquals(stateA.hashCode(), stateB.hashCode() );
	}

}
