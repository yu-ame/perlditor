package info.yu_ame.perlditor.handler;

import info.yu_ame.perlditor.Activator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.IEncodingSupport;


public class AutoCharsetHandler extends AbstractHandler {
    
    public byte[] readAll(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte [] buffer = new byte[1024];
        while(true) {
            int len = inputStream.read(buffer);
            if(len < 0) {
                break;
            }
            bout.write(buffer, 0, len);
        }
        bout.close();
        return bout.toByteArray();
    }
    
  public Object execute(ExecutionEvent event) throws ExecutionException {
      
      
      IPreferenceStore store = Activator.getDefault().getPreferenceStore();
      store.setValue("PREF_KEY", 10);

     IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

     //open Project explorer
     IFileEditorInput input = (IFileEditorInput)page.getActiveEditor().getEditorInput() ;
     IFile filea = input.getFile();
     
     InputStream s = null;
    try {
        s = filea.getContents();
        try{
            byte[] bytes = readAll(s);
            
            String str = new String(bytes, "JISAutoDetect");
            
            String charset = "UTF-8";
            if(str.matches("\\p{ASCII}+")){
                
            }
            else if(str.equals(new String(bytes, "EUC-JP"))){
                charset = "EUC-JP";
            }     
         
            IEncodingSupport encodingSupport = (IEncodingSupport)page.getActiveEditor().getAdapter(IEncodingSupport.class);
            encodingSupport.setEncoding(charset);
        }catch(Exception e){

        }finally{
            try {
                s.close();
            } catch (IOException e1) {

            }        	
        }
    } catch (CoreException e1) {
    }

    return null;
  }
  
}