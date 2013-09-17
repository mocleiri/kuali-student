package net.pandoragames.far.ui.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Displays single HTML pages.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class HelpView extends SubWindow {

	private static final int MINWIDTH = 100;
	private static final int MINHEIGHT = 100;
	
	private static int PREFWIDTH = 0;
	private static int PREFHEIGHT = 0;

	private JEditorPane textArea;
	private JScrollPane contentScrollPane;
	private boolean isSmall = false;
	
	/**
	 * Constructor.
	 * @param owner root window
	 * @param title window title
	 * @param fileName a file relative to the "help" directory
	 * @param screenCenter screen coordinates
	 */
	public HelpView(JFrame owner, String title, String fileName, Point screenCenter) {
		super(owner, title);
		init(fileName, screenCenter);
	}
	
	/**
	 * Called when used for the about page.
	 */
	public void setSmall() {
		contentScrollPane.setPreferredSize( new Dimension( 300, 300 ) );
		isSmall = true;
	}
	
	/*
	 * paint the window
	 */
	private void init(String fileName, Point screenCenter) {
		
		long timer = System.currentTimeMillis();
		// text area
		textArea = new JEditorPane();
		textArea.setEditable( false );
		textArea.addHyperlinkListener( new HelpLink() );

		readURI( textArea , "help/en/" + fileName );
		
		contentScrollPane = new JScrollPane( textArea );
		contentScrollPane.setAlignmentX( Component.LEFT_ALIGNMENT );
		registerCloseWindowKeyListener( contentScrollPane );
		
		contentScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		
		if(PREFWIDTH < MINWIDTH || PREFHEIGHT < MINHEIGHT) calculatePreferedDim();
		contentScrollPane.setPreferredSize( new Dimension(PREFWIDTH, PREFHEIGHT) );
		contentScrollPane.setMinimumSize(new Dimension(MINWIDTH, MINHEIGHT));
		addWindowListener( new RememberDimensionListener() );
		getContentPane().add( contentScrollPane, BorderLayout.CENTER );
		
		placeOnScreen( screenCenter );
		logger.debug("file " + fileName + " loaded in " + (System.currentTimeMillis() - timer) + " ms");
	}
	
	private void calculatePreferedDim() {
		Rectangle screen = getGraphicsConfiguration().getBounds();
		PREFWIDTH = Math.min(screen.width/2,520);
		PREFHEIGHT = Math.min(2*screen.height/3, 560);
	}

	/*
	 * Read the resource and write it to the editor pane
	 */
	private void readURI(JEditorPane editorPane, String resource) {
		URL helpURL = this.getClass().getClassLoader().getResource( resource );
		if (helpURL != null) {
			readURI( editorPane, helpURL );
		} else {
			logger.error("Couldn't find file: " + resource );
		}		
	}

	/*
	 * Read the resource and write it to the editor pane
	 */
	private void readURI(JEditorPane editorPane, URL helpURL) {
	    try {
	        editorPane.setPage(helpURL);
	    } catch (IOException e) {
	        logger.error("Attempted to read a bad URL: " + helpURL);
	    }
	}
	
	// HyperlinkListener
    class HelpLink implements HyperlinkListener {  	 
        public void hyperlinkUpdate(HyperlinkEvent event) {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            	URL target = event.getURL();
            	if( target != null ) {
            		logger.debug("Link to " + target.toString());
            		readURI( textArea, target );
            	}
            }
        }
    }

	class RememberDimensionListener extends WindowAdapter {
		public void windowDeactivated(WindowEvent e) {
			Window win = e.getWindow();
			if(win != null && ! isSmall) {
				Dimension dim = win.getSize();
				if( dim.width > MINWIDTH ) {
					PREFWIDTH = dim.width;
				} else {
					PREFWIDTH = MINWIDTH;
				}
				if( dim.height > MINHEIGHT ) {
					PREFHEIGHT = dim.height;
				} else {
					PREFHEIGHT = MINHEIGHT;
				}
			}
		}
	}

}
