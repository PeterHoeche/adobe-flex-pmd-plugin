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
import java.util.Arrays;

import com.adobe.ac.pmd.eclipse.FlexPMDPlugin;
import com.adobe.ac.pmd.eclipse.flexpmd.FlexPMDKeys;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.PmdViolationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.preferences.FlexPMDPreferences;
import com.adobe.ac.pmd.eclipse.utils.FileUtils;
import com.adobe.ac.pmd.eclipse.utils.IProcessable;
import com.adobe.ac.pmd.eclipse.utils.cli.SysCommandExecutor;
import com.adobe.ac.pmd.eclipse.utils.cli.SysCommandExecutorFactory;

public class FlexPMD implements IProcessable< PmdViolationsVO >
{
   public PmdViolationsVO process( final File resource ) throws FlexPmdExecutionException
   {
      final FlexPMDPreferences conf = FlexPMDPreferences.get();

      if ( "".equals( conf.getPmdCommandLinePath() ) )
      {
         throw new FlexPmdExecutionException( FlexPMDKeys.MISSING_PMD_COMMAND_LINE );
      }

      if ( "".equals( conf.getJavaVmCommandLine() ) )
      {
         throw new FlexPmdExecutionException( FlexPMDKeys.MISSING_JAVA_COMMAND_LINE );
      }

      final SysCommandExecutor executor = SysCommandExecutorFactory.newInstance( resource );

      PmdViolationsVO pmdResults = null;

      File outPutDirectory;
      try
      {
         outPutDirectory = FileUtils.createTemporaryDirectory();

         String[] commandLine = new String[]
         { "java",
                     conf.getJavaVmCommandLine(),
                     "-jar",
                     conf.getPmdCommandLinePath(),
                     "-s",
                     resource.getPath(),
                     "-o",
                     outPutDirectory.getAbsolutePath() };

         if ( "".equals( conf.getRulesetPath() ) )
         {
            commandLine[ commandLine.length - 1 ] = "-r";
            commandLine[ commandLine.length - 1 ] = conf.getRulesetPath();
         }

         FlexPMDPlugin.getDefault().logInfo( Arrays.toString( commandLine ) );
         executor.runCommand( commandLine );

         final File reportFile = new File( outPutDirectory.getAbsoluteFile()
               + "/pmd.xml" );

         pmdResults = FlexPMDResultsParser.parse( reportFile );
      }
      catch ( final Exception e )
      {
         FlexPMDPlugin.getDefault().logError( "Error running FlexPMD",
                                              e );
      }

      return pmdResults;
   }
}
