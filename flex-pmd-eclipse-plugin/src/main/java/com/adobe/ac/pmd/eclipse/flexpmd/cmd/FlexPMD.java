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
package com.adobe.ac.pmd.eclipse.flexpmd.cmd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.tools.ant.types.Commandline;

import com.adobe.ac.pmd.eclipse.FlexPMDPlugin;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.PmdViolationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.preferences.FlexPMDPreferences;
import com.adobe.ac.pmd.eclipse.flexpmd.preferences.PreferencesValidator;
import com.adobe.ac.pmd.eclipse.flexpmd.preferences.PreferencesValidator.PmdInstallationValid;
import com.adobe.ac.pmd.eclipse.utils.FileUtils;
import com.adobe.ac.pmd.eclipse.utils.IProcessable;
import com.adobe.ac.pmd.eclipse.utils.cli.SysCommandExecutor;
import com.adobe.ac.pmd.eclipse.utils.cli.SysCommandExecutorFactory;

public class FlexPMD implements IProcessable< PmdViolationsVO >
{
   private FlexPMDPreferences configuration;
   private ArrayList<String>  commandLine;
   private File               outPutDirectory;
   private File               resource;

   public PmdViolationsVO process( final File resource ) throws FlexPmdExecutionException
   {
      this.resource = resource;

      loadRuntimeConfiguration();

      PmdViolationsVO pmdResults = null;

      try
      {
         createOutputDirectory();
         configureCommandLine();
         executeCommandLine();
         pmdResults = processResultsFile();
      }
      catch ( final Exception e )
      {
         FlexPMDPlugin.getDefault().logError( "Error running FlexPMD",
                                              e );
      }

      return pmdResults;
   }

   private void loadRuntimeConfiguration() throws FlexPmdExecutionException
   {
      configuration = FlexPMDPreferences.get();

      // Validate runtime installation
      String runtimePath = configuration.getPmdCommandLinePath();
      PmdInstallationValid installation = PreferencesValidator.validateFlexPmdInstallation( runtimePath );
      if ( !PmdInstallationValid.VALID.equals( installation ) )
      {
         throw new FlexPmdExecutionException( "FlexPmd.configurationError" );
      }

      // Validate ruleset
      installation = PreferencesValidator.validateFlexPmdRuleset( configuration.getRulesetPath() );
      if ( !PmdInstallationValid.VALID.equals( installation ) )
      {
         throw new FlexPmdExecutionException( installation.getKey() );
      }
   }

   private void createOutputDirectory() throws IOException
   {
      outPutDirectory = FileUtils.createTemporaryDirectory();
   }

   private void configureCommandLine() throws FlexPmdExecutionException
   {
      createCommandLine();
      addJavaInvocationParameters();
      addClasspathParameters();
      addSourceParameters();
      addOutputParameters();
      addCustomRulesetParameters();

      FlexPMDPlugin.getDefault().logInfo( commandLine.toString() );
   }

   private void createCommandLine()
   {
	   commandLine = new ArrayList<String>();
   }

   private boolean hasCustomRuleset()
   {
      return !"".equals( configuration.getRulesetPath() );
   }

   private void addJavaInvocationParameters() throws FlexPmdExecutionException
   {
      commandLine.add( "java" );
      
      String[] jvmCommandLineArgs = Commandline.translateCommandline( configuration.getJavaVmCommandLine() );
      
      for ( String argument:jvmCommandLineArgs )
    	  commandLine.add( argument );
   }

   private void addClasspathParameters()
   {
      String runtimePath = configuration.getPmdCommandLinePath();
      String toolName = PreferencesValidator.getTool( PreferencesValidator.FLEX_PMD_TOOL,
                                                      runtimePath );
      
      String classpath = configuration.getPmdCommandLinePath().concat( "/" ).concat( toolName );
      
      if ( configuration.getClasspath().length() != 0 )
    	  classpath += File.pathSeparator + configuration.getClasspath(); 
      
      commandLine.add( "-classpath" );
      commandLine.add( classpath );
      commandLine.add( "com.adobe.ac.pmd.commandline.FlexPMD" );
   }

   private void addSourceParameters()
   {
	   commandLine.add( "-s" );
	   commandLine.add( resource.getPath() );
   }

   private void addOutputParameters()
   {
	   commandLine.add( "-o" );
	   commandLine.add( outPutDirectory.getAbsolutePath() );
   }

   private void addCustomRulesetParameters()
   {
      if ( hasCustomRuleset() )
      {
         commandLine.add( "-r" );
         commandLine.add( configuration.getRulesetPath() );
      }
   }

   private void executeCommandLine() throws Exception
   {
      final SysCommandExecutor executor = SysCommandExecutorFactory.newInstance( resource );
      executor.runCommand( commandLine.toArray( new String[commandLine.size()] ) );
   }

   private PmdViolationsVO processResultsFile() throws FileNotFoundException
   {
      final File reportFile = new File( outPutDirectory.getAbsoluteFile()
            + "/pmd.xml" );

      PmdViolationsVO pmdResults = FlexPMDResultsParser.parse( reportFile );

      return pmdResults;
   }
}
