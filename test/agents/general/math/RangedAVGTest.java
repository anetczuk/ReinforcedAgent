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
public class RangedAVGTest {
	
	@Test
	public void constructor_reserve() {
		RangedAVG avg = new RangedAVG(10);
		assertEquals( 0.0, avg.value(), 0.001 );
	}
	
	@Test
	public void constructor_fill() {
		RangedAVG avg = new RangedAVG(10, 100);
		assertEquals( 100.0, avg.value(), 0.001 );
	}
	
	@Test
	public void add_fill() {
		RangedAVG avg = new RangedAVG(4);
		
		avg.add( 10.0 );
		assertEquals( 10.0, avg.value(), 0.001 );
	}
	
	@Test
	public void add_normal_0() {
		RangedAVG avg = new RangedAVG(4, 0.0);
		
		avg.add( 10.0 );
		assertEquals( 2.5, avg.value(), 0.001 );
		
		avg.add( 10.0 );
		assertEquals( 5.0, avg.value(), 0.001 );
		
		avg.add( 10.0 );
		assertEquals( 7.5, avg.value(), 0.001 );
	}
	
	@Test
	public void add_normal_10() {
		RangedAVG avg = new RangedAVG(4, 10);
		
		avg.add( 10.0 );
		assertEquals( 10.0, avg.value(), 0.001 );
		
		avg.add( 0.0 );
		assertEquals( 7.5, avg.value(), 0.001 );
		
		avg.add( 0.0 );
		assertEquals( 5.0, avg.value(), 0.001 );
		
		avg.add( 0.0 );
		assertEquals( 2.5, avg.value(), 0.001 );
	}
	
	@Test
	public void add_max_simple() {
		final double halfMax = Double.MAX_VALUE / 2.0;
		
		RangedAVG avg = new RangedAVG(2, halfMax);
		
		avg.add( halfMax );
		assertEquals( halfMax, avg.value(), halfMax*0.0000000001 );
		
		avg.add( 0.0 );
		assertEquals( halfMax/2.0, avg.value(), halfMax*0.0000000001 );
	}
	
	@Test
	public void add_max() {
		final double halfMax = Double.MAX_VALUE / 2.0;
		
		RangedAVG avg = new RangedAVG(10, halfMax);
		
		for(int i=0; i<avg.size(); ++i)
			avg.add( 10.0 );

		assertEquals( 10.0, avg.value(), 0.0001 );
	}

}
