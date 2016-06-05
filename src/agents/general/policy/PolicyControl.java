/**
 * 
 */
package agents.general.policy;

import org.rlcommunity.rlglue.codec.types.Observation;

import agents.general.AgentAction;

/**
 * @author bob
 *
 */
public interface PolicyControl {	
	
	AgentAction start(Observation nextState);

	AgentAction step(double reward, Observation nextState);

	void end(double reward);

	void setExplore(boolean state);

	void setPrintData(boolean state);

	void setLogStep(boolean state);

}
