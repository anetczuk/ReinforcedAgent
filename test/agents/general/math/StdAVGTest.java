/**
 * 
 */
package agents.general.math;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author bob
 *
 */
public class StdAVGTest {

	@Test
	public void add() {
		StdAVG avg = new StdAVG();
		
		avg.add(1.0);
		assertEquals( 1.0, avg.value, 0.001 );
		
		avg.add(3.0);
		assertEquals( 2.0, avg.value, 0.001 );
		
		avg.add(5.0);
		assertEquals( 3.0, avg.value, 0.001 );
	}

	@Test
	public void add_max() {
		StdAVG avg = new StdAVG();
		
		final double halfMax = Double.MAX_VALUE / 2.0;
		
		avg.add( halfMax );
		assertEquals( halfMax, avg.value, halfMax*0.000000001 );
		
		avg.add( halfMax );
		assertEquals( halfMax, avg.value, halfMax*0.000000001 );
		
		avg.add( halfMax );
		assertEquals( halfMax, avg.value, halfMax*0.000000001 );
	}
	
}
