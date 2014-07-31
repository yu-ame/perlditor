package info.yu_ame.perlditor;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class PerlPartitionScanner extends RuleBasedPartitionScanner {
    
    public PerlPartitionScanner() {

        IToken comment = new Token(PerlContentType.COMMENT);
        IToken pod = new Token(PerlContentType.POD);
        IToken string = new Token(PerlContentType.String);
    
        
        IPredicateRule[] rules = new IPredicateRule[7];

        rules[0] = new MultiLineRule("=encoding", "=cut", pod); 
        rules[1] = new MultiLineRule("=pod", "=cut", pod);   
        rules[2] = new MultiLineRule("=head", "=cut", pod); 
        rules[3] = new MultiLineRule("=item", "=cut", pod); 
        rules[4] = new SingleLineRule("#", "", comment);
        rules[5] = new MultiLineRule("\"", "\"", string, '\\' );   
        rules[6] = new MultiLineRule("'", "'", string, '\\' );   
        
        setPredicateRules(rules);
    }
}
