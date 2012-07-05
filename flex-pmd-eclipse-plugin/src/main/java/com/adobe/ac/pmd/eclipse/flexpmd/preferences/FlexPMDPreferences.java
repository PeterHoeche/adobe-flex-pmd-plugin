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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;

import com.adobe.ac.pmd.eclipse.FlexPMDPlugin;
import com.adobe.ac.pmd.eclipse.flexpmd.properties.PropertyConstants;

public final class FlexPMDPreferences
{
	private static IProject currentProject = null;
	
   public final static FlexPMDPreferences get()
   {

      final IPreferenceStore preferenceStore = FlexPMDPlugin.getDefault().getPreferenceStore();
      
      final FlexPMDPreferences preferences = new FlexPMDPreferences( preferenceStore.getString( PreferenceConstants.COMMAND_LINE_INSTALLATION_PATH ),
                                     preferenceStore.getString( PreferenceConstants.JAVA_COMMAND_LINE_ARGUMENTS ),
                                     preferenceStore.getString( PreferenceConstants.RULESET ),
                                     preferenceStore.getString( PreferenceConstants.CPD_MINIMUM_TOKENS ) );
      
      setProjectSpecificPreferencesIfNecessary( preferences );
      
      return preferences;
   }
   
   private static void setProjectSpecificPreferencesIfNecessary( FlexPMDPreferences preferences ) 
   {
	   currentProject = null;
	   
	   FlexPMDPlugin.getDefault().getWorkbench().getDisplay().syncExec( new Runnable() 
	   {
		   @Override
		   public void run() 
		   {
			   ISelection selection = FlexPMDPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
				
			   if ( selection instanceof TreeSelection )
			   {
				   TreePath path = ( (TreeSelection) selection ).getPaths()[0];
				   
				   for( int i = 0; i < path.getSegmentCount(); i++ )
				   {
					   if ( path.getSegment( i ) instanceof IProject )
					   {
						   currentProject = ( IProject ) path.getSegment( i );
					   }
				   }
			   }
		   }
	   } );
	   
	   if ( currentProject != null )
	   {
		   try
		   {
			   String specificProjectSettingsEnabled = currentProject.getPersistentProperty( new QualifiedName( "", PropertyConstants.PROJECT_SPECIFIC_FLEX_PMD_PROPERTY ) );
			   
			   if ( Boolean.parseBoolean( specificProjectSettingsEnabled ) )
			   {
				   preferences.setRulesetPath( currentProject.getWorkspace().getRoot().getLocation().toFile().getAbsolutePath() + currentProject.getPersistentProperty( new QualifiedName( "", PropertyConstants.RULESET_PATH ) ) );
			   }
		   }
		   catch ( CoreException e )
		   {
		   }
	   }
   }
   
   private final String cpdMinimumLines;
   private String       javaVmMemory;
   private String       pmdCommandLinePath;

   private String       rulesetPath;

   private FlexPMDPreferences( final String commandLinePathToBeSet,
                               final String javaVmMemoryToBeSet,
                               final String rulesetPathToBeSet,
                               final String cpdMinimumLinesToBeSet )
   {
      pmdCommandLinePath = commandLinePathToBeSet;
      javaVmMemory = javaVmMemoryToBeSet;
      rulesetPath = rulesetPathToBeSet;
      cpdMinimumLines = cpdMinimumLinesToBeSet;
   }

   public String getCpdMinimumLines()
   {
      return cpdMinimumLines;
   }

   public final String getJavaVmCommandLine()
   {
      return javaVmMemory;
   }

   public final String getPmdCommandLinePath()
   {
      return pmdCommandLinePath;
   }

   public final String getRulesetPath()
   {
      return rulesetPath;
   }

   public final void setCommandLinePath( final String commandLinePathToBeSet )
   {
      pmdCommandLinePath = commandLinePathToBeSet;
   }

   public final void setJavaVmMemory( final String javaVmMemoryToBeSet )
   {
      javaVmMemory = javaVmMemoryToBeSet;
   }

   public final void setRulesetPath( final String rulesetPathToBeSet )
   {
      rulesetPath = rulesetPathToBeSet;
   }
}
