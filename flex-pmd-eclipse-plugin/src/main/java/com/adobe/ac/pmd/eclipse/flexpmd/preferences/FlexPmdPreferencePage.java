/**
 *    Copyright (c) 2009, Adobe Systems, Incorporated
 *    All rights reserved.
 *
 *    Redistribution  and  use  in  source  and  binary  forms, with or without
 *    modification,  are  permitted  provided  that  the  following  conditions
 *    are met:
 *
 *      * Redistributions  of  source  code  must  retain  the  above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions  in  binary  form  must reproduce the above copyright
 *        notice,  this  list  of  conditions  and  the following disclaimer in
 *        the    documentation   and/or   other  materials  provided  with  the
 *        distribution.
 *      * Neither the name of the Adobe Systems, Incorporated. nor the names of
 *        its  contributors  may be used to endorse or promote products derived
 *        from this software without specific prior written permission.
 *
 *    THIS  SOFTWARE  IS  PROVIDED  BY THE  COPYRIGHT  HOLDERS AND CONTRIBUTORS
 *    "AS IS"  AND  ANY  EXPRESS  OR  IMPLIED  WARRANTIES,  INCLUDING,  BUT NOT
 *    LIMITED  TO,  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *    PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 *    OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,  INCIDENTAL,  SPECIAL,
 *    EXEMPLARY,  OR  CONSEQUENTIAL  DAMAGES  (INCLUDING,  BUT  NOT  LIMITED TO,
 *    PROCUREMENT  OF  SUBSTITUTE   GOODS  OR   SERVICES;  LOSS  OF  USE,  DATA,
 *    OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *    LIABILITY,  WHETHER  IN  CONTRACT,  STRICT  LIABILITY, OR TORT (INCLUDING
 *    NEGLIGENCE  OR  OTHERWISE)  ARISING  IN  ANY  WAY  OUT OF THE USE OF THIS
 *    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.adobe.ac.pmd.eclipse.flexpmd.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.adobe.ac.pmd.eclipse.FlexPMDPlugin;
import com.adobe.ac.pmd.eclipse.utils.MessageUtils;

public class FlexPmdPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
   private static final String COMMAND_LINE_INSTALLATION_KEY     = "FlexPmdPreferencePage.FlexPmdCommandLineInstallation";
   private static final String CPD_COMMAND_LINE_INSTALLATION_KEY = "FlexPmdPreferencePage.FlexCpdCommandLineInstallation";
   private static final String CPD_MINIMUM_TOKENS                = "FlexPmdPreferencePage.MinimumTokens";
   private static final String CUSTOM_RULESET_KEY                = "FlexPmdPreferencePage.customRuleset";
   private static final String FIELD_EDITOR_VALUE                = "field_editor_value";
   private static final String JAVA_COMMAND_LINE_ARGUMENTS_KEY   = "FlexPmdPreferencePage.JavaCommandLineArguments";

   private FileFieldEditor     cpdInstallationPathField;
   private IntegerFieldEditor  cpdMinimumTokensField;
   private FileFieldEditor     flexPmdInstallationPathField;
   private StringFieldEditor   javaCommandLineArgumentsField;

   public FlexPmdPreferencePage()
   {
      super( GRID );
      setPreferenceStore( FlexPMDPlugin.getDefault().getPreferenceStore() );
   }

   private void addCpdInstallationField()
   {
      cpdInstallationPathField = new FileFieldEditor( PreferenceConstants.CPD_COMMAND_LINE_PATH,
                                                      MessageUtils.getString( CPD_COMMAND_LINE_INSTALLATION_KEY ),
                                                      getFieldEditorParent() );
      addField( cpdInstallationPathField );
   }

   private void addCpdTokensField()
   {
      cpdMinimumTokensField = new IntegerFieldEditor( PreferenceConstants.CPD_MINIMUM_TOKENS,
                                                      MessageUtils.getString( CPD_MINIMUM_TOKENS ),
                                                      getFieldEditorParent() );
      addField( cpdMinimumTokensField );
   }

   private void addCustomRulesetField()
   {
      final FileFieldEditor customRuleSetField = new FileFieldEditor( PreferenceConstants.RULESET,
                                                                      MessageUtils.getString( CUSTOM_RULESET_KEY ),
                                                                      getFieldEditorParent() );
      customRuleSetField.setFileExtensions( new String[]
      { "*.xml" } );
      addField( customRuleSetField );
   }

   private void addJavaCommandLineField()
   {
      javaCommandLineArgumentsField = new StringFieldEditor( PreferenceConstants.JAVA_COMMAND_LINE_ARGUMENTS,
                                                    MessageUtils.getString( JAVA_COMMAND_LINE_ARGUMENTS_KEY ),
                                                    getFieldEditorParent() );
      addField( javaCommandLineArgumentsField );
   }

   private void addPmdInstallationField()
   {
      flexPmdInstallationPathField = new FileFieldEditor( PreferenceConstants.COMMAND_LINE_PATH,
                                                          MessageUtils.getString( COMMAND_LINE_INSTALLATION_KEY ),
                                                          getFieldEditorParent() );

      flexPmdInstallationPathField.setFileExtensions( new String[]
      { "*.jar" } );

      addField( flexPmdInstallationPathField );
   }

   @Override
   public void createFieldEditors()
   {
      addPmdInstallationField();
      addCustomRulesetField();
      addJavaCommandLineField();
      addCpdInstallationField();
      addCpdTokensField();

   }

   public void init( final IWorkbench workbench )
   {
   }

   @Override
   public void propertyChange( final PropertyChangeEvent event )
   {
      if ( event.getProperty().equals( FIELD_EDITOR_VALUE ) )
      {
         if ( event.getSource() == javaCommandLineArgumentsField )
         {
            validate( PreferencesValidator.validateJavaCommandLine( javaCommandLineArgumentsField.getStringValue() )
                                          .getKey(),
                      event );
         }

         if ( event.getSource() == flexPmdInstallationPathField )
         {
            validate( PreferencesValidator.validateFlexPmdInstallation( flexPmdInstallationPathField.getStringValue() )
                                          .getKey(),
                      event );
         }

         if ( event.getSource() == cpdInstallationPathField )
         {
            validate( PreferencesValidator.validateFlexPmdInstallation( cpdInstallationPathField.getStringValue() )
                                          .getKey(),
                      event );
         }

         if ( event.getSource() == cpdMinimumTokensField )
         {
            setValid( true );
            setErrorMessage( null );
            super.propertyChange( event );
         }
      }
   }

   private void validate( final String errorMessageKey,
                          final PropertyChangeEvent event )
   {
      if ( "".equals( errorMessageKey ) )
      {
         setValid( true );
         setErrorMessage( null );
         super.propertyChange( event );
      }
      else
      {
         setValid( false );
         setErrorMessage( MessageUtils.getString( errorMessageKey ) );
      }
   }
}
