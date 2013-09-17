package net.pandoragames.far.ui.swing.component.listener;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Informs interested components about the state of the doBackup flag.
 * 
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class BackupFlagListener implements ItemListener {

	private List<Component> toBeToggled = new ArrayList<Component>();
	
	/**
	 * {@inheritDoc}
	 */
	public void itemStateChanged(ItemEvent event) {
		for(Component component: toBeToggled) {
			component.setEnabled( (ItemEvent.SELECTED == event.getStateChange()) );
		}
	}
	
	/**
	 * Adds a component that will be toggled according to the state of the doBackup flag.
	 * @param component to be added to the list
	 */
	public void addToBeToggled(Component component) {
		if( component != null ) {
			toBeToggled.add(component);
		}
	}
}
