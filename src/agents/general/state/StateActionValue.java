/**
 * 
 */
package agents.general.state;

import agents.general.math.Value;

/**
 * @author bob
 * 
 * Action-value function.
 *
 */
public class StateActionValue extends StateActionFunction<Value> {
	
	public StateActionValue(int actionsSize) {
		super(actionsSize);
	}

	public StateActionValue(StateTranslator range) {
		super(range);
	}
	
	@Override
	protected Value createItem() {
		return new Value(1.0);
	}
		
}
