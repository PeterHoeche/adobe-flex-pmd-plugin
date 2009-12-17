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
package com.adobe.ac.pmd.eclipse.flexcpd.actions;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.adobe.ac.pmd.eclipse.FlexPMDPlugin;
import com.adobe.ac.pmd.eclipse.flexcpd.FlexCPDKeys;
import com.adobe.ac.pmd.eclipse.flexcpd.cmd.FlexCPD;
import com.adobe.ac.pmd.eclipse.flexcpd.cmd.data.CPDDuplicationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.FlexPmdExecutionException;
import com.adobe.ac.pmd.eclipse.utils.IProcessable;

public class RunFlexCPDJob extends Job
{
   private String                                  errorMessage;
   private final IProcessable< CPDDuplicationsVO > flexCpd;
   private final File                              resource;
   private CPDDuplicationsVO                       result;

   public RunFlexCPDJob( final File file )
   {
      this( file, new FlexCPD() );
   }

   public RunFlexCPDJob( final File resource,
                         final IProcessable< CPDDuplicationsVO > flexCPD )
   {
      super( FlexCPDKeys.RUNNING_FLEXCPD );

      this.resource = resource;
      flexCpd = flexCPD;
   }

   public String getErrorMessage()
   {
      return errorMessage;
   }

   public CPDDuplicationsVO getProcessResult()
   {
      return result;
   }

   @Override
   public IStatus run( final IProgressMonitor monitor )
   {
      try
      {
         result = flexCpd.process( resource );
         return Status.OK_STATUS;
      }
      catch ( final FlexPmdExecutionException e )
      {
         errorMessage = e.getMessage();
         FlexPMDPlugin.getDefault().showError( "Error running FlexCPD process",
                                               e );

         return Status.CANCEL_STATUS;
      }
   }
}