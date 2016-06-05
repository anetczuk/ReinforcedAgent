/**
 * 
 */
package agents.visual;

import java.awt.Checkbox;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import rlVizLib.general.TinyGlue;
import rlVizLib.visualization.AbstractVisualizer;
import rlVizLib.visualization.interfaces.DynamicControlTarget;

/**
 * @author bob
 *
 */
public class AgentVisualizer extends AbstractVisualizer {

	//This is a little interface that will let us dump controls to a panel somewhere.
	DynamicControlTarget theControlTarget = null;
	private Vector<Component> controls = null;
	
	
    public AgentVisualizer(TinyGlue glueState, DynamicControlTarget theControlTarget) {
        super();
        this.theControlTarget = theControlTarget;
        setupVizComponents();
    }

    protected void setupVizComponents() {
    	System.out.println("AgentViz.setupVizComponents init");
    	
    	
    	controls = new Vector<Component>();
    	/// controls.add( new Label("xxx") );
    	{
    		Checkbox box = new Checkbox("Explore?", true);
    		
			box.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					final boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
					//System.out.println("Explore: " + Boolean.toString( selected ) );
					ExploreMessage.send( selected );
				}
			});
    		controls.add( box );
    	}
    	{
    		Checkbox box = new Checkbox("Output data?", true);
    		
    		box.addItemListener(new ItemListener() {
    			public void itemStateChanged(ItemEvent e) {
    				final boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
    				//System.out.println("Explore: " + Boolean.toString( selected ) );
    				DataPrintMessage.send( selected );
    			}
    		});
    		controls.add( box );
    	}
    	{
    		Checkbox box = new Checkbox("Log step?", false);
    		//box.addLis
    		
    		box.addItemListener(new ItemListener() {
    			public void itemStateChanged(ItemEvent e) {
    				final boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
    				//System.out.println("Explore: " + Boolean.toString( selected ) );
    				LogStepMessage.send( selected );
    			}
    		});
    		controls.add( box );
    	}
    	
		theControlTarget.addControls(controls);
    	
    	
        //theValueFunction = new ValueFunctionVizComponent(this, theControlTarget, this.glueState);
        //theAgentOnValueFunction = new AgentOnValueFunctionVizComponent(this, this.glueState);
    	
//        SelfUpdatingVizComponent mountain = new MountainVizComponent(this);
//        SelfUpdatingVizComponent carOnMountain = new CarOnMountainVizComponent(this);
//        SelfUpdatingVizComponent scoreComponent = new GenericScoreComponent(this);

//        super.addVizComponentAtPositionWithSize(theValueFunction, 0, .5, 1.0, .5);
//        super.addVizComponentAtPositionWithSize(theAgentOnValueFunction, 0, .5, 1.0, .5);
//
//        super.addVizComponentAtPositionWithSize(mountain, 0, 0, 1.0, .5);
//        super.addVizComponentAtPositionWithSize(carOnMountain, 0, 0, 1.0, .5);
//        super.addVizComponentAtPositionWithSize(scoreComponent, 0, 0, 1.0, 1.0);
    }
    
	@Override
	public String getName() {
		return "Cart-Pole Agent";
	}
	
}
