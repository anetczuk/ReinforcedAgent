/*
Copyright 2007 Brian Tanner
http://rl-library.googlecode.com/
brian@tannerpages.com
http://brian.tannerpages.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package agents;

import java.net.URL;

import org.rlcommunity.rlglue.codec.AgentInterface;
import org.rlcommunity.rlglue.codec.taskspec.TaskSpec;
import org.rlcommunity.rlglue.codec.taskspec.TaskSpecVRLGLUE3;
import org.rlcommunity.rlglue.codec.taskspec.ranges.AbstractRange;
import org.rlcommunity.rlglue.codec.taskspec.ranges.DoubleRange;
import org.rlcommunity.rlglue.codec.taskspec.ranges.IntRange;
import org.rlcommunity.rlglue.codec.types.Action;
import org.rlcommunity.rlglue.codec.types.Observation;

import rlVizLib.general.ParameterHolder;
import rlVizLib.general.hasVersionDetails;
import rlVizLib.messaging.NotAnRLVizMessageException;
import rlVizLib.messaging.agent.AgentMessageParser;
import rlVizLib.messaging.agent.AgentMessages;
import rlVizLib.messaging.agentShell.TaskSpecResponsePayload;
import rlVizLib.messaging.interfaces.HasAVisualizerInterface;
import rlVizLib.messaging.interfaces.HasImageInterface;
import rlVizLib.visualization.QueryableAgent;
import agents.general.AgentAction;
import agents.general.AgentFactory;
import agents.general.policy.PolicyControl;
import agents.general.policy.RandomPolicy;
import agents.general.policy.td.QLearn;
import agents.general.policy.td.Sarsa;
import agents.general.policy.td.SarsaLambda;
import agents.general.policy.td.WatkinsQ;
import agents.general.state.StateTranslator;
import agents.visual.AgentVisualizer;
import agents.visual.DataPrintMessage;
import agents.visual.ExploreMessage;
import agents.visual.LogStepMessage;

/**
 * Simple random agent that can do multidimensional continuous or discrete
 * actions.
 * @author btanner
 */
