package info.yu_ame.perlditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

public class PerlAssistProcessor implements IContentAssistProcessor {

    private List<String> assisList_now;
    private int last_offset = 0;
    private PackageList pl = PackageList.getInstance();
    private ActiveEditorAssistList al = ActiveEditorAssistList.getInstance();
    
    public PerlAssistProcessor(){
        this.assisList_now = pl.getAssistList();
        al.setIWorkbenchWindow(PlatformUI.getWorkbench().getActiveWorkbenchWindow());//widnow is not null!
    }  
    
    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
            int offset) {
        
        assisList_now = pl.getAssistList();
        
        if(offset == last_offset -1){
            return new ICompletionProposal[0];
        }
        
        String currentWord = getCurrentWord(viewer.getDocument(), offset);
        last_offset = offset;
        
        
        
        List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
        int currentWordLen = currentWord.length();
        if(currentWordLen <= 0){
            ICompletionProposal[] proposals =
                new ICompletionProposal[0];            
            return proposals;
        }        
        
        Pattern package_pattern = Pattern.compile("^((?:[a-zA-Z0-9]+::)*)("+currentWord + "[a-zA-Z0-9]*)$", Pattern.CASE_INSENSITIVE);
        Pattern package_pattern2 = Pattern.compile("^((?:[a-zA-Z0-9]+::)*"+currentWord + ".*)$", Pattern.CASE_INSENSITIVE);
        Pattern value_pattern = Pattern.compile("^(\\Q" + currentWord + "\\E[a-zA-Z_0-9]*)$");
        Pattern constant_pattern = Pattern.compile("^("+ currentWord + "[A-Z_0-9]*)$");
        Pattern string_pattern = Pattern.compile("^(\\Q" + currentWord + "\\E[a-zA-Z_0-9]*)$");     
        
        List<String> tmp_a = new ArrayList<String>();
        if(currentWord.matches("^(?:\\$|@|%)[a-zA-Z0-9_]*")){


            Iterator<String> i_p = al.getValueList().iterator();
            while(i_p.hasNext()){
                String tmp = i_p.next();
    
                Matcher m = value_pattern.matcher(tmp);
                if(
                        m.find()
                ){
                    result.add(
                        new PerlCompletionProposal(
                            tmp,
                            offset - currentWordLen, 
                            currentWordLen, 
                            tmp.length(),
                            null,
                            m.group(1),
                            null,
                            null
                        )
                    );          
                }
            }        	
            
        }else if (isMethod(viewer.getDocument(), offset - currentWordLen) && 
                currentWord.matches("^([a-zA-Z0-9]+::)*[a-z_]+$")){

            currentWord = currentWord.replaceAll("^([a-zA-Z0-9]+::)*", "");
            currentWordLen = currentWord.length();

            Pattern method_pattern = Pattern.compile("^("+ currentWord + "[a-zA-Z_0-9]*)$");
                   
            Iterator<HashMap<String, String>> i_p = al.getMethodList().iterator();
            while(i_p.hasNext()){
                HashMap<String, String> tmp = i_p.next();
    
                Matcher m = method_pattern.matcher(tmp.get("name"));
                if(
                        m.find()
                ){
                    result.add(
                        new PerlCompletionProposal(
                            tmp.get("name"),
                            offset - currentWordLen, 
                            currentWordLen, 
                            tmp.get("name").length(),
                            null,
                            m.group(1) + " - " + tmp.get("info"),
                            null,
                            null
                        )
                    );          
                }
            }  
            
            Iterator<String> i_p2 = al.getStringList().iterator();
            while(i_p2.hasNext()){
                String tmp = i_p2.next();
                Matcher m = string_pattern.matcher(tmp);
                if( 
                        m.find()
                ){
                    result.add(
                        new PerlCompletionProposal(
                            tmp,
                            offset - currentWordLen, 
                            currentWordLen, 
                            tmp.length(),
                            null,
                            m.group(1),
                            null,
                            null
                        )
                    );          
                }
            }               
        }else if(currentWord.matches("^[a-z]+")){            
            Iterator<String> i_p = al.getStringList().iterator();
            while(i_p.hasNext()){
                String tmp = i_p.next();
                Matcher m = string_pattern.matcher(tmp);
                if(
                        m.find()
                ){
                    result.add(
                        new PerlCompletionProposal(
                            tmp,
                            offset - currentWordLen, 
                            currentWordLen, 
                            tmp.length(),
                            null,
                            m.group(1),
                            null,
                            null
                        )
                    );          
                }
            }        
            
        }else{
            Iterator<String> i = this.sortAsList(assisList_now).iterator();
            HashMap<String, String> check = new HashMap<String, String>();
            
            while(i.hasNext()){
                String tmp = i.next();
    
                Matcher m = package_pattern.matcher(tmp);
                if(   
                        m.find()
                ){
                    result.add(
                        new PerlCompletionProposal(
                            tmp,
                            offset - currentWordLen, 
                            currentWordLen, 
                            tmp.length(),
                            null,
                            m.group(2) + " - " + m.group(1),
                            null,
                            null
                        )
                    );          
                    tmp_a.add(tmp);
                    check.put(tmp, "1");
                }
            }

            Iterator<String> i2 = this.sortAsList(assisList_now).iterator();
            
            while(i2.hasNext()){
                String tmp = i2.next();
    
                Matcher m = package_pattern2.matcher(tmp);
                if(   
                        m.find() && check.get(tmp) == null
                ){
                    result.add(
                        new PerlCompletionProposal(
                            tmp,
                            offset - currentWordLen, 
                            currentWordLen, 
                            tmp.length(),
                            null,
                            m.group(1),
                            null,
                            null
                        )
                    );          
                    tmp_a.add(tmp);
                }
            }            
            
            Iterator<HashMap<String, String>> i_p = al.getConstantList().iterator();
            while(i_p.hasNext()){
                HashMap<String, String> tmp = i_p.next();
    
                Matcher m = constant_pattern.matcher(tmp.get("name"));
                if(
                        m.find()
                ){
                    result.add(
                        new PerlCompletionProposal(
                            tmp.get("name"),
                            offset - currentWordLen, 
                            currentWordLen, 
                            tmp.get("name").length(),
                            null,
                            m.group(1) + " - " + tmp.get("info"),
                            null,
                            null
                        )
                    );          
                }
            }   
            
            currentWord = currentWord.replaceAll("^([a-zA-Z0-9]+::)*", "");
            currentWordLen = currentWord.length();

            Pattern string_pattern2 = Pattern.compile("^(\\Q" + currentWord + "\\E[a-zA-Z_0-9]*)$");   
            
            Iterator<String> i_ps = al.getStringList().iterator();
            while(i_ps.hasNext()){
                String tmp = i_ps.next();
                Matcher m = string_pattern2.matcher(tmp);
                if( 
                        m.find()
                ){
                    result.add(
                        new PerlCompletionProposal(
                            tmp,
                            offset - currentWordLen, 
                            currentWordLen, 
                            tmp.length(),
                            null,
                            m.group(1),
                            null,
                            null
                        )
                    );          
                }
            }      
        }

        ICompletionProposal[] proposals =
          new ICompletionProposal[result.size()];
        result.toArray(proposals);
        return proposals;
    }
    
    private ArrayList<String> sortAsList(List<String> assisList_now2){
        Collections.sort(assisList_now2);
        
        String path = getSelfLibraryPath();
        ArrayList<String> newList = new ArrayList<String>();
        HashMap<String, String> check = new HashMap<String, String>();
        int nestCount = 0;
        while(path.length() > 0 && nestCount < 20){
            for(String asStr: assisList_now2){
                if(asStr.matches("^" +path + ".*$") && check.get(asStr) == null){
                    newList.add(asStr);
                    check.put(asStr, "1");
                }
            }
            path = path.replaceAll("(::)?[^:]+$", "");
            nestCount++;
        }
        for(String asStr: assisList_now2){
            if(check.get(asStr) == null){
                newList.add(asStr);
                check.put(asStr, "1");
            }
        }
        
        return newList;
    }
    
    private String getSelfLibraryPath(){
        IFileEditorInput input = (IFileEditorInput)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
        IFile file_now = input.getFile();
        String path = file_now.getFullPath().toString();
        StringBuffer sb = new StringBuffer();
        String s[] = Preference.getString(PreferenceKeys.PROJECT_LIBS).split(";");
        for(int i =0; i < s.length;i++){
            sb.append(s[i]).append("/");
            if(i < s.length -1){
                sb.append("|");
            }
        }
        path  = path.replaceAll("^.*?(" + sb.toString() + ")", "");
        path  = path.replaceAll("/[^/]+$", "");
        path  = path.replaceAll("/", "::");
        return path;
    }
    
    private boolean isMethod(IDocument document, int documentOffset){
        try {
            if(
                document.getChar(documentOffset - 1) == '>' &&
                document.getChar(documentOffset - 2) == '-'
            ){
                return true;
            }
            if(
                document.getChar(documentOffset - 1) == ':' &&
                document.getChar(documentOffset - 2) == ':'
            ){
                return true;
            }
        } catch (Exception e) {
        }        
        return false;
    }
    
    protected String getCurrentWord(IDocument document, int documentOffset) {
        StringBuffer currentWord = new StringBuffer();
        char ch;
        
        try {
            for (int offset = documentOffset - 1 ;
                offset >=0 
                && 
                (
                    Character.isLetterOrDigit(ch = document.getChar(offset))
                    || '_' == document.getChar(offset)
                    || ':' == document.getChar(offset)
                    || '$' == document.getChar(offset)
                    || '@' == document.getChar(offset)
                    || '%' == document.getChar(offset)
                )
                ;
                offset--) {
                currentWord.insert(0, ch);
            }
            return currentWord.toString();
        } catch (Exception e) {
            return null;
        }
    }    

    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer,
            int offset) {
        // TODO Auto-generated method stub   
        return null;   
    }
        

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }

    @Override
    public String getErrorMessage() {
        // TODO Auto-generated method stub
        return null;
    }

}
