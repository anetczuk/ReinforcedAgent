/**
 * 
 */
package agents.general.policy.td;

import agents.general.math.Value;
import agents.general.state.DiscreteFunction2D;
import agents.general.state.DiscreteState;

/**
 * @author bob
 *
 */
public class EligibilityTrace extends DiscreteFunction2D<ETValue> {
	
	public EligibilityTrace(int actionsSize) {
		super( actionsSize );
	}

	public void update(DiscreteState state, int action) {
		add(state, action, 1.0);
	}

	@Override
	protected ETValue createItem() {
		return new ETValue();
	}
	
}

/**
 * 
 * @author bob
 *
 */
class ETValue extends Value {
	
	public ETValue() {
		super(0.0);
	}


	public void increase() {
		add(1.0);
	}
	
}
