package info.yu_ame.perlditor;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import org.eclipse.ui.IWorkbenchWindow;

public class ActiveEditorAssistList {

    private static ActiveEditorAssistList a;
    private Timer timer1 = new Timer();
    private ActiveEditorAssistListTask run;
    
    private ActiveEditorAssistList(){
        run = new ActiveEditorAssistListTask();
        timer1.schedule(run, 0, 3000);    
    }
    
    public void setIWorkbenchWindow(IWorkbenchWindow window){
        run.setWindow(window);
    }
    
    public static ActiveEditorAssistList getInstance(){
        if(a == null){
            a = new ActiveEditorAssistList();           
        }
        return a;
    }
    
    public List<String> getValueList(){
        return run.getValueList();
    }
    
    public List<String> getStringList(){
        return run.getStringList();
    }    
    
    public List<HashMap<String, String>> getMethodList(){
        return run.getMethodList();
    }    
 
    public List<HashMap<String, String>> getConstantList(){
        return run.getConstantList();
    }   
    
    
}
