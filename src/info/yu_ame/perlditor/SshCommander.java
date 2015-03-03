package info.yu_ame.perlditor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class SshCommander {
    
    private static SshCommander sshCommander;
    private Connection conn;
    
    private SshCommander(){
        try{
            conn = new Connection(Preference.getString(PreferenceKeys.SSH_HOST),
                    Integer.valueOf(Preference.getString(PreferenceKeys.SSH_PORT)));
            conn.connect();
            File pemfile = new File(Preference.getString(PreferenceKeys.SSH_KEY_FILE));
            conn.authenticateWithPublicKey(Preference.getString(PreferenceKeys.SSH_USER), pemfile, Preference.getString(PreferenceKeys.SSH_PASSPHRASE));          
        
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static SshCommander getInstance(){
        if(sshCommander == null){
            sshCommander = new SshCommander();
        }
        return sshCommander;
    }

    
    
    
    public String execute(String command){
        System.out.println("command:" + command);
        String str = "";
        String err = "";
        Session session = null;
        try{
            session = conn.openSession();
            session.execCommand(command);
            str = streamToString(session.getStdout());         
            err = streamToString(session.getStderr());
        }catch(Exception e){
            e.printStackTrace();            
        }finally{
            if(session != null){
                session.close();
            }
        }
        return str;
    }
    
    
    private String streamToString(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len;
        while ((len = in.read(buf, 0, 1024))>0) out.write(buf, 0, len);
        in.close();
        String r = out.toString();
        out.close();
        return r;
    }      
    
}
