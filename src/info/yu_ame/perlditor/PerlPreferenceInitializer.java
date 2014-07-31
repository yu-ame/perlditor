package info.yu_ame.perlditor;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PerlPreferenceInitializer   extends AbstractPreferenceInitializer {
    public void initializeDefaultPreferences() {
        IPreferenceStore store =
           Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceKeys.SSH_HOST, "");
        store.setDefault(PreferenceKeys.SSH_PORT, "");
        store.setDefault(PreferenceKeys.SSH_KEY_FILE, "");
        store.setDefault(PreferenceKeys.SSH_USER, "");
        store.setDefault(PreferenceKeys.SSH_PASSPHRASE, "");
        store.setDefault(PreferenceKeys.MODULE_LIBS, "");
        store.setDefault(PreferenceKeys.PROJECT_LIBS, "");
        store.setDefault(PreferenceKeys.DOUBLE_CLICK_PLUGINS, "");
        store.setDefault(PreferenceKeys.LOCAL_TMPL_FILE_DIR, "");
        store.setDefault(PreferenceKeys.TEST_FILE_LIB, "");
        store.setDefault(PreferenceKeys.COLOR_BACKGROUND, "0,0,0");
        store.setDefault(PreferenceKeys.COLOR_HIGHLIGHT, "60,60,60");
        store.setDefault(PreferenceKeys.COLOR_POD, "255,183,132");
        store.setDefault(PreferenceKeys.COLOR_COMMENT, "255,183,132");
        store.setDefault(PreferenceKeys.COLOR_STRING, "224,122,122");
        store.setDefault(PreferenceKeys.COLOR_OTHER, "122,224,144");
        store.setDefault(PreferenceKeys.COLOR_KEYWORD, "122,224,255");
        store.setDefault(PreferenceKeys.COLOR_FUNCTION, "122,224,255");
    }

}
