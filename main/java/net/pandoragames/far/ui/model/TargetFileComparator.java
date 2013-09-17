package net.pandoragames.far.ui.model;

import java.util.Comparator;

/**
 * Comparator for TargetFile objects. Allows sorting by name and sorting by path.
 *
 * @author Olivier Wehner at 02/12/2009
 * <!-- copyright note -->
 */
public class TargetFileComparator implements Comparator<TargetFile> {

	private static final int BYPATH = 0;
	private static final int BYNAME = 1;
	
	private int orderCriterion;
	private boolean invert;
	
	/* 
	 * Use one of the static methods to get an instance.
	 */
	private TargetFileComparator(int byWhat) {
		orderCriterion = byWhat;
	}
	
	/**
	 * Returns a comparator that sorts TargetFile objects by name.
	 * @return comparator to sort by name
	 */
	public static TargetFileComparator orderByName() {
		return new TargetFileComparator( BYNAME );
	}
	
	/**
	 * Returns a comparator that sorts TargetFile objects by path.
	 * @return comparator to sort by path
	 */
	public static TargetFileComparator orderByPath() {
		return new TargetFileComparator( BYPATH );
	}
	
	/**
	 * Invert the effect of the current sort criterion (asc vs. desc).
	 */
	public void invert() {
		invert = !invert;
	}
	
	/**
	 * Returns true if the current sort criterion
	 * is "by name".
	 * @return true if sort "by name"
	 */
	public boolean isOrderByName() {
		return orderCriterion == BYNAME;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int compare(TargetFile file1, TargetFile file2) {
		if( orderCriterion == BYNAME ) {
			int result = file1.getName().compareTo( file2.getName() );
			if( result == 0 ) result = file1.getPath().compareTo( file2.getPath() );
			return invert ? -result : result;
		} else {
			int result = file1.getPath().compareTo( file2.getPath() );
			if( result == 0 ) result = file1.getName().compareTo( file2.getName() );
			return invert ? -result : result;		
		}
	}

	/**
	 * Returns true if both TargetF ileComparator instances use the same
	 * order criterion (name or path).
	 * @param o object to compare this with
	 * @return true if this is the same as the specified object
	 */
	public boolean equals(Object o) {
		if( o == null ) return false;
		try {
			TargetFileComparator other = (TargetFileComparator) o;
			return (orderCriterion == other.orderCriterion) && (invert == other.invert);
		} catch(ClassCastException ccx) {
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		int hash = (orderCriterion == BYNAME) ? this.getClass().hashCode() : -this.getClass().hashCode();
		return hash + (invert ? -1 : 1);
	}
}
