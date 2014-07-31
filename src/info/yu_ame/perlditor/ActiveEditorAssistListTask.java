package info.yu_ame.perlditor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.editors.text.TextEditor;

public class ActiveEditorAssistListTask extends TimerTask{
    
    Pattern p_value = Pattern.compile("(\\$|@|%)([a-zA-Z_0-9]+)");
    Pattern p_method = Pattern.compile("\\s*sub\\s+.*?([a-z0-9_]+)");
    Pattern p_use = Pattern.compile("(?:use|require|use_ok).*?([a-zA-Z0-9]+::[a-zA-Z0-9:]+)");
    Pattern p_constant = Pattern.compile("(?:\"|'|sub\\s+|\\$|@|%|\\s){1}([A-Z0-9_]+)");	
    Pattern p_string = Pattern.compile("([a-zA-Z0-9_]+)");
    private List<String> valueList = new ArrayList<String>();
    private List<HashMap<String, String>> methodList = new ArrayList<HashMap<String, String>>();
    private List<HashMap<String, String>> constantList = new ArrayList<HashMap<String, String>>();
    private List<String> stringList = new ArrayList<String>();
    private IWorkbenchWindow window;
    private int backLength = 0;
    
    public void setWindow(IWorkbenchWindow window){
        this.window = window;
    }
    
    @Override
    public void run() {
        try{
            IWorkbenchPage page = window.getActivePage();
            TextEditor editor = (TextEditor)page.getActiveEditor();
            String text = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
            if(text.length() != backLength){
                setAs(text);
            } 
        }catch (Exception e) {
        }
    }
    
    public List<String> getValueList(){
        return this.valueList;
    }
    
    public List<HashMap<String, String>> getMethodList(){
        return this.methodList;
    }    
 
    public List<HashMap<String, String>> getConstantList(){
        return this.constantList;
    }   
    
    public List<String> getStringList(){
        return this.stringList;
    }    
    
    private void setAs(String text){
        backLength = text.length();
        
        valueList = new ArrayList<String>();
        HashMap<String, String> check = new HashMap<String, String>();
        Matcher m = p_value.matcher(text);
        while(m.find()){
            String head = m.group(1);
            String body = m.group(2);
            if(check.get(head + body) == null){ 
                check.put(head + body, "1");
                valueList.add(head + body);
                if(head.equals("@") || head.equals("%")){
                    valueList.add("$" + body);
                }
            }
        }
       

        methodList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> check2 = new HashMap<String, String>();
        Matcher m2 = p_method.matcher(text);
        while(m2.find()){
            String w = m2.group(1);
            if(check2.get(w) == null){ 
                check2.put(w, "1");
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("name", w);
                hm.put("info", "this");
                methodList.add(hm);
            }
        }      
        constantList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> check3 = new HashMap<String, String>();
        Matcher m_c = p_constant.matcher(text);
        while(m_c.find()){
            String w = m_c.group(1);
            if(check3.get(w) == null){ 
                check3.put(w, "1");
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("name", w);
                hm.put("info", "this");
                constantList.add(hm);
            }
        } 
        
        stringList = new ArrayList<String>();
        HashMap<String, String> check_s = new HashMap<String, String>();
        Matcher m_s = p_string.matcher(text);
        while(m_s.find()){
            String body = m_s.group(1);
            if(check_s.get(body) == null){ 
                check_s.put(body, "1");
                stringList.add(body);
            }
        }          
        
        Matcher m3 = p_use.matcher(text);
        while(m3.find()){
            String w = m3.group(1);            
            String moduleText = getModuleSorce(w);
            if(moduleText.equals("")){
                continue;
            }
            Matcher m_tmp = p_method.matcher(moduleText);
            while(m_tmp.find()){
                String w2 = m_tmp.group(1);
                if(check2.get(w2) == null){ 
                    check2.put(w2, "1");
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("name", w2);
                    hm.put("info", w);
                    methodList.add(hm);
                }
            }
            Matcher m_tmp2 = p_constant.matcher(moduleText);
            while(m_tmp2.find()){
                String w2 = m_tmp2.group(1);
                if(check3.get(w2) == null){ 
                    check3.put(w2, "1");
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("name", w2);
                    hm.put("info", w);
                    constantList.add(hm);
                }
            }
            Matcher m_s2 = p_string.matcher(moduleText);
            while(m_s2.find()){
                String body = m_s2.group(1);
                if(check_s.get(body) == null){ 
                    check_s.put(body, "1");
                    stringList.add(body);
                }
            }        
        }        
        
      
        
    }         
    
    private String getModuleSorce(String module){
        IFileEditorInput input = (IFileEditorInput)this.window.getActivePage().getActiveEditor().getEditorInput();
        IFile file_now = input.getFile();
        IProject activeProject = file_now.getProject();
        
        String modulepath = module.replaceAll("::", "/");

        IFile foundFile = null;
        for(String lpath: Preference.getString(PreferenceKeys.PROJECT_LIBS).split(";")){

            IFile file = (IFile)activeProject.findMember(lpath + "/" + modulepath + ".pm");	
            if(file != null && file.exists()){
                foundFile = file;
                break;
            }
        }          
        if(foundFile == null){
            for(String lpath: Preference.getString(PreferenceKeys.TEST_FILE_LIB).split(";")){

                IFile file = (IFile)activeProject.findMember(lpath + "/" + modulepath + ".pm");	
                if(file != null && file.exists()){
                    foundFile = file;
                    break;
                }
            }         	
        }
        
        if(foundFile == null || !foundFile.exists()){
            return "";
        }   
        StringBuffer sb = new StringBuffer();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(foundFile.getContents()));
            String line;
            while((line = br.readLine()) != null){
                sb.append(line).append(System.getProperty("line.separator"));
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return new String(sb);
    }    
}
