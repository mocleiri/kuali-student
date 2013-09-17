package net.pandoragames.far.ui.swing.component;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import net.pandoragames.far.ui.model.TargetFile;
import net.pandoragames.far.ui.swing.SwingConfig;

/**
 * Base functionality for the cell renderer of the file list table.
 * @author Olivier Wehner at 04.03.2008
 * <!-- copyright note --> 
 */
public class TargetFileListTableCellRenderer extends DefaultTableCellRenderer
{
	/**
	 * {@inheritDoc}
	 */
	public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) 
	{
		JLabel comp = (JLabel) super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
		FileSetTableModel tableModel = ( FileSetTableModel ) table.getModel();
		decorate( comp, tableModel.getRow(row), value );
		if( isSelected && ! hasFocus ) {
			comp.setForeground( table.getSelectionForeground() );
			comp.setBackground( table.getSelectionBackground() );
		}
		return comp;
	}
	
	/**
	 * Basic renderer functionality, handy classes for inheritors.
	 * @param renderer the actual cell renderer
	 * @param row current row object
	 * @param cellValue curren cell value
	 */
	protected void decorate(JLabel renderer, TargetFile row, Object cellValue) {
		if( ! row.isSelected() ) {
			renderer.setForeground( Color.GRAY );
		} else {
			renderer.setForeground( Color.BLACK );
		}
		if(! row.isIncluded() ) {
			renderer.setBackground( SwingConfig.GRAY_EXTRA_LIGHT );
		} else {
			renderer.setBackground( Color.WHITE );			
		}
	}
}
