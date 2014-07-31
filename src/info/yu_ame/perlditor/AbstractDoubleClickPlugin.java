package info.yu_ame.perlditor;

import java.io.File;
import java.io.FileWriter;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

public abstract class AbstractDoubleClickPlugin {

    public abstract boolean doubleClicked(ITextViewer part);
    
    public void openNewEdtirForModule(String moduleName){
        
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        
        IFileEditorInput input = (IFileEditorInput)page.getActiveEditor().getEditorInput() ;
        IFile filea = input.getFile();
        IProject activeProject = filea.getProject();
        
        String modulepath = moduleName.replaceAll("::", "/");
        String modulepathForValue = modulepath.replaceFirst("/[a-zA-Z0-9_]+$", "");

        IFile foundFile = null;
        for(String lpath: Preference.getString(PreferenceKeys.PROJECT_LIBS).split(";")){

            IFile file = (IFile)activeProject.findMember(lpath + "/" + modulepath + ".pm");	
            IFile file2 = (IFile)activeProject.findMember(lpath + "/" + modulepathForValue + ".pm");
            if(file != null && file.exists()){
                foundFile = file;
                break;
            }
            if(file2 != null && file2.exists()){
                foundFile = file2;
                break;
            }
        }       
        if(foundFile == null){
            
            SshCommander sc = SshCommander.getInstance();
            for(String str: Preference.getString(PreferenceKeys.MODULE_LIBS).split(";")){
                String[] check_pm = {
                    str+"/"+modulepath+".pm",
                    str+"/"+modulepathForValue+".pm"	
                };
                for(String path_tmp: check_pm){
                    String ret = sc.execute("test -f "+path_tmp+" && echo $?");
                    if(ret.equals("0\n")){
                        String filestr = sc.execute("cat " + path_tmp);
                        File file = new File(Preference.getString(PreferenceKeys.LOCAL_TMPL_FILE_DIR) + "\\" + System.currentTimeMillis() + ".pm");
                        try{
                            FileWriter fw = new FileWriter(file);
                            fw.write(filestr);
                            fw.close();
    
                              IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.toURI());
                           
                              try {
                                  IDE.openEditorOnFileStore( page, fileStore );
                              } catch ( Exception e ) {
                                  e.printStackTrace();
                              }
                             return;
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        
                    }
                }
            }
            
            return;
        }
        
        IEditorDescriptor desc = PlatformUI.getWorkbench().
        getEditorRegistry().getDefaultEditor(foundFile.getName());
        try {
            page.openEditor(new FileEditorInput(foundFile), desc.getId());
        } catch (PartInitException e) {
            e.printStackTrace();
        }   

    }        
}
