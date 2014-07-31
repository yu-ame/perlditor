package info.yu_ame.perlditor;

import info.yu_ame.perlditor.Activator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;

public class Preference {

    public static Color getPreferenceColor(Device device, String name){
        IPreferenceStore store =
            Activator.getDefault().getPreferenceStore();    	
        String[] rgb = store.getString(name).split(",");
        return new Color(device, new RGB(Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));        
    }
    public static RGB getPreferenceRGB(String name){
        IPreferenceStore store =
            Activator.getDefault().getPreferenceStore();    	
        String[] rgb = store.getString(name).split(",");
        return new RGB(Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2]));        
    }
    public static String getString(String name){
        IPreferenceStore store =
            Activator.getDefault().getPreferenceStore();    	
        return store.getString(name);
    }
}
