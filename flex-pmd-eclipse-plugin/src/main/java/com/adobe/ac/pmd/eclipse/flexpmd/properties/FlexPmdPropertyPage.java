package com.adobe.ac.pmd.eclipse.flexpmd.properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.adobe.ac.pmd.eclipse.FlexPMDPlugin;

public class FlexPmdPropertyPage extends PropertyPage 
{
	private static final String DEFAULT_RULESET_PATH = "";
	
	private Button enableProjectSpecificSettingsButton;

	private Label rulesetLabel;
	private Text rulesetEditorField;
	private Button rulesetBrowseButton;

	public FlexPmdPropertyPage()
	{
		super();
	}

	@Override
	public boolean performOk() 
	{
		try 
		{
			IResource resource = ( IResource ) getElement();
			
			resource.setPersistentProperty( new QualifiedName( "", PropertyConstants.RULESET_PATH ), rulesetEditorField.getText() );
			resource.setPersistentProperty( new QualifiedName( "", PropertyConstants.PROJECT_SPECIFIC_FLEX_PMD_PROPERTY ), "" + enableProjectSpecificSettingsButton.getSelection() );
		} 
		catch ( CoreException e ) 
		{
			return false;
		}
		
		return true;
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents( Composite parent ) 
	{
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout();
		composite.setLayout( layout );
		
		GridData gridData = new GridData( GridData.FILL );
		gridData.grabExcessHorizontalSpace = true;
		
		composite.setLayoutData( gridData );

		addProjectSpecificSettingsCheckBox( composite );
		
		addRulesetSettings( composite );
		
		return composite;
	}

	@Override
	protected void performDefaults() 
	{
		super.performDefaults();
		
		enableProjectSpecificSettingsButton.setSelection( false );
		rulesetEditorField.setText( DEFAULT_RULESET_PATH );
	}
	
	private void addProjectSpecificSettingsCheckBox( Composite composite ) 
	{
		enableProjectSpecificSettingsButton = new Button( composite, SWT.CHECK );
		enableProjectSpecificSettingsButton.setText( "Enable project specific settings" );
		enableProjectSpecificSettingsButton.addListener( SWT.Selection, new Listener() 
		{
			@Override
			public void handleEvent( Event arg0 ) 
			{
				toggleEnableProjectSettingsState();
			}
		} );
		
		try 
		{
			String selection = ( ( IResource ) getElement() )
									.getPersistentProperty( new QualifiedName( "", PropertyConstants.PROJECT_SPECIFIC_FLEX_PMD_PROPERTY ) );
			
			enableProjectSpecificSettingsButton.setSelection( Boolean.parseBoolean( selection ) );
		} 
		catch (CoreException e) 
		{
			enableProjectSpecificSettingsButton.setSelection( false );
		}
	}
	
	private void toggleEnableProjectSettingsState() 
	{
		rulesetLabel.setEnabled( isProjectSpecificSettingsEnabled() );
		rulesetEditorField.setEnabled( isProjectSpecificSettingsEnabled() );
		rulesetBrowseButton.setEnabled( isProjectSpecificSettingsEnabled() );
	}
	
	private boolean isProjectSpecificSettingsEnabled()
	{
		return enableProjectSpecificSettingsButton.getSelection();
	}

	private void addRulesetSettings( Composite parent ) 
	{
		Composite composite = createDefaultComposite( parent );

		rulesetLabel = new Label( composite, SWT.NONE );
		rulesetLabel.setText( "FlexPMD custom ruleset" );
		
		rulesetEditorField = new Text( composite, SWT.SINGLE | SWT.BORDER );
		rulesetEditorField.setEnabled( isProjectSpecificSettingsEnabled() );
		
		GridData gridData = new GridData();
		gridData.widthHint = convertWidthInCharsToPixels( 60 );
		rulesetEditorField.setLayoutData(gridData);
		
		try 
		{
			String rulesetPath = ( ( IResource ) getElement() ).getPersistentProperty( new QualifiedName( "", PropertyConstants.RULESET_PATH ) );
			rulesetEditorField.setText( ( rulesetPath != null ) ? rulesetPath : DEFAULT_RULESET_PATH );
		} 
		catch ( CoreException e ) 
		{
			rulesetEditorField.setText( DEFAULT_RULESET_PATH );
		}
		
		rulesetBrowseButton = new Button( composite, SWT.NONE );
		rulesetBrowseButton.setEnabled( isProjectSpecificSettingsEnabled() );
		rulesetBrowseButton.setText( "Browse..." );
		rulesetBrowseButton.setLayoutData( new GridData( SWT.RIGHT ) );
		rulesetBrowseButton.addListener( SWT.Selection, new Listener() 
		{
			@Override
			public void handleEvent( Event arg0 ) 
			{
			    ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog( getShell(), new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider() );
				
			    dialog.setInput( ( IResource ) getElement() );
				dialog.setTitle( "Select FlexPMD custom ruleset" );
				
				dialog.addFilter( new ViewerFilter() 
				{
					
					@Override
					public boolean select( Viewer viewer, Object parentElement, Object element ) 
					{
						if ( element instanceof IFile && ( ( IFile)element ).getFullPath().getFileExtension().equals( "xml" ) )
							return true;
						else
							return false;
					}
				} );
				
				dialog.setValidator( new ISelectionStatusValidator() 
				{
					@Override
					public IStatus validate( Object[] selection ) 
					{
						if ( selection.length == 1 && selection[0] instanceof IFile ) 
						      return new Status( IStatus.OK, FlexPMDPlugin.PLUGIN_ID, 0, "", null );
						
						return new Status( IStatus.ERROR, FlexPMDPlugin.PLUGIN_ID, 0, "Please select the ruleset file!", null );
					}
				} );
				
				if ( dialog.open() == Window.OK ) 
				{
					IFile result = ( IFile )dialog.getResult()[0];
					
					rulesetEditorField.setText( result.getFullPath().toString() );
				}
			}
		} );
	}
	
	private Composite createDefaultComposite(Composite parent) 
	{
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout( layout );

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData( data );

		return composite;
	}
}