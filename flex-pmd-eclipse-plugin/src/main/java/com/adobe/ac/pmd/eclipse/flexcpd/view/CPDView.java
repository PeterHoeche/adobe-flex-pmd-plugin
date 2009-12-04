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
package com.adobe.ac.pmd.eclipse.flexcpd.view;

import java.io.File;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;

import com.adobe.ac.pmd.eclipse.flexcpd.cmd.data.CPDDuplicationsVO;
import com.adobe.ac.pmd.eclipse.flexcpd.cmd.data.CPDFileVO;
import com.adobe.ac.pmd.eclipse.flexpmd.FlexPMDKeys;
import com.adobe.ac.pmd.eclipse.utils.EditorUtils;
import com.adobe.ac.pmd.eclipse.utils.MessageUtils;

public class CPDView extends ViewPart
{

   public static final String  VIEW_ID = "com.adobe.ac.pmd.eclipse.flexcpd.view.CPDView";

   private ViewContentProvider dataprovider;
   private TreeViewer          viewer;

   private CPDDuplicationsVO   duplications;

   private void createLineNumberColumn( final Tree tree )
   {
      final TreeColumn lineColumn = new TreeColumn( tree, SWT.LEFT );
      lineColumn.setText( "Class" );
      lineColumn.setWidth( 500 );
      lineColumn.setResizable( false );
   }

   private void createMessageColumn( final Tree tree )
   {
      final TreeColumn messageColumn = new TreeColumn( tree, SWT.LEFT );
      messageColumn.setText( MessageUtils.getString( FlexPMDKeys.RULE_MESSAGE_HEADER_KEY ) );
      messageColumn.setWidth( 300 );
   }

   @Override
   public void createPartControl( final Composite parent )
   {
      viewer = new TreeViewer( parent, SWT.SINGLE
            | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );

      final Tree tree = viewer.getTree();
      tree.setHeaderVisible( true );

      createMessageColumn( tree );
      createLineNumberColumn( tree );

      dataprovider = new ViewContentProvider( getViewSite() );

      if ( duplications != null )
      {
         dataprovider.loadData( duplications );
      }

      viewer.setContentProvider( dataprovider );
      viewer.setLabelProvider( new ViewLabelProvider() );
      viewer.setInput( getViewSite() );

      hookDoubleClickAction();
   }

   private void hookDoubleClickAction()
   {
      viewer.addDoubleClickListener( new IDoubleClickListener()
      {
         public void doubleClick( final DoubleClickEvent event )
         {
            openSelectedItem();
         }
      } );
   }

   public void loadDuplications( final CPDDuplicationsVO duplications )
   {
      this.duplications = duplications;
      dataprovider.loadData( duplications );
      viewer.setContentProvider( dataprovider );
      viewer.setInput( getViewSite() );
   }

   private void openSelectedItem()
   {
      final ISelection selection = viewer.getSelection();
      final Object selectedElement = ( ( IStructuredSelection ) selection ).getFirstElement();

      if ( selectedElement instanceof CPDFileVO )
      {
         final CPDFileVO duplicationFile = ( CPDFileVO ) selectedElement;
         final File file = new File( duplicationFile.getPath() );
         final int line = duplicationFile.getLine();

         EditorUtils.openFilePathInEditor( file.getAbsolutePath(),
                                           getSite().getPage(),
                                           line,
                                           0,
                                           line
                                                 + duplicationFile.getParent().getLines() );
      }
   }

   @Override
   public void setFocus()
   {
   }
}