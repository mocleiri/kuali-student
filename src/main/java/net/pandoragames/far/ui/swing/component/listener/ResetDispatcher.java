package net.pandoragames.far.ui.swing.component.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.text.JTextComponent;

import net.pandoragames.far.ui.model.Resetable;

/**
 * Listens to a RESET command and updates depending components.
 * @author Olivier Wehner at 03.03.2008
 * <!-- copyright note --> 
 */
public class ResetDispatcher implements ActionListener
{
	private List<Component> toBeDisabled = new ArrayList<Component>();
	private List<Component> toBeEnabled = new ArrayList<Component>();
	private List<JTextComponent> toBeCleared = new ArrayList<JTextComponent>();
	private List<Resetable> toBeReseted = new ArrayList<Resetable>();
	private List<AbstractButton> toBeSelected = new ArrayList<AbstractButton>();
	/**
	 * Adds a component that will be disabled when a RESET is issued
	 * @param c to be disabled
	 */
	public void addToBeDisabled(Component c) {
		if( c != null ) toBeDisabled.add( c );
	}
	/**
	 * Adds a component that will be enabled when a RESET is issued
	 * @param c to be enabled
	 */
	public void addToBeEnabled(Component c) {
		if( c != null ) toBeEnabled.add( c );
	}
	/**
	 * Adds a JTextComponent that will be cleared when a RESET is issued
	 * @param tc to be cleared
	 */
	public void addToBeCleared(JTextComponent tc) {
		if( tc != null ) toBeCleared.add( tc );
	}
	
	/**
	 * Adds a generic Resetable component.
	 * @param resetable to be called on RESET
	 */
	public void addResetable(Resetable resetable) {
		if( resetable != null ) toBeReseted.add( resetable );
	}
	/**
	 * Adds an "AbstractButton" (e.g. Checkbox) that will be selected when an RESET is issued.
	 * @param button to be selected on RESET
	 */
	public void addToBeSelected(AbstractButton button) {
		if(button != null ) toBeSelected.add( button );
	}
	/**
	 * Called when the search form shall be reseted.
	 * @param e fired by some component
	 */
	public void actionPerformed(ActionEvent e)
	{
		dispatchEvent( e );
	}
	
	/**
	 * Executes the respective operations on the registered componts
	 * @param event the original event.
	 */
	protected void dispatchEvent( EventObject event ) {
		for(Component c : toBeDisabled) {
			c.setEnabled( false );
		}
		for(Component c : toBeEnabled) {
			c.setEnabled( true );
		}
		for(JTextComponent tc : toBeCleared) {
			tc.setText("");
		}
		for(Resetable resetable : toBeReseted) {
			resetable.reset();
		}		
		for(AbstractButton button : toBeSelected) {
			button.setSelected( true );
		}		
	}

}
