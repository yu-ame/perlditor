package info.yu_ame.perlditor;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class PerlPodRule implements IPredicateRule {

    private IToken token;
    
    public PerlPodRule(IToken token) {
        this.token = token;
    }

    public IToken evaluate(ICharacterScanner scanner, boolean resume) {
        
        int cnt = 0;
        
        if(resume){
            int fl = 0;
            while(true){
                scanner.unread();
                int c = scanner.read();
                scanner.unread();
                cnt--;
                if(fl == 0 && c == 't') fl = 1;
                else if( fl == 1 && c == 'u') fl = 2;
                else if( fl == 2 && c == 'c') fl = 3;
                else if( fl == 3 && c == '=') fl = 4;
                else if( fl == 4 && c == '\n'){
                    break;
                }else{
                    fl = 0;
                }                
                if(c == ICharacterScanner.EOF){
                    break;
                }            
                if(scanner.getColumn() <= 0){
                    break;
                }
            }

            scanner.unread();
            cnt--;
        }
        
        boolean is_ok = false;
        if(scanner.read() == '\n'){
            if(scanner.read() == '='){
                int fl = 0;
                while(true){
                    int c = scanner.read();
                    cnt++;
                    if(fl == 0 && c == '\n') fl = 1;
                    else if(fl == 1 && c == '=') fl = 2;
                    else if(fl == 2 && c == 'c') fl = 3;
                    else if(fl == 3 && c == 'u') fl = 4;
                    else if(fl == 4 && c == 't'){
                        is_ok = true;
                        break;
                    }
                    else{
                        fl = 0;
                    }
                    if(c == ICharacterScanner.EOF){
                        break;
                    }
                }
            } 
            cnt++;
        }
        cnt++;
        if(is_ok){
            return token;
        }
        if(cnt < 0){
            for(int i = 0; i > cnt; i--){
                scanner.read();
            }            
        }else{
            for(int i = 0; i < cnt; i++){
                scanner.unread();
            }
        }
        return Token.UNDEFINED;
    }

    @Override
    public IToken getSuccessToken() {
        return token;
    }

    @Override
    public IToken evaluate(ICharacterScanner scanner) {
        return evaluate(scanner, false);
    }
}
