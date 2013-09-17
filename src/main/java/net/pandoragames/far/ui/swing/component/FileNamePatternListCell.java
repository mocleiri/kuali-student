package net.pandoragames.far.ui.swing.component;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import net.pandoragames.far.ui.model.FileNamePattern;

/**
 * ListCellRenderer implementation for the file name column.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class FileNamePatternListCell implements ListCellRenderer //, ComboBoxEditor
{
	
	private FileNamePattern fileNamePattern;
	private BasicComboBoxEditor editor = new BasicComboBoxEditor();
	private BasicComboBoxRenderer renderer = new BasicComboBoxRenderer();

	/**
	 * {@inheritDoc}
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return renderer.getListCellRendererComponent(list, value.toString(), index, isSelected, cellHasFocus);
	}


	
	/**
	 * @see javax.swing.plaf.basic.BasicComboBoxEditor#getItem()
	 */
	public Object getItem()
	{
		return fileNamePattern;
	}

	/**
	 * @see javax.swing.plaf.basic.BasicComboBoxEditor#setItem(java.lang.Object)
	 */
	public void setItem(Object anObject)
	{
		fileNamePattern = (FileNamePattern) anObject;
		editor.setItem( fileNamePattern.getPattern() );
	}



	/**
	 * @see javax.swing.plaf.basic.BasicComboBoxEditor#addActionListener(java.awt.event.ActionListener)
	 */
	public void addActionListener(ActionListener l)
	{
		editor.addActionListener(l);
	}



	/**
	 * @see javax.swing.plaf.basic.BasicComboBoxEditor#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e)
	{
		editor.focusGained(e);
	}



	/**
	 * @see javax.swing.plaf.basic.BasicComboBoxEditor#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e)
	{
		editor.focusLost(e);
	}



	/**
	 * @see javax.swing.plaf.basic.BasicComboBoxEditor#getEditorComponent()
	 */
	public Component getEditorComponent()
	{
		return editor.getEditorComponent();
	}



	/**
	 * @see javax.swing.plaf.basic.BasicComboBoxEditor#removeActionListener(java.awt.event.ActionListener)
	 */
	public void removeActionListener(ActionListener l)
	{
		editor.removeActionListener(l);
	}



	/**
	 * @see javax.swing.plaf.basic.BasicComboBoxEditor#selectAll()
	 */
	public void selectAll()
	{
		editor.selectAll();
	}
	
	
}
