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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.adobe.ac.pmd.eclipse.FlexPMDPlugin;
import com.adobe.ac.pmd.eclipse.flexpmd.FlexPMDKeys;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.PmdViolationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.utils.FlexPMDMarkerUtils;
import com.adobe.ac.pmd.eclipse.flexpmd.view.ViolationsView;
import com.adobe.ac.pmd.eclipse.utils.FileUtils;
import com.adobe.ac.pmd.eclipse.utils.MessageUtils;

public class RunFlexPmdAction extends AbstractHandler implements IActionDelegate
{
   private IStructuredSelection selection;

   private void activateFlexPMDView( final PmdViolationsVO violations )
   {
      final IWorkbench workbench = PlatformUI.getWorkbench();
      final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

      try
      {
         window.getActivePage().showView( ViolationsView.VIEW_ID );
         final ViolationsView view = ( ViolationsView ) window.getActivePage()
                                                              .findView( ViolationsView.VIEW_ID );
         view.loadViolations( violations );
      }
      catch ( final PartInitException e )
      {
         FlexPMDPlugin.getDefault().logError( "Error activating FlexPMDView",
                                              e );
      }
   }

   public Object execute( final ExecutionEvent event ) throws ExecutionException
   {

      final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked( event );
      final IStructuredSelection selection = ( IStructuredSelection ) window.getSelectionService()
                                                                            .getSelection();

      runPmdOnSelection( selection );
      return null;
   }

   public void run( final IAction iaction )
   {
      if ( selection != null )
      {
         runPmdOnSelection( selection );
      }
   }

   private void runPmdOnSelection( final IStructuredSelection selection )
   {
      if ( selection == null )
      {
         FlexPMDPlugin.getDefault().showError( MessageUtils.getString( FlexPMDKeys.NO_SELECTED_RESOURCE ),
                                               null );
      }
      else
      {
         final RunFlexPMDJob job = new RunFlexPMDJob( FileUtils.extractResourceFromSelection( selection.getFirstElement() ) );
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
   }

   public void selectionChanged( final IAction iaction,
                                 final ISelection iselection )
   {
      if ( iselection instanceof IStructuredSelection )
      {
         selection = ( IStructuredSelection ) iselection;
      }
      else
      {
         selection = null;
      }
   }

   private void updateUIWithPMDResults( final PmdViolationsVO violations )
   {
      Display.getDefault().asyncExec( new Runnable()
      {
         public void run()
         {
            Job job = FlexPMDMarkerUtils.addMarkers( violations );
            job.schedule();
            activateFlexPMDView( violations );
         }
      } );
   }
}
