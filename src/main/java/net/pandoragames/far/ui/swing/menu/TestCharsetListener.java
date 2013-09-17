package net.pandoragames.far.ui.swing.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.util.Arrays;

import net.pandoragames.far.ui.model.CharacterUtil;
import net.pandoragames.far.ui.model.TargetFile;
import net.pandoragames.far.ui.swing.component.FileSetTableModel;
import net.pandoragames.far.ui.swing.component.listener.AbstractFileOperationListener;
import net.pandoragames.util.i18n.Localizer;

/**
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */

class TestCharsetListener extends AbstractFileOperationListener {
	private Localizer localizer;
	private Charset defaultEncoding;
	public TestCharsetListener(Charset standard, FileSetTableModel model, Localizer localizer) {
		super( model );
		this.localizer = localizer;
		defaultEncoding = standard;
	}
	
	@Override
	protected final void execute(TargetFile targetFile) {
		File source = targetFile.getFile();
		if( ! source.exists() ) {
			targetFile.error( localizer.localize("message.file-not-found") );
			return;
		}
		if( ! source.canWrite() ) {
			targetFile.error( localizer.localize("message.read-only") );
			return;
		}
		final char NULLCHAR = '\u0000';
		boolean ok = true;
		Reader input = null;
		Charset fileEncoding = ( targetFile.getCharacterset() == null ) ? defaultEncoding : targetFile.getCharacterset();
		try {
			final int BUFSIZ = 1024;
			input = new InputStreamReader(new FileInputStream( source ), fileEncoding.newDecoder());
			char[] buffer = new char[BUFSIZ];
			int charsread = 0;
			String context = "";
			int lineCount = 1;
			do {
				Arrays.fill(buffer, NULLCHAR);
				try{
					charsread = input.read(buffer, 0, BUFSIZ);
				}catch(MalformedInputException mix) {
					// charsread not reliable at this point
					charsread = 0;
					for(; charsread < BUFSIZ; charsread++) {
						if(buffer[charsread] == NULLCHAR) break;
					}
					if( charsread > 1 ) context = getContext(buffer, charsread );
					Integer lineNumber = Integer.valueOf( lineCount + CharacterUtil.countLinebreaks(buffer, 0, charsread) );
					targetFile.error( localizer.localize("message.illegal-byte-sequence", new Object[]{ context, lineNumber} ) );
					ok = false;
					break;
				}
				if( charsread > 0 ) {
					context = getContext(buffer, charsread );
					lineCount = lineCount + CharacterUtil.countLinebreaks(buffer, 0, charsread);
				}
			} while( charsread > 0 );		
			if( ok ) targetFile.info( fileEncoding.displayName() + " " + localizer.localize("message.ok") );
		} catch(IOException iox) {
			targetFile.error( localizer.localize("message.file-processing-error", new Object[]{ targetFile.getName(), iox.getMessage()}) );
			logger.error( iox.getClass().getName() + " processing " + targetFile.getFile().getPath() + ": " + iox.getMessage());
		} finally {
			if( input != null ) try { input.close(); } catch(IOException x) { /* ignore me*/ }
		}
	}	
	private String getContext(char[] buffer, int pos) {
		if( pos < 1 ) return "";
		int rawLength = Math.min(pos, 8);
		String raw = String.valueOf(buffer, pos - rawLength, rawLength);
		return CharacterUtil.flatWhiteSpace(raw);
	}	
}
