package info.yu_ame.perlditor;

import java.io.File;
import java.io.FileReader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.ide.FileStoreEditorInput;

public class PerlDocumentProvider extends FileDocumentProvider {

    protected IDocument createDocument(Object element) throws CoreException {
        IDocument document = super.createDocument(element);
        if(document == null && element instanceof FileStoreEditorInput){
            FileStoreEditorInput fs = (FileStoreEditorInput)element;
            
            String path = fs.getURI().getPath();
            try{
                File fi = new File(path);
                FileReader fr = new FileReader(fi);
                char[] ch = new char[(int)fi.length()];
                fr.read(ch, 0, (int)fi.length());
                fr.close();
                document= createEmptyDocument();
                document.set(new String(ch));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (document != null) {
            IDocumentPartitioner partitioner =
                new FastPartitioner(
                    new PerlPartitionScanner(),
                    new String[] {
                        PerlContentType.POD,
                        PerlContentType.COMMENT,
                        PerlContentType.String,
                    }
                );
            partitioner.connect(document);
            document.setDocumentPartitioner(partitioner);

        
        }
        return document;
    }
    

    
}
