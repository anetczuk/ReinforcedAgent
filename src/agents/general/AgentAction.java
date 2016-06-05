/**
 * 
 */
package agents.general;

import org.rlcommunity.rlglue.codec.types.Action;

/**
 * @author bob
 *
 */
public class AgentAction {

	int action;
	
	
	public AgentAction() {
		action = 0;
	}

	public AgentAction(int anAction) {
		action = anAction;
	}

	public void toAction(Action action) {
		action.setInt(0, this.action);
	}
	
}
