/**
 * 
 */
package agents.general;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import agents.general.state.StateTranslator.RangeTranslator;

/**
 * @author bob
 *
 */
public class RangeTranslatorTest {

	@Test
	public void translate_2() {
		RangeTranslator translator = new RangeTranslator(2, 0.0, 10.0 );
		
		assertEquals(0, translator.translate(0.0));
		assertEquals(0, translator.translate(4.9));
		
		assertEquals(1, translator.translate(5.1));
		assertEquals(1, translator.translate(9.9));
	}
	
	@Test
	public void translate_2_neg() {
		RangeTranslator translator = new RangeTranslator(2, -10.0, 10.0 );
		
		assertEquals(0, translator.translate(-9.0));
		assertEquals(0, translator.translate(-0.1));
		
		assertEquals(1, translator.translate( 0.1));
		assertEquals(1, translator.translate( 9.0));
	}
	
	@Test
	public void translate_3() {
		RangeTranslator translator = new RangeTranslator(3, 5.0, 10.0 );
		
		assertEquals(0, translator.translate(5.1));
		assertEquals(0, translator.translate(6.5));
		
		assertEquals(1, translator.translate(7.0));
		assertEquals(1, translator.translate(8.0));
		
		assertEquals(2, translator.translate(8.8));
		assertEquals(2, translator.translate(9.5));
	}
	
	@Test
	public void translate_3_neg() {
		RangeTranslator translator = new RangeTranslator(3, -3.0, 3.0 );
		
		assertEquals(0, translator.translate(-2.1));
		assertEquals(0, translator.translate(-1.1));
		
		assertEquals(1, translator.translate(-0.9));
		assertEquals(1, translator.translate( 0.9));
		
		assertEquals(2, translator.translate( 1.1));
		assertEquals(2, translator.translate( 2.5));
	}

}
