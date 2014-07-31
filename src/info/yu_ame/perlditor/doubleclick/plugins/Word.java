package info.yu_ame.perlditor.doubleclick.plugins;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;

import info.yu_ame.perlditor.AbstractDoubleClickPlugin;

public class Word extends AbstractDoubleClickPlugin {
    protected ITextViewer fText;
    public boolean doubleClicked(ITextViewer part) {
        
        int pos = part.getSelectedRange().x;
           
        fText = part;
        
        selectWord(pos);
        return true;
    }	

    protected boolean selectWord(int caretPos) {

        IDocument doc = fText.getDocument();
        int startPos, endPos;

        try {

            int pos = caretPos;
            char c;

            while (pos >= 0) {
                c = doc.getChar(pos);
                if(
                    Character.isLetterOrDigit(c) ||
                    c == '_' ||
                    c == '.' ||
                    c == '%' ||
                    c == '$' ||
                    c == '@'
                ){
                    
                }else{
                    break;
                }
                --pos;
            }

            startPos = pos;

            pos = caretPos;
            int length = doc.getLength();

            while (pos < length) {
                c = doc.getChar(pos);
                if(
                    Character.isLetterOrDigit(c) ||
                    c == '_' ||
                    c == '.' ||
                    c == '%' ||
                    c == '$' ||
                    c == '@'
                ){
                    
                }else{
                    break;
                }
                ++pos;
            }

            endPos = pos;
            selectRange(startPos, endPos);
            return true;

        } catch (BadLocationException x) {
        }

        return false;
    }

    private void selectRange(int startPos, int stopPos) {
        int offset = startPos + 1;
        int length = stopPos - offset;
        fText.setSelectedRange(offset, length);
    }
}

