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
package com.adobe.ac.pmd.eclipse.flexpmd.view.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.adobe.ac.pmd.eclipse.flexpmd.actions.RunFlexPMDJob;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.PmdViolationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.utils.FlexPMDMarkerUtils;
import com.adobe.ac.pmd.eclipse.flexpmd.view.OutlineView;
import com.adobe.ac.pmd.eclipse.utils.FileUtils;
import com.adobe.ac.pmd.eclipse.utils.changeDetector.ActivePartContentChangeDetector;
import com.adobe.ac.pmd.eclipse.utils.changeDetector.IActiveEditorContentChangeListener;

public class MonitorizeActivePartAction extends Action implements IActiveEditorContentChangeListener
{
   private ActivePartContentChangeDetector changeDetector;
   private boolean                         updatingMarkers;
   private OutlineView                     view;

   public MonitorizeActivePartAction()
   {
   }

   public MonitorizeActivePartAction( final OutlineView outlineView )
   {
      super( "Monitorize", IAction.AS_CHECK_BOX );
      view = outlineView;
      changeDetector = new ActivePartContentChangeDetector( getPartService(), this );

      setImageDescriptor( PlatformUI.getWorkbench()
                                    .getSharedImages()
                                    .getImageDescriptor( ISharedImages.IMG_ELCL_SYNCED ) );

      setDisabledImageDescriptor( PlatformUI.getWorkbench()
                                            .getSharedImages()
                                            .getImageDescriptor( ISharedImages.IMG_ELCL_SYNCED_DISABLED ) );

   }

   public void activePartContentChange( final IFile file )
   {
      if ( !updatingMarkers
            && FileUtils.isFlexFile( file.getLocation().getFileExtension() ) )
      {
         final RunFlexPMDJob job = new RunFlexPMDJob( file.getLocation().toFile() );
         job.addJobChangeListener( new JobChangeAdapter()
         {
            @Override
            public void done( final IJobChangeEvent event )
            {
               if ( event.getResult().isOK() )
               {
                  updateUIWithPMDResults( file,
                                          job.getProcessResult() );
               }
            }
         } );
         job.schedule();
      }
   }

   public void disable()
   {
      if ( isChecked() )
      {
         setChecked( false );
      }

      changeDetector.disable();
   }

   public void enable()
   {
      if ( !isChecked() )
      {
         setChecked( true );
      }
      changeDetector.enable();
   }

   private IPartService getPartService()
   {
      final IWorkbenchWindow workbenchWindow = view.getViewSite().getWorkbenchWindow();
      return workbenchWindow.getPartService();
   }

   @Override
   public void run()
   {
      if ( isChecked() )
      {
         enable();
      }
      else
      {
         disable();
      }
   }

   protected void updateUIWithPMDResults( final IFile file,
                                          final PmdViolationsVO violations )
   {
      Display.getDefault().asyncExec( new Runnable()
      {
         public void run()
         {
            updatingMarkers = true;
            FlexPMDMarkerUtils.cleanMarkers( file );
            FlexPMDMarkerUtils.addMarkers( violations );
            updatingMarkers = false;

            view.loadMarkers( FlexPMDMarkerUtils.getMarkers( file ) );
         }
      } );
   }
}
