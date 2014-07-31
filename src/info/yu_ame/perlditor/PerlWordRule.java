package info.yu_ame.perlditor;

import java.util.HashMap;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class PerlWordRule implements IRule {

    private IToken token;
    private HashMap<String, Boolean> check = new HashMap<String, Boolean>(); 
    
    public PerlWordRule(String[] words,IToken token) {
        this.token = token;
        for(int i = 0; i < words.length; i++){
            check.put(words[i], new Boolean(true));
        }
    }    
    
    @Override
    public IToken evaluate(ICharacterScanner scanner) {
        
        scanner.unread();
        int c = scanner.read();
        if(Character.isLetter(c) || c == '_'){
            return Token.UNDEFINED;
        }

        int cnt = 0;
        StringBuilder sb = new StringBuilder();
        while(true){
            c = scanner.read();
            cnt++;
            if(!Character.isLetter(c) && c != '_'){
                break;
            }
            sb.append((char)c);
        }
        if(check.get(sb.toString()) != null){
            scanner.unread();
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

}
