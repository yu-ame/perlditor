package info.yu_ame.perlditor;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class PerlCompletionProposal implements ICompletionProposal{
    private CompletionProposal cp;
    private String replacementString;
    private int position = 0;

    /**
     * Creates a new completion proposal based on the provided information. The replacement string is
     * considered being the display string too. All remaining fields are set to <code>null</code>.
     *
     * @param replacementString the actual string to be inserted into the document
     * @param replacementOffset the offset of the text to be replaced
     * @param replacementLength the length of the text to be replaced
     * @param cursorPosition the position of the cursor following the insert relative to replacementOffset
     */
    public PerlCompletionProposal(String replacementString, int replacementOffset, int replacementLength, int cursorPosition) {
        this(replacementString, replacementOffset, replacementLength, cursorPosition, null, null, null ,null);
    }

    /**
     * Creates a new completion proposal. All fields are initialized based on the provided information.
     *
     * @param replacementString the actual string to be inserted into the document
     * @param replacementOffset the offset of the text to be replaced
     * @param replacementLength the length of the text to be replaced
     * @param cursorPosition the position of the cursor following the insert relative to replacementOffset
     * @param image the image to display for this proposal
     * @param displayString the string to be displayed for the proposal
     * @param contextInformation the context information associated with this proposal
     * @param additionalProposalInfo the additional information associated with this proposal
     */
    public PerlCompletionProposal(String replacementString, int replacementOffset, int replacementLength, int cursorPosition, Image image, String displayString, IContextInformation contextInformation, String additionalProposalInfo) {
        cp = new CompletionProposal(replacementString, replacementOffset, replacementLength, cursorPosition, image, displayString, contextInformation, additionalProposalInfo);
        this.replacementString = replacementString;
        this.position = replacementOffset + cursorPosition;
    }

    @Override
    public void apply(IDocument document) {
        cp.apply(document);
        
        Pattern p = Pattern.compile("(use|require|use_ok)[^;]*"+ this.replacementString + "(\\s|;|'|\")", Pattern.DOTALL);
        Pattern p2 = Pattern.compile("^use\\s+(?!constan)[^;]*[a-zA-Z0-9:]+.*?;", Pattern.MULTILINE | Pattern.DOTALL);

        
        if(this.replacementString.matches(".*::.*") &&
            !p.matcher(document.get()).find()
        ){
            Matcher m = p2.matcher(document.get());
            MatchResult last = null;
            while(m.find()){
                last = m.toMatchResult();
            }
            if(last != null){
                try {
                    String insert = "\n" + "use " + this.replacementString + ";";
                    this.position += insert.length();
                    document.replace(last.end(), 0, insert);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                
            }
        }
    }

    @Override
    public Point getSelection(IDocument document) {
        return new Point(this.position, 0);
    }

    @Override
    public String getAdditionalProposalInfo() {
        return cp.getAdditionalProposalInfo();
    }

    @Override
    public String getDisplayString() {
        return cp.getDisplayString();
    }

    @Override
    public Image getImage() {
        return cp.getImage();
    }

    @Override
    public IContextInformation getContextInformation() {
        return cp.getContextInformation();
    }
    
}
