package info.yu_ame.perlditor;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.CursorLinePainter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
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
        checkSyntax();
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

    private void checkSyntax(){
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

        //open Project explorer
        IFileEditorInput input = (IFileEditorInput)page.getActiveEditor().getEditorInput() ;
        TextEditor editor = (TextEditor)page.getActiveEditor();
        IFile file = input.getFile();
        
      

    	
    	Thread th = new Thread(new CheckSyntaxRunnable(file));
    	th.start();

        
    }
    
    private class CheckSyntaxRunnable implements Runnable {

    	IFile file;
    	String perlPath;
    	String projectPath;
    	
    	public CheckSyntaxRunnable(IFile file){
    		this.file = file;
    		IProject project = file.getProject();
    		perlPath = PerlPropertyPage.getValue(project, PerlPropertyPage.KEY_PERL_PATH);
    		projectPath = PerlPropertyPage.getValue(project, PerlPropertyPage.KEY_PROJECT_PATH);

    	}
    	
    	private String convertInputStreamToString(InputStream is) throws IOException {
	        InputStreamReader reader = new InputStreamReader(is);
	        StringBuilder builder = new StringBuilder();
	        char[] buf = new char[1024];
	        int numRead;
	        while (0 <= (numRead = reader.read(buf))) {
	            builder.append(buf, 0, numRead);
	        }
	        return builder.toString();
    	}    	
    	
		@Override
		public void run() {			
			
	        String fileExtension = file.getFileExtension();
	        String filePath = file.getProjectRelativePath().toString();     
	        String returnStr = "";
	        if(fileExtension.equals("pm")){
	        	String text;
				try {
					text = convertInputStreamToString(file.getContents());
				} catch (IOException | CoreException e) {
					return;
				}

	            Pattern p_package = Pattern.compile("package\\s+([a-zA-Z0-9:]+);");
	            Matcher m_package = p_package.matcher(text);
	            if(!m_package.find()){	 
	                return;
	            }        	
	            String s_package = m_package.group(1);        	
	            returnStr = SshCommander.getInstance().execute(perlPath + " -wc -e \"use " + s_package + ";\" 2>&1");        	        	
	        }else if(fileExtension.equals("pl")){
	            returnStr = SshCommander.getInstance().execute(perlPath + " -wc "+ projectPath +"/" + filePath + " 2>&1");        	
	        }
	        
	        if(returnStr.matches(".*syntax\\sOK.*")){
	        	
	        }else{
	        	PerlConsole.println(returnStr);
	        }			
		}
    }
}
