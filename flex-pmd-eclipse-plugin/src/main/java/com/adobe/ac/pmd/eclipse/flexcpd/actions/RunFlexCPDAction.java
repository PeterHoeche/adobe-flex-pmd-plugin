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

import java.util.logging.Logger;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.adobe.ac.pmd.eclipse.flexcpd.cmd.data.CPDDuplicationsVO;
import com.adobe.ac.pmd.eclipse.flexcpd.view.CPDView;
import com.adobe.ac.pmd.eclipse.utils.FileUtils;

public class RunFlexCPDAction implements IObjectActionDelegate
{
   private static final Logger  LOGGER = Logger.getLogger( RunFlexCPDAction.class.getName() );
   private IStructuredSelection selection;

   public void run( final IAction action )
   {
      if ( selection != null )
      {
         try
         {
            final RunFlexCPDJob job = new RunFlexCPDJob( FileUtils.extractResourceFromSelection( selection.getFirstElement() ) );
            job.addJobChangeListener( new JobChangeAdapter()
            {
               @Override
               public void done( final IJobChangeEvent event )
               {
                  if ( event.getResult().isOK() )
                  {
                     updateUIWithPMDResults( job.getProcessResult() );
                  }
               }
            } );
            job.schedule();
         }
         catch ( final Exception e )
         {
            LOGGER.warning( e.toString() );
         }
      }
   }

   public void selectionChanged( final IAction action,
                                 final ISelection selection )
   {
      if ( selection instanceof IStructuredSelection )
      {
         this.selection = ( IStructuredSelection ) selection;
      }
      else
      {
         this.selection = null;
      }
   }

   private void openAndFocusCPDView( final CPDDuplicationsVO duplications )
   {
      final IWorkbench workbench = PlatformUI.getWorkbench();
      final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
      try
      {
         window.getActivePage().showView( CPDView.VIEW_ID );

         final CPDView view = ( CPDView ) window.getActivePage().findView( CPDView.VIEW_ID );
         view.loadDuplications( duplications );
      }
      catch ( final PartInitException e )
      {
      }
   }

   private void updateUIWithPMDResults( final CPDDuplicationsVO duplications )
   {
      Display.getDefault().asyncExec( new Runnable()
      {
         public void run()
         {
            openAndFocusCPDView( duplications );
         }
      } );
   }

   public void setActivePart( final IAction action,
                              final IWorkbenchPart targetPart )
   {
   }
}
