package info.yu_ame.perlditor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class PerlPropertyPage extends PropertyPage {

	private Text perlPath;
	private Text projectPath;	
	public static final String KEY_PERL_PATH   = "perl remote path";
	public static final String KEY_PROJECT_PATH = "project remote path";

	
	/**
	 * Constructor for SamplePropertyPage.
	 */
	public PerlPropertyPage() {
		super();
	}


	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
						
		IProject project = (IProject) getElement();

		Composite composite = new Composite(parent, SWT.NONE);
		{
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			composite.setLayout(layout);
		}

		{
			Label label = new Label(composite, SWT.NONE);
			label.setText("perl path");

			perlPath = new Text(composite, SWT.SINGLE | SWT.BORDER);
			perlPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			String value = getValue(project, KEY_PERL_PATH);
			if (value != null) {
				perlPath.setText(value);
			}
		}
		{
			Label label = new Label(composite, SWT.NONE);
			label.setText("project remote path");

			projectPath = new Text(composite, SWT.SINGLE | SWT.BORDER);
			projectPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			String value = getValue(project, KEY_PROJECT_PATH);
			if (value != null) {
				projectPath.setText(value);
			}
		}

		return composite;
	}
	
	public static final String getValue(IProject project, String key) {
		try {
			return project.getPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, key));
		} catch (CoreException e) {
			return "";
		}
	}

	public static final void setValue(IProject project, String key, String value) {
		try {
			project.setPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, key), value);
		} catch (CoreException e) {
		}
	}	


	public boolean performOk() {
		IProject project = (IProject) getElement();

		setValue(project, KEY_PERL_PATH,   perlPath.getText());
		setValue(project, KEY_PROJECT_PATH, projectPath.getText());

		return true;
	}

}