package org.kuali.student.common.ui.client.widgets.list.impl;

import java.util.ArrayList;
import java.util.List;

import org.kuali.student.common.ui.client.widgets.KSCheckBox;
import org.kuali.student.common.ui.client.widgets.impl.KSDropDownImpl;
import org.kuali.student.common.ui.client.widgets.list.KSSelectItemWidgetAbstract;
import org.kuali.student.common.ui.client.widgets.list.ListItems;
import org.kuali.student.common.ui.client.widgets.list.ModelListItems;
import org.kuali.student.common.util.Callback;
import org.kuali.student.core.dto.Idable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;


/**
 * This is a description of what this class does - Will Gomes don't forget to fill this in. 
 * 
 * @author Kuali Student Team 
 *
 */
public class KSCheckBoxListImpl extends KSSelectItemWidgetAbstract implements ClickHandler{
    private FlexTable checkBoxes = new FlexTable();
    private List<String> selectedItems = new ArrayList<String>();

    private int maxCols = 1; //default max columns
    private boolean enabled = true;

    public KSCheckBoxListImpl() {
        initWidget(checkBoxes);
    }

    /**
     * @see org.kuali.student.common.ui.client.widgets.list.KSSelectItemWidgetAbstract#deSelectItem(java.lang.String)
     */
    public void deSelectItem(String id) {
        for (int i=0; i < checkBoxes.getRowCount(); i++){
            for (int j=0; j < checkBoxes.getCellCount(i); j++){
                KSCheckBox checkbox = (KSCheckBox)checkBoxes.getWidget(i, j);
                if (checkbox.getFormValue().equals(id)){
                    this.selectedItems.remove(id);
                    checkbox.setValue(false);
                    break;
                }
            }
        }		
    }

    /**
     * @see org.kuali.student.common.ui.client.widgets.list.KSSelectItemWidgetAbstract#getSelectedItems()
     */
    public List<String> getSelectedItems() {
        return selectedItems;
    }



    /**
     * @see org.kuali.student.common.ui.client.widgets.list.KSSelectItemWidgetAbstract#selectItem(java.lang.String)
     */
    public void selectItem(String id) {
        if (!selectedItems.contains(id)){
            for (int i=0; i < checkBoxes.getRowCount(); i++){
                for (int j=0; j < checkBoxes.getCellCount(i); j++){
                    KSCheckBox checkbox = (KSCheckBox)checkBoxes.getWidget(i, j);
                    if (checkbox.getFormValue().equals(id)){
                        this.selectedItems.add(id);
                        checkbox.setValue(true);
                        break;
                    }
                }
            }
        }
    }
    
    public void redraw(){
        checkBoxes.clear();
        int itemCount = super.getListItems().getItemCount();
        int currCount = 0;
        int row = 0;
        int col = 0;

        if (maxCols <= 2){
            //Row flow - increment row faster than column
            int maxRows = (itemCount / maxCols) + (itemCount % 2);
            for (String id:super.getListItems().getItemIds()){
                currCount++;
                row = (currCount % maxRows);
                row = ((row == 0) ? maxRows:row) - 1;
                
                checkBoxes.setWidget(row, col, createCheckbox(id));
                
                col += ((row + 1)/ maxRows) * 1;
            }
        } else {
            //Column flow - increment column faster than row
            for (String id:super.getListItems().getItemIds()){
                currCount++;
                col = currCount % maxCols;
                col = ((col == 0) ? maxCols:col) - 1;
                
                checkBoxes.setWidget(row, col, createCheckbox(id));
                
                row += ((col + 1 )/ maxCols) * 1;
            }
        }
    }

    @Override
    public <T extends Idable> void setListItems(ListItems listItems) {
        if(listItems instanceof ModelListItems){
            ((ModelListItems<T>)listItems).addOnAddCallback(new Callback<T>(){
    
                @Override 
                public void exec(T result){
                    KSCheckBoxListImpl.this.redraw();
                }
            });
            
            ((ModelListItems<T>)listItems).addOnRemoveCallback(new Callback<T>(){
    
                @Override 
                public void exec(T result){
                    KSCheckBoxListImpl.this.redraw();
                }
            });
            
            ((ModelListItems<T>)listItems).addOnUpdateCallback(new Callback<T>(){
    
                @Override 
                public void exec(T result){
                    KSCheckBoxListImpl.this.redraw();
                }
            });
        }
        
        super.setListItems(listItems);

        redraw();
    }

    private KSCheckBox createCheckbox(String id){
        KSCheckBox checkbox = new KSCheckBox(getListItems().getItemText(id));
        checkbox.setFormValue(id);
        checkbox.addClickHandler(this);
        return checkbox;
    }


    @Override
    public void onClick(ClickEvent event) {
        KSCheckBox checkbox = (KSCheckBox)(event.getSource());   
        String value = checkbox.getFormValue();
        if (checkbox.getValue()){
            if (!selectedItems.contains(value)){
                selectedItems.add(value);
            }
        } else {
            selectedItems.remove(value);
        }
        fireChangeEvent();
    }

    public void onLoad() {}

    /**
     * @see org.kuali.student.common.ui.client.widgets.list.KSSelectItemWidgetAbstract#setMaxColumns(int)
     */
    @Override
    public void setColumnSize(int col) {
        maxCols = col;
    }

    @Override
    public void setEnabled(boolean b) {
        enabled = b;
        for (int i=0; i < checkBoxes.getRowCount(); i++){
            for (int j=0; j < checkBoxes.getCellCount(i); j++){
                ((KSCheckBox)checkBoxes.getWidget(i, j)).setEnabled(b);
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
