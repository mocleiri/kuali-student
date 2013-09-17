/**
 * 
 */
package net.pandoragames.far.ui.swing.menu;

import java.nio.charset.Charset;

import net.pandoragames.far.ui.model.TargetFile;
import net.pandoragames.far.ui.swing.component.FileSetTableModel;
import net.pandoragames.far.ui.swing.component.listener.AbstractFileOperationListener;

/**
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
class AssignCharsetListener extends AbstractFileOperationListener {
	private Charset encoding;
	public AssignCharsetListener(Charset charset, FileSetTableModel model) {
		super( model );
		encoding = charset;
	}
	protected void execute(TargetFile targetFile) {
		// TODO: assignment testen
		targetFile.setCharacterset( encoding );
		targetFile.info( encoding.displayName() );
	}
}
