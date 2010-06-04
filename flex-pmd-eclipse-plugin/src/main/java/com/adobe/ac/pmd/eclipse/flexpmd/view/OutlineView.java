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
package com.adobe.ac.pmd.eclipse.flexpmd.view;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.adobe.ac.pmd.eclipse.flexpmd.utils.FlexPMDMarkerUtils;
import com.adobe.ac.pmd.eclipse.flexpmd.view.actions.MonitorizeActivePartAction;
import com.adobe.ac.pmd.eclipse.utils.EditorUtils;
import com.adobe.ac.pmd.eclipse.utils.changeDetector.ActivePartChangeDetector;
import com.adobe.ac.pmd.eclipse.utils.changeDetector.IActiveEditorChangeListener;

public class OutlineView extends ViewPart
{
   class ActiveEditorChangeListener implements IActiveEditorChangeListener
   {
      public void partBroughtToTop( final IFile file )
      {
         loadMarkers( FlexPMDMarkerUtils.getMarkers( file ) );
      }

      public void partClosed( final boolean wasLastOpenedEditor )
      {
         if ( wasLastOpenedEditor )
         {
            loadMarkers( new IMarker[]
            {} );
         }
      }
   }

   class ViewContentProvider implements IStructuredContentProvider
   {
      public void dispose()
      {
      }

      public Object[] getElements( final Object parent )
      {
         if ( markers == null )
         {
            return new Object[]
            {};
         }

         return markers;
      }

      public void inputChanged( final Viewer v,
                                final Object oldInput,
                                final Object newInput )
      {
      }
   }

   class ViewLabelProvider extends LabelProvider implements ITableLabelProvider
   {
      public Image getColumnImage( final Object obj,
                                   final int index )
      {
         final IMarker marker = ( IMarker ) obj;
         try
         {
            final int severity = ( Integer ) marker.getAttribute( IMarker.SEVERITY );

            switch ( severity )
            {
            case IMarker.SEVERITY_ERROR:
               return PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJS_ERROR_TSK );
            case IMarker.SEVERITY_WARNING:
               return PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJS_WARN_TSK );
            default:
               return PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJS_INFO_TSK );
            }
         }
         catch ( final CoreException e )
         {
            return null;
         }
      }

      public String getColumnText( final Object obj,
                                   final int index )
      {
         try
         {
            final IMarker marker = ( IMarker ) obj;
            return ( String ) marker.getAttribute( IMarker.MESSAGE );
         }
         catch ( final CoreException e )
         {
            e.printStackTrace();
            return "";
         }
      }
   }

   public static final String         ID = "com.adobe.ac.pmd.eclipse.flexpmd.view.OutlineView";
   private ActivePartChangeDetector   changeDetector;
   private IMarker[]                  markers;

   private IMemento                   memento;

   private MonitorizeActivePartAction monitorizeActivePartAction;

   private TableViewer                viewer;

   private void configureActiveEditorListener()
   {
      changeDetector = new ActivePartChangeDetector( new ActiveEditorChangeListener() );
      getPartService().addPartListener( changeDetector );
   }

   private void configureToolBar()
   {
      final IActionBars actionBars = getViewSite().getActionBars();
      final IToolBarManager toolBar = actionBars.getToolBarManager();
      monitorizeActivePartAction = new MonitorizeActivePartAction( this );
      toolBar.add( monitorizeActivePartAction );
   }

   @Override
   public void createPartControl( final Composite parent )
   {
      viewer = new TableViewer( parent, SWT.MULTI
            | SWT.H_SCROLL | SWT.V_SCROLL );
      viewer.setContentProvider( new ViewContentProvider() );
      viewer.setLabelProvider( new ViewLabelProvider() );
      viewer.setSorter( new ViewerSorter() );
      viewer.setInput( getViewSite() );

      configureToolBar();
      configureActiveEditorListener();
      hookDoubleClickAction();

      restoreState();
   }

   @Override
   public void dispose()
   {
      monitorizeActivePartAction.disable();
      getPartService().removePartListener( changeDetector );
   }

   private IPartService getPartService()
   {
      final IWorkbenchWindow workbenchWindow = getViewSite().getWorkbenchWindow();
      return workbenchWindow.getPartService();
   }

   private void hookDoubleClickAction()
   {
      viewer.addDoubleClickListener( new IDoubleClickListener()
      {
         public void doubleClick( final DoubleClickEvent event )
         {
            final IStructuredSelection selection = ( IStructuredSelection ) viewer.getSelection();
            final IMarker selectedMarker = ( IMarker ) selection.getFirstElement();
            EditorUtils.openMarkerInEditor( getViewSite().getPage(),
                                            selectedMarker );
         }
      } );
   }

   @Override
   public void init( final IViewSite site,
                     final IMemento memento ) throws PartInitException
   {
      init( site );
      this.memento = memento;
   }

   public void loadMarkers( final IMarker[] markers )
   {
      this.markers = markers;
      viewer.refresh();
   }

   private void restoreState()
   {
      if ( memento != null )
      {
         final boolean isChecked = memento.getBoolean( "autoupdateEnabled" );
         monitorizeActivePartAction.setChecked( isChecked );
         monitorizeActivePartAction.run();
      }
   }

   @Override
   public void saveState( final IMemento memento )
   {
      memento.putBoolean( "autoupdateEnabled",
                          monitorizeActivePartAction.isChecked() );
   }

   @Override
   public void setFocus()
   {
      viewer.getControl().setFocus();
   }
}