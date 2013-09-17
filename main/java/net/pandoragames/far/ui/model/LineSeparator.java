package net.pandoragames.far.ui.model;

/**
 * Line separator characters.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public enum LineSeparator {

	/** Undefined line separator. */
	UNDEFINED(),
	/** Unix style line separator. */
	LF('\n'),
	/** DOS style line separator. */
	CRLF('\r', '\n'),
	/** Apple style line separator. */
	CR('\r'),
	/** Exotic line separator. */
	LFCR('\n', '\r');
	
	private char[] sequence;
	
	LineSeparator(char ... sequence) {
		this.sequence = sequence;
	}

	/**
	 * Returns the actual character sequence used for this line separator.
	 * @return character sequence
	 */
	public char[] getSequence() {
		return sequence;
	}
}
