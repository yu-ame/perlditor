package info.yu_ame.perlditor.doubleclick.plugins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;

import info.yu_ame.perlditor.AbstractDoubleClickPlugin;

public class Module extends AbstractDoubleClickPlugin {
    
    protected ITextViewer fText;
    private String mName = "";

    public boolean doubleClicked(ITextViewer part) {
                
        int pos = part.getSelectedRange().x;
           
        fText = part;
        
        if(selectModule(pos)){
            //Activator.getDefault().getLog().log(new Status(IStatus.INFO,    Activator.PLUGIN_ID, "module!"));
            openNewEdtirForModule(mName);
            return true;
        }
        return false;
   
        
    }	

    private boolean selectModule(int currentPos){
        IDocument doc = fText.getDocument();
        int startPos, endPos;
        int pos = currentPos;
        char c = ' ';
        
        try {
            while (pos >= 0) {
                c = doc.getChar(pos);
                
                if (
                    Character.isLetter(c) || 
                    Character.isDigit(c) ||
                    c == ':' ||
                    c == '-'	
                ){
                    --pos;
                    continue;
                }
                break;
            }    
            
            startPos = pos + 1;
            
            pos = currentPos;
            int length = doc.getLength();
            c = ' ';

            while (pos < length) {
                c = doc.getChar(pos);
                if (
                    Character.isLetter(c) || 
                    Character.isDigit(c) ||
                    c == ':'
                ){
                    ++pos;
                    continue;
                }
                break;
            }
            
            endPos = pos;
            
            String text = doc.get(startPos, endPos - startPos);

            if(text.matches("^((?:[a-zA-Z0-9_]+::)+)([a-zA-Z0-9_]+)$")){
                mName = text;
                fText.setSelectedRange(startPos, endPos - startPos);
                return true;
            }
            if(text.matches("^-([a-zA-Z0-9_:]+)$")){
                String tmp = text;
                tmp = tmp.replace("-", "");

                System.out.println("t="+text);
                String document = doc.get();
                Pattern p = Pattern.compile("((?:(?:[a-zA-Z0-9_]+::)+)(?:[a-zA-Z0-9_]+))[^:]*?->[a-z_]+\\([^\\(\\)]*" + text, Pattern.DOTALL);
                Matcher m = p.matcher(document);
                if(m.find()){
                    mName = m.group(1) + "::" + tmp;
                    System.out.println("m="+mName);
                    fText.setSelectedRange(startPos, endPos - startPos);
                    return true;
                }
                return false;            	
            }
        } catch (BadLocationException x) {
        }    
        return false;
    }    
}
