package info.yu_ame.perlditor;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;

public class PerlConfiguration extends SourceViewerConfiguration {

    private ColorManager colorManager;
    private PerlDefaultScanner defaultScanner;
    private PerlDouble doubleClickStrategy;
    private ContentAssistant assistant;
    private PerlAssistProcessor processor;
    
    public PerlConfiguration(ColorManager colorManager) {
        this.colorManager = colorManager;
    }    
    
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) { 
     return new String[] {
       IDocument.DEFAULT_CONTENT_TYPE,
       PerlContentType.POD,
       PerlContentType.COMMENT,
       PerlContentType.String};
   }
    
    public ITextDoubleClickStrategy getDoubleClickStrategy(
        ISourceViewer sourceViewer,
        String contentType) {
        if (doubleClickStrategy == null)
            doubleClickStrategy = new PerlDouble();
        return doubleClickStrategy;
    }
    
    public IContentAssistant getContentAssistant
      (ISourceViewer sourceViewer) {
        if(assistant == null){
            assistant = new ContentAssistant();
            assistant.setContextInformationPopupOrientation
            (IContentAssistant.CONTEXT_INFO_ABOVE);
    
            processor = new PerlAssistProcessor();
            assistant.setContentAssistProcessor(processor,IDocument.DEFAULT_CONTENT_TYPE);
            assistant.setContentAssistProcessor(processor,PerlContentType.POD);
            assistant.setContentAssistProcessor(processor,PerlContentType.COMMENT);
            assistant.setContentAssistProcessor(processor,PerlContentType.String);
            

            IDialogSettings dia = Activator.getDefault().getDialogSettings().addNewSection("assist");
            
            dia.put(ContentAssistant.STORE_SIZE_X, 600);
            dia.put(ContentAssistant.STORE_SIZE_Y, 350);
            assistant.setRestoreCompletionProposalSize(dia);
                        
            assistant.install(sourceViewer);
        }

        return assistant;
    }

    protected PerlDefaultScanner getPerlDefaultScanner() {
        if (defaultScanner == null) {
            defaultScanner = new PerlDefaultScanner(colorManager);
            defaultScanner.setDefaultReturnToken(
                new Token(
                    new TextAttribute(
                        colorManager.getColor(
                                Preference.getPreferenceRGB(PreferenceKeys.COLOR_OTHER)))));
        }
        return defaultScanner;
    }

    private static class SingleTokenScanner extends BufferedRuleBasedScanner {
        public SingleTokenScanner(TextAttribute attribute) {
          setDefaultReturnToken(new Token(attribute));
        }
    };    

    public IPresentationReconciler getPresentationReconciler(
            ISourceViewer sourceViewer) { 
        PresentationReconciler reconciler = new PresentationReconciler();
        
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
                new SingleTokenScanner(
                        new TextAttribute(colorManager.getColor(
                                Preference.getPreferenceRGB(PreferenceKeys.COLOR_POD)
                        ))
                )
        );
        reconciler.setDamager(dr, PerlContentType.POD);
        reconciler.setRepairer(dr, PerlContentType.POD);

        dr = new DefaultDamagerRepairer(
                new SingleTokenScanner(
                        new TextAttribute(colorManager.getColor(
                                Preference.getPreferenceRGB(PreferenceKeys.COLOR_COMMENT)), null, SWT.BOLD)
                )
        );
        reconciler.setDamager(dr, PerlContentType.COMMENT);
        reconciler.setRepairer(dr, PerlContentType.COMMENT);        

        dr = new DefaultDamagerRepairer(
                new SingleTokenScanner(
                        new TextAttribute(colorManager.getColor(
                                Preference.getPreferenceRGB(PreferenceKeys.COLOR_STRING)))
                )
        );
        reconciler.setDamager(dr, PerlContentType.String);
        reconciler.setRepairer(dr, PerlContentType.String);              

        dr = new DefaultDamagerRepairer(getPerlDefaultScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);             
        
        return reconciler;
        
    };

}
