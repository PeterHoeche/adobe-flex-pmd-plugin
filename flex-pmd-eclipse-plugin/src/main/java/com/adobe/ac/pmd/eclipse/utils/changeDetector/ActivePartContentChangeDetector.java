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
package com.adobe.ac.pmd.eclipse.utils.changeDetector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;

import com.adobe.ac.pmd.eclipse.flexpmd.utils.FlexPMDMarkerUtils;

public class ActivePartContentChangeDetector implements IPartListener2
{
   private class ResourceChangeListener implements IResourceChangeListener
   {
      private boolean isPostChangeEvent( final IResourceChangeEvent event )
      {
         return event.getType() == IResourceChangeEvent.POST_CHANGE;
      }

      private boolean isAutoBuilding( final IResourceChangeEvent event )
      {
         boolean building = false;

         if ( event.getSource() instanceof IWorkspace )
         {
            IWorkspace workspace = ( IWorkspace ) event.getSource();
            building = workspace.isAutoBuilding();
         }

         return building;
      }

      private boolean activeEditorContentHasChanged( final IResourceChangeEvent event )
      {
         final IResourceDelta docDelta = event.getDelta().findMember( activeEditorPath );

         int contentModified = 0;

         if ( docDelta != null )
         {
            contentModified = docDelta.getFlags()
                  & IResourceDelta.CONTENT;
         }

         return contentModified == 0 ? false
                                    : true;
      }

      private boolean activeEditorMarkersHaveChanged( final IResourceChangeEvent event )
      {
         final IMarkerDelta[] docDelta = event.findMarkerDeltas( FlexPMDMarkerUtils.MARKER_TYPE,
                                                                 false );

         return docDelta.length == 0 ? false
                                    : true;
      }

      public void resourceChanged( final IResourceChangeEvent event )
      {
         if ( isPostChangeEvent( event ) )
         {
            if ( !isAutoBuilding( event )
                  && activeEditorContentHasChanged( event ) )
            {
               notifyActivePartContentChange();
            }
            else if ( activeEditorMarkersHaveChanged( event ) )
            {
               notifyActivePartMarkersChange();
            }
         }

      }
   }

   private IPath                                activeEditorPath;
   private IFile                                activeFile;
   private final IActivePmdEditorChangeListener changeListener;
   private final IPartService                   partService;
   private final ResourceChangeListener         resourceListener;

   public ActivePartContentChangeDetector( final IPartService iPartService,
                                           final IActivePmdEditorChangeListener changeListener )
   {
      resourceListener = new ResourceChangeListener();
      partService = iPartService;
      this.changeListener = changeListener;
   }

   public void disable()
   {
      ResourcesPlugin.getWorkspace().removeResourceChangeListener( resourceListener );
      partService.removePartListener( this );
   }

   public void enable()
   {
      ResourcesPlugin.getWorkspace().addResourceChangeListener( resourceListener );
      partService.addPartListener( this );
   }

   private void notifyActivePartContentChange()
   {
      if ( changeListener != null )
      {
         changeListener.activePartContentChange( activeFile );
      }
   }

   private void notifyActivePartMarkersChange()
   {
      if ( changeListener != null )
      {
         changeListener.activePartMarkersChange( activeFile );
      }
   }

   public void partActivated( final IWorkbenchPartReference part )
   {
      final IWorkbenchPage page = part.getPage();

      if ( page != null )
      {
         final IEditorPart editor = page.getActiveEditor();

         if ( editor != null )
         {
            final IEditorInput input = editor.getEditorInput();

            if ( input instanceof IFileEditorInput )
            {
               final IFileEditorInput fileInput = ( IFileEditorInput ) input;
               activeEditorPath = fileInput.getFile().getFullPath();
               activeFile = fileInput.getFile();
            }
         }
      }
   }

   public void partBroughtToTop( final IWorkbenchPartReference part )
   {
   }

   public void partClosed( final IWorkbenchPartReference arg0 )
   {
   }

   public void partDeactivated( final IWorkbenchPartReference arg0 )
   {
   }

   public void partHidden( final IWorkbenchPartReference arg0 )
   {
   }

   public void partInputChanged( final IWorkbenchPartReference arg0 )
   {
   }

   public void partOpened( final IWorkbenchPartReference arg0 )
   {
   }

   public void partVisible( final IWorkbenchPartReference arg0 )
   {
   }
}
