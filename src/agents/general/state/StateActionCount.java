/**
 * 
 */
package agents.general.state;

import agents.general.math.ValueCount;

/**
 * @author bob
 * 
 * Action-value function.
 *
 */
public class StateActionCount extends StateActionFunction<ValueCount> {
	
	public StateActionCount(int actionsSize) {
		super(actionsSize);
	}

	public StateActionCount(StateTranslator range) {
		super(range);
	}
	
	@Override
	protected ValueCount createItem() {
		return new ValueCount(1.0);
	}
		
}
