package net.pandoragames.far.ui.swing.component.listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * Notified when the search criteria have been substantially changed.
 * @author Olivier Wehner at 07.03.2008
 * <!-- copyright note --> 
 */
public class SearchBaseHasChangedListener extends ResetDispatcher implements ItemListener, PropertyChangeListener {
	/**
	 * {@inheritDoc}
	 */
	public void itemStateChanged(ItemEvent event) {
		dispatchEvent( event );
	}
	/**
	 * {@inheritDoc}
	 */	
	public void propertyChange(PropertyChangeEvent evt) {
		dispatchEvent( evt );			
	}	
}
