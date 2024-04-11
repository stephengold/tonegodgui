/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core;

/**
 *
 * @author t0neg0d
 */
class ConflictingIDException extends Exception {
	public ConflictingIDException () {  }
	
    public ConflictingIDException (String message) {
		super (message);
	}
	
    public ConflictingIDException (Throwable cause) {
		super (cause);
	}
	
    public ConflictingIDException (String message, Throwable cause) {
		super (message, cause);
	}
}
