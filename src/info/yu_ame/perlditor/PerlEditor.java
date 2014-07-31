package info.yu_ame.perlditor;


import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.CursorLinePainter;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;

public class PerlEditor extends TextEditor {
    
    private ColorManager colorManager;
    private PerlConfiguration config;
    Pattern p_value = Pattern.compile("((?:\\$|@|%)[a-zA-Z_0-9]+)");
    Pattern p_method = Pattern.compile("\\s*sub\\s+.*?([a-zA-Z_0-9]+)");
    Pattern p_use = Pattern.compile("use\\s+(?:parent\\s+|base\\s+)*(?:\"|')*([a-zA-Z0-9:]+)(?:\"|')*");
    Pattern p_constant = Pattern.compile("(?:\"|')*([A-Z0-9_]+)(?:\"|')*\\s*=");
    
    public PerlEditor() {
        super();
        colorManager = new ColorManager();
        config = new PerlConfiguration(colorManager);
        setSourceViewerConfiguration(config);
        setDocumentProvider(new PerlDocumentProvider());
    } 

    public void dispose() {
        colorManager.dispose();
        super.dispose();
    }
    
    public void doSave(IProgressMonitor monitor)
    {
        updateState(getEditorInput());
        validateState(getEditorInput());
        performSave(false, monitor);
    }

    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        this.getSourceViewer().getTextWidget().setBackground(
                Preference.getPreferenceColor(parent.getDisplay(), PreferenceKeys.COLOR_BACKGROUND)
        );
        
        CursorLinePainter cursorLinePainter = new CursorLinePainter(this.getSourceViewer());
        cursorLinePainter.setHighlightColor(
                Preference.getPreferenceColor(parent.getDisplay(), PreferenceKeys.COLOR_HIGHLIGHT)
        );      
        ((ITextViewerExtension2)this.getSourceViewer()).addPainter(cursorLinePainter);
  
    }

}
