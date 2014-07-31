package info.yu_ame.perlditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

public class PackageListRunnable extends TimerTask{

    private List<String> list = new ArrayList<String>();
    
    @Override
    public void run() {
        this.loadAssistStrings();
    }
    
    public List<String> getList(){
        return this.list;
    }
    
    private void loadAssistStrings(){
        list = new ArrayList<String>();
        
        HashMap<String, String> check = new HashMap<String, String>();
        
        SshCommander sc = SshCommander.getInstance();
        String[] modules  = Preference.getString(PreferenceKeys.MODULE_LIBS).split(";");
        for(int i = 0;i < modules.length; i++){
            String path = modules[i];
            String str = sc.execute("find " + path + " -name \"*.pm\"");
            String[] files = str.split("\n");
            for(int j = 0; j < files.length; j++){
                String tmp = files[j].replaceFirst(path + "/", "");
                tmp = tmp.replaceAll("/", "::");
                tmp = tmp.replaceFirst("\\.pm", "");
                if(check.get(tmp) == null){
                    check.put(tmp, "1");
                    list.add(tmp);
                }
            }        	
        }
    }
}