public class ReinforcedAgent implements AgentInterface, 
									  HasImageInterface, HasAVisualizerInterface, 
									  QueryableAgent {
	
	ParameterHolder params = null;
	
    private Action action = null;
    private PolicyControl agent = null;
    

    /**
     * Constructor called by RL-Glue. If not found, then calls default constructor.
     * @param p
     */
    public ReinforcedAgent(ParameterHolder p) {
    	params = p;
    }
    
    /**
     * Called by RL-Glue only when parametrised constructor not found.
     */
    public ReinforcedAgent() {
        this(getDefaultParameters());
    }

    /**
     * Called during experiment load
     */
    public void agent_init(String taskSpec) {
    	TaskSpec TSO = null;
        TSO = new TaskSpec(taskSpec);

        //Quick hack for Mario
        if (TSO.getVersionString().equals("Mario-v1")) {
            TaskSpecVRLGLUE3 hardCodedTaskSpec = new TaskSpecVRLGLUE3();
            hardCodedTaskSpec.setEpisodic();
            hardCodedTaskSpec.setDiscountFactor(1.0d);
            //Run
            hardCodedTaskSpec.addDiscreteAction(new IntRange(-1, 1));
            //Jump
            hardCodedTaskSpec.addDiscreteAction(new IntRange(0, 1));
            //Speed
            hardCodedTaskSpec.addDiscreteAction(new IntRange(0, 1));
            TSO = new TaskSpec(hardCodedTaskSpec);
        }
        
        {
        	System.out.println("task: " + TSO.getStringRepresentation() );
        	System.out.println("actions:");
            for (int i = 0; i < TSO.getNumDiscreteActionDims(); i++) {
                IntRange thisActionRange = TSO.getDiscreteActionRange(i);
                System.out.println("i[" + Integer.toString(i) + "]: " + thisActionRange.toString() );
            }
            for (int i = 0; i < TSO.getNumContinuousActionDims(); i++) {
                DoubleRange thisActionRange = TSO.getContinuousActionRange(i);
                System.out.println("d[" + Integer.toString(i) + "]: " + thisActionRange.toString() );
            }
        }


        //Do some checking on the ranges here so we don't feel bad if we crash later for not checking them.
        for (int i = 0; i < TSO.getNumDiscreteActionDims(); i++) {
            AbstractRange thisActionRange = TSO.getDiscreteActionRange(i);

            if (thisActionRange.hasSpecialMinStatus() || thisActionRange.hasSpecialMaxStatus()) {
                System.err.println("The random agent does not know how to deal with actions that are unbounded or unspecified ranges.");
            }
        }
        for (int i = 0; i < TSO.getNumContinuousActionDims(); i++) {
            AbstractRange thisActionRange = TSO.getContinuousActionRange(i);

            if (thisActionRange.hasSpecialMinStatus() || thisActionRange.hasSpecialMaxStatus()) {
                System.err.println("The random agent does not know how to deal with actions that are unbounded or unspecified ranges.");
            }
        }

        action = new Action(TSO.getNumDiscreteActionDims(), TSO.getNumContinuousActionDims());
                
        agent = Factory.createPolicy( TSO, params );
    }

	/**
     * Called when game begins.
     */
    public Action agent_start(Observation o) {
        //setRandomActions(action);
    	AgentAction a = agent.start( o );
    	a.toAction( action );
        return action;
    }

    /**
     * Make step action.
     */
    public Action agent_step(double stepReward, Observation o) {
    	//System.out.println("step: " + Double.toString(stepReward));
        //setRandomActions(action);
    	AgentAction a = agent.step( stepReward, o );
    	a.toAction( action );
        return action;
    }

    /**
     * Called when game ended - receives final reward.
     */
    public void agent_end(double reward) {
    	agent.end(reward);
    }

    public void agent_cleanup() {
    	System.out.println("cleanup");
    }

    public String agent_message(String theMessage) {
    	/// System.out.println("received message: " + theMessage);
    	
        AgentMessages theMessageObject;
        try {
            theMessageObject = AgentMessageParser.parseMessage(theMessage);
        } catch (NotAnRLVizMessageException e) {
            System.err.println("Someone sent random agent a message that wasn't RL-Viz compatible");
            return "I only respond to RL-Viz messages!";
        }

        if (theMessageObject.canHandleAutomatically(this)) {
            return theMessageObject.handleAutomatically(this);
        }
        
//        //If it wasn't handled automatically, maybe its a custom Mountain Car Message
//        if (theMessageObject.getTheMessageType() != rlVizLib.messaging.environment.EnvMessageType.kEnvCustom.id()) {        	
//        	System.err.println( getClass().getName() + ": unknown message: " + theMessageObject.getMessageTypeName() );
//        	return null;
//        }

        {
        	ExploreMessage.Response response = ExploreMessage.parse( theMessageObject );
	        if (response != null) {
	        	agent.setExplore( response.state );
	        	return null;
	        }
        }
        {
        	DataPrintMessage.Response response = DataPrintMessage.parse( theMessageObject );
        	if (response != null) {
        		agent.setPrintData( response.state );
        		return null;
        	}
        }
        {
        	LogStepMessage.Response response = LogStepMessage.parse( theMessageObject );
        	if (response != null) {
        		agent.setLogStep( response.state );
        		return null;
        	}
        }
        
        System.err.println( getClass().getName() + ": unknown message: " + theMessageObject.getMessageTypeName() );
        return null;
    }

    public URL getImageURL() {
    	URL res = getClass().getResource("/images/cl1.jpg");
    	return res;
    }
    
    @Override
    public double getValueForState(Observation arg0) {
    	System.out.println("asking for state value... " + arg0.toString());
    	return 0;
    }
    
	@Override
	public String getVisualizerClassName() {
		String name = AgentVisualizer.class.getName();
		//String can = AgentViz.class.getCanonicalName();
		//System.out.println("asking for visualizer class: " + name + " " + can );
		return name;
	}
	
    
    //====================================================
    
    
    /**
     * Determines whether this agent is compatible with a task and parameter set. 
     * 
     * @param P		the parameter set.
     * @param TaskSpecString	the task specification.
     * @return	a response.
     */
    public static TaskSpecResponsePayload isCompatible(ParameterHolder P, String taskSpec) {
//        TaskSpec theTSO = new TaskSpec(taskSpec);
//        
//        if (theTSO.getNumContinuousActionDims() > 0)  {
//            return new TaskSpecResponsePayload(true, "This agent does not support continuous actions.");
//        }
//        if (theTSO.getNumDiscreteObsDims() > 0)  {
//            return new TaskSpecResponsePayload(true, "This agent does not support discrete observations.");
//        }
        
    	/// compatible
        return new TaskSpecResponsePayload(false, "");
    }
    
    /**
     * Return a ParameterHolder object with the agent's default parameter settings.
     * Method is called from RL-Glue framework.
     *
     * @return a default ParameterHolder object.
     */
    public static ParameterHolder getDefaultParameters() {
		ParameterHolder p = Factory.initializeParams();
		rlVizLib.utilities.UtilityShop.setVersionDetails(p, new DetailsProvider());
		return p;
    }

//    public static void main(String[] args) {    	
//        AgentLoader L = new AgentLoader(new ReinforcedAgent());
//        L.run();
//    }

}


