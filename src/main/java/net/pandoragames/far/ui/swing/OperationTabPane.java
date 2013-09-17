package net.pandoragames.far.ui.swing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.pandoragames.far.ui.model.Resetable;
import net.pandoragames.far.ui.swing.component.listener.TabEvent;
import net.pandoragames.far.ui.swing.component.listener.TabListener;

/**
 * Adds some special behaviour to the JTabbedPane class. This component
 * implements the {@link net.pandoragames.far.ui.model.Resetable Resetable} interface
 * and the setEnabled() method will enable/disable all tabs.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class OperationTabPane extends JTabbedPane implements Resetable {

	private Map<Integer, List<TabListener>> listenerMap = new HashMap<Integer, List<TabListener>>();
	private Integer lastSelectedTab = null;
	
	public OperationTabPane() {
		this.addChangeListener( new TabChangedListener() );
	}

	/**
	 * Adds a tab listener that listens for events on the indicated tab
	 * or to all tabs if index is negative. 
	 * @param index tab index or -1 for all.
	 * @param listener to be added
	 */
	public void addTabListener(int index, TabListener listener) {
		if(listener == null) return;
		Integer key = Integer.valueOf( index );
		if( index < 0 ) key = TabListener.ANYTAB;
		if(! listenerMap.containsKey(key) ) listenerMap.put(key, new ArrayList<TabListener>());
		List<TabListener> listenerList = listenerMap.get(key);
		listenerList.add( listener );
	}
	
	/**
	 * Selects the first tab and disables all others.
	 */
	public void reset() {
		if( this.getSelectedIndex() > 0 ) {
			this.setSelectedIndex( 0 );
		}
		for(int i = 1; i < this.getTabCount(); i++) {
			this.setEnabledAt(i, false);
		}
	}

	/**
	 * Enables or disables the single tabs of this tab pane rather
	 * than the tab pane itself.
	 */
	public void setEnabled(boolean enabled) {
		for(int i = 0; i < this.getTabCount(); i++) {
			this.setEnabledAt(i, enabled);
		}		
		super.setEnabled(enabled);
	}
		
	private void fireTabEvent(Integer key, TabEvent event) {
		if( listenerMap.containsKey( key ) ) {
			List<TabListener> listenerList = listenerMap.get(key);
			for(TabListener listener : listenerList) {
				switch(event.getEventType()) {
					case SELECTED : listener.tabSelected(event); break;
					case UNSELECTED : listener.tabUnselected(event); break;
					default : ;
				}
			}
		}
	}
	
	class TabChangedListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			OperationTabPane otp = (OperationTabPane) e.getSource();
			int selectedTab = otp.getSelectedIndex();
			if(lastSelectedTab != null) {
				TabEvent unselect = new TabEvent( OperationTabPane.this, lastSelectedTab.intValue(), TabEvent.TAB_EVENT_TYPE.UNSELECTED);
				fireTabEvent( lastSelectedTab, unselect );
				fireTabEvent( TabListener.ANYTAB, unselect );					
			}
			TabEvent select = new TabEvent( OperationTabPane.this, selectedTab, TabEvent.TAB_EVENT_TYPE.SELECTED);
			fireTabEvent( selectedTab, select );
			fireTabEvent( TabListener.ANYTAB, select );	
			lastSelectedTab = selectedTab;
		}
	}
	
}
