package net.pandoragames.far.ui.swing.menu;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.pandoragames.far.ui.model.CharacterUtil;
import net.pandoragames.far.ui.model.TargetFile;
import net.pandoragames.far.ui.swing.component.FileSetTableModel;
import net.pandoragames.far.ui.swing.component.listener.AbstractFileUpdaterListener;
import net.pandoragames.util.i18n.Localizer;

/**
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
class TrimLinesListener extends AbstractFileUpdaterListener {
	private int trimmedLineCount;
	public TrimLinesListener(FileSetTableModel model, Localizer localizer) {
		super(model, localizer);
	}
	protected boolean executeForUpdate(TargetFile targetFile, File tempfile) throws IOException {
		trimmedLineCount = 0;
		boolean startOfLine = true;
		boolean leadingWS = false;
		boolean trailingWS = false;
		byte[] whiteBuffer = new byte[1024];
		int bufferIndex = 0;
		int currentChar = 0;
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream( targetFile.getFile() );
			output = new BufferedOutputStream( new FileOutputStream( tempfile ) );
			do {
				currentChar = input.read();
				// remove leading ws
				while( startOfLine && isWhite( currentChar )) {
					leadingWS = true;
					currentChar = input.read();
				}
				// buffer intermediate and trailing ws
				while( isWhite( currentChar )) {
					trailingWS = true;
					whiteBuffer[bufferIndex++] = (byte) currentChar;
					if( bufferIndex == whiteBuffer.length ) {
						for(int i = 0; i < bufferIndex; i++) output.write( whiteBuffer[i] );
						bufferIndex = 0;
						trailingWS = false;
					}
					currentChar = input.read();
				}
				// write non-ws
				if( currentChar >= 0 ) {
					startOfLine = CharacterUtil.isLineBreakChar( (char) currentChar );
					// write buffered intermediate ws
					if( !startOfLine && bufferIndex > 0) {
						for(int i = 0; i < bufferIndex; i++) output.write( whiteBuffer[i] );
						bufferIndex = 0;
						trailingWS = false;
					}
					output.write( currentChar );
				}
				if( startOfLine || currentChar < 0 ) {
					if( leadingWS || trailingWS ) trimmedLineCount++;
					leadingWS = false;
					trailingWS = false;
					bufferIndex = 0;
				}
			} while( currentChar >= 0 );
			
			output.flush();
		} finally {
			if( input != null ) try { input.close(); } catch(IOException x) { /* ignore me*/ }
			if( output != null ) try { output.close(); } catch(IOException x) { /* ignore me*/ }
		}
		return true;		
	}
	private boolean isWhite(int c) {
		return c == ' ' || c == '\t';
	}
	protected void success(TargetFile targetFile) {
		targetFile.info( localizer.localize("message.n-lines-trimmed", Integer.valueOf(trimmedLineCount)) );
		logger.info(trimmedLineCount + " lines trimmed in " + targetFile.getFile().getPath());
	}
}