//=============================================================


/**
 * 
 * @author bob
 *
 */
class Factory {
	
	static final String agentTypeParam = "agent-type";

	static Factory policyFactory = new Factory();
	
    static {
    	/// initialize factories
    	AgentFactory.Registry.add( new RandomPolicy.Factory() );
    	AgentFactory.Registry.add( new Sarsa.Factory() );
    	AgentFactory.Registry.add( new QLearn.Factory() );
    	AgentFactory.Registry.add( new SarsaLambda.Factory() );
    	AgentFactory.Registry.add( new WatkinsQ.Factory() );
    	System.out.println("Initialize AgentType factories: " + AgentFactory.Registry.size());
    }
    
	public static ParameterHolder initializeParams() {
		ParameterHolder params = new ParameterHolder();
		//params.addIntegerParam( agentTypeParam, 2 );		/// QLearn
		params.addIntegerParam( agentTypeParam, 3 );		/// Sarsa-Lambda
		AgentFactory.Registry.initParams( params );
		return params;
	}
	
	public static PolicyControl createPolicy(TaskSpec tso, ParameterHolder params) {
		StateTranslator range = new StateTranslator( tso );
		
    	int agentType = params.getIntegerParam( agentTypeParam );
    	AgentFactory type = AgentFactory.Registry.get(agentType);
    	if (type == null) {
    		StringBuilder str = new StringBuilder();
    		str.append("Invalid agent type index[");
    		str.append( agentType );
    		str.append( "], valid range 0-" );
    		str.append( AgentFactory.Registry.size() );
    		System.out.println( str.toString() );
    		type = AgentFactory.Registry.defaultFactory();
    	}
    	
        System.out.println("translator: " + range.print());
        System.out.println("factory: " + type.toString() );
        
    	return type.create( range, params );
	}
	
}


//=============================================================


/**
 * This is a little helper class that fills in the details about this environment
 * for the fancy print outs in the visualizer application.
 * @author btanner
 */
class DetailsProvider implements hasVersionDetails {

    public String getName() {
        return "Reinforced Agent 1.0";
    }

    public String getShortName() {
        return "Reinforced Agent";
    }

    public String getAuthors() {
        return "Bob";
    }

    public String getInfoUrl() {
        return "-- no url --";
    }

    public String getDescription() {
        return "Can handle multi dimensional continuous states and one dimensional discrete actions.";
    }
}

