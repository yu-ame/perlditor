package info.yu_ame.perlditor;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PerlPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage {
    public PerlPreferencePage() {
        super(GRID);
        setPreferenceStore(
          Activator.getDefault().getPreferenceStore());
    }
    protected void createFieldEditors() {
        addField(new StringFieldEditor(PreferenceKeys.SSH_HOST,"ssh host",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.SSH_PORT,"ssh port",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.SSH_KEY_FILE,"ssh keyfile path\n(local full path)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.SSH_USER,"ssh user",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.SSH_PASSPHRASE,"ssh passphrase\n(no crypt!)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.MODULE_LIBS,"remote perl module libs\n(/hoge/fuga;/hoge/fuga)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.PROJECT_LIBS,"project libs\n(/hoge/fuga;/hoge/fuga)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.DOUBLE_CLICK_PLUGINS,"double click plugins\n(com.exsample.XXXClassA;com.exsample.XXXClassB)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.LOCAL_TMPL_FILE_DIR,"local temp file directory\n(local temp directory full path)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.TEST_FILE_LIB,"project test file libs\n(t/lib;hoge/fuga)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.COLOR_BACKGROUND,"color: background\n(255,255,255)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.COLOR_HIGHLIGHT,"color: highlight\n(255,255,255)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.COLOR_POD,"color: pod\n(255,255,255)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.COLOR_COMMENT,"color: comment\n(255,255,255)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.COLOR_STRING,"color: string\n(255,255,255)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.COLOR_OTHER,"color: other\n(255,255,255)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.COLOR_KEYWORD,"color: keyword\n(255,255,255)",getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceKeys.COLOR_FUNCTION,"color: function\n(255,255,255)",getFieldEditorParent()));

    }

    public void init(IWorkbench workbench) {
    }
}

