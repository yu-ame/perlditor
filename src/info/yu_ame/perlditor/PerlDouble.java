package info.yu_ame.perlditor;

import info.yu_ame.perlditor.doubleclick.plugins.Module;
import info.yu_ame.perlditor.doubleclick.plugins.Word;

import java.util.ArrayList;

import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

public class PerlDouble implements ITextDoubleClickStrategy {
    public void doubleClicked(ITextViewer part) {
               
        ArrayList<AbstractDoubleClickPlugin> list = new ArrayList<AbstractDoubleClickPlugin>();
        list.add(new Module());
        
        for(String className: Preference.getString(PreferenceKeys.DOUBLE_CLICK_PLUGINS).split(";")){
            if(className.equals("")){
                continue;
            }
            try {
                Class c = Class.forName(className);
                AbstractDoubleClickPlugin dp = (AbstractDoubleClickPlugin)c.newInstance();
                list.add(dp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        list.add(new Word());

        
        for(AbstractDoubleClickPlugin dp: list){
            if(dp.doubleClicked(part)){
                break;
            }
        }
        
    }


    

}