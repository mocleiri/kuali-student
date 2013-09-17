package net.pandoragames.far.ui.model;
/**
 * Represents an operation like "find" or "replace".
 * Each page (or tab) in FAR is dedicated to a particular <i>operation</i>,
 * e.g. "find" or "replace". This enum serves to identify those operations
 * in varying contexts.
 * @see OperationForm
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public enum OperationType  {
	/** Represents "no type" */
	NONE,
	/** Type for find forms and operations */
	FIND,
	/** Type for filter forms and operations */
	FILTER,
	/** Type for replace forms and operations */
	REPLACE,
	/** Type for rename forms and operations */
	RENAME,
	/** Represents any type of operation */
	ANY
}