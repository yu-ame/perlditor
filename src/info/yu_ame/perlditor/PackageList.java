package info.yu_ame.perlditor;

import java.util.List;
import java.util.Timer;

public class PackageList {

    private static PackageList p;
    private Timer timer1 = new Timer();
    private PackageListRunnable run = new PackageListRunnable();
    
    private PackageList(){
        timer1.schedule(run, 0, 30000);
    }
    
    public static PackageList getInstance(){
        if(p == null){
            p = new PackageList();           
        }
        return p;
    }
    
    public List<String> getAssistList(){
        return run.getList();
    }
    
        
}
