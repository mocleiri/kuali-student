package net.pandoragames.far.ui.swing.component.listener;

import java.util.EventObject;

import javax.swing.JTabbedPane;

/**
 * Event object for {@link TabListener TabListener}.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public class TabEvent extends EventObject {

	public enum TAB_EVENT_TYPE { 	/** Event type for a tab getting active.*/
									SELECTED, 
									/** Event type for a tab getting inactive.*/
									UNSELECTED };
	
	private int tabIndex;
	private TAB_EVENT_TYPE eventType;
	
	/**
	 * Counstructor.
	 * @param source JTabbedPane that triggered the event
	 * @param index index of affected tab
	 * @param type event type
	 */
	public TabEvent(JTabbedPane source, int index, TAB_EVENT_TYPE type) {
		super(source);
		tabIndex = index;
		eventType = type;
	}
	/**
	 * Returns the event type this object represents
	 * @return event type
	 */
	public TAB_EVENT_TYPE getEventType() {
		return eventType;
	}
	
	/**
	 * Returns the index of the affected tab.
	 * @return tab index
	 */
	public int getTabIndex() {
		return tabIndex;
	}
	
	
}
