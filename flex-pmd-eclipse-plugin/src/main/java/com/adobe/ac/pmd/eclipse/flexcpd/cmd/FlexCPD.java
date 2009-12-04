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
package com.adobe.ac.pmd.eclipse.flexcpd.cmd;

import java.io.File;
import java.text.MessageFormat;
import java.util.logging.Logger;

import com.adobe.ac.pmd.eclipse.flexcpd.FlexCPDKeys;
import com.adobe.ac.pmd.eclipse.flexcpd.cmd.data.CPDDuplicationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.FlexPMD;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.FlexPmdExecutionException;
import com.adobe.ac.pmd.eclipse.flexpmd.preferences.FlexPMDPreferences;
import com.adobe.ac.pmd.eclipse.utils.FileUtils;
import com.adobe.ac.pmd.eclipse.utils.IProcessable;
import com.adobe.ac.pmd.eclipse.utils.cli.SysCommandExecutor;
import com.adobe.ac.pmd.eclipse.utils.cli.SysCommandExecutorFactory;

public class FlexCPD implements IProcessable< CPDDuplicationsVO >
{
   private static final Logger LOGGER = Logger.getLogger( FlexPMD.class.getName() );

   public CPDDuplicationsVO process( final File resource ) throws FlexPmdExecutionException
   {
      final FlexPMDPreferences conf = FlexPMDPreferences.get();

      if ( "".equals( conf.getCpdCommandLinePath() ) )
      {
         throw new FlexPmdExecutionException( FlexCPDKeys.MISSING_CPD_COMMAND_LINE );
      }

      final SysCommandExecutor executor = SysCommandExecutorFactory.newInstance( resource,
                                                                                 LOGGER );
      CPDDuplicationsVO cpdResults = null;

      try
      {
         final File outPutDirectory = FileUtils.createTemporaryDirectory();
         final String outputFilePath = outPutDirectory.getAbsolutePath().concat( "/cpd.xml" );

         final String command = MessageFormat.format( "{0} -jar {1} -s . -o {2} -m {3}",
                                                      new Object[]
                                                      { conf.getJavaVmCommandLine(),
                                                                  conf.getCpdCommandLinePath(),
                                                                  outputFilePath,
                                                                  conf.getCpdMinimumLines() } ).toString();
         LOGGER.info( command );
         executor.runCommand( command );

         cpdResults = FlexCPDResultsParser.parse( new File( outputFilePath ) );
      }
      catch ( final Exception e )
      {
         LOGGER.severe( e.getMessage() );
      }

      return cpdResults;
   }
}
