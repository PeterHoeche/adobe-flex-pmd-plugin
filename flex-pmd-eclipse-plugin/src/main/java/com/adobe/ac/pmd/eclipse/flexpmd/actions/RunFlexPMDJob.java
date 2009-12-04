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
package com.adobe.ac.pmd.eclipse.flexpmd.actions;

import java.io.File;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import com.adobe.ac.pmd.eclipse.flexpmd.FlexPMDKeys;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.FlexPMD;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.FlexPmdExecutionException;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.PmdViolationsVO;
import com.adobe.ac.pmd.eclipse.utils.IProcessable;

public class RunFlexPMDJob extends Job
{
   private static final Logger                   LOGGER = Logger.getLogger( RunFlexPMDJob.class.getName() );
   private String                                errorMessage;
   private final IProcessable< PmdViolationsVO > flexPMD;
   private final File                            resource;
   private PmdViolationsVO                       result;

   public RunFlexPMDJob( final File file )
   {
      this( file, new FlexPMD() );
   }

   public RunFlexPMDJob( final File resource,
                         final IProcessable< PmdViolationsVO > flexPMD )
   {
      super( FlexPMDKeys.RUNNING_FLEXPMD );

      this.resource = resource;
      this.flexPMD = new FlexPMD();
   }

   public String getErrorMessage()
   {
      return errorMessage;
   }

   public PmdViolationsVO getProcessResult()
   {
      return result;
   }

   @Override
   public IStatus run( final IProgressMonitor monitor )
   {
      try
      {
         result = flexPMD.process( resource );
         return Status.OK_STATUS;
      }
      catch ( final FlexPmdExecutionException e )
      {
         errorMessage = e.getMessage();
         LOGGER.warning( errorMessage );

         final Runnable uiAction = new NotifyErrorAction( errorMessage );
         Display.getDefault().asyncExec( uiAction );

         return Status.CANCEL_STATUS;
      }
   }
}
