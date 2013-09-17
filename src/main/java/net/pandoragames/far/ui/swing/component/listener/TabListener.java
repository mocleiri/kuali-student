package net.pandoragames.far.ui.swing.component.listener;

/**
 * Interface for components on an 
 * {@link net.pandoragames.far.ui.swing.OperationTabPane OperationTabPane}.
 * Allows the component to get informed about changes of the
 * state of its tab.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public interface TabListener {

	/**
	 * Constant for listener registration representing any tab.
	 * A listener registered with this constant will be called for every tab event
	 * not matter what tab index. 
	 */
	public static final Integer ANYTAB = Integer.valueOf(-1);

	/**
	 * Called when the components tab is selected.
	 * @param event related event
	 */
	public void tabSelected(TabEvent event);
	
	/**
	 * Called when the components tab becomes unselected.
	 * @param event related event
	 */
	public void tabUnselected(TabEvent event);
	
	/**
	 * Called when the components tab is enabled.
	 */
	// public void tabEnabled();
	
	/**
	 * Called when the components tab becomes disabled.
	 */
	// public void tabDisabled();
}
