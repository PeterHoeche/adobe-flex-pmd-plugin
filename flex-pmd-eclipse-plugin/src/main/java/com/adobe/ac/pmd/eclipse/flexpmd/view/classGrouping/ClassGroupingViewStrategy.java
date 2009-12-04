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
package com.adobe.ac.pmd.eclipse.flexpmd.view.classGrouping;

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;

import com.adobe.ac.pmd.eclipse.flexpmd.FlexPMDKeys;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.PmdViolationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.ViolationVO;
import com.adobe.ac.pmd.eclipse.flexpmd.view.BaseViewStrategy;
import com.adobe.ac.pmd.eclipse.flexpmd.view.IViolationsViewStrategy;
import com.adobe.ac.pmd.eclipse.flexpmd.view.classGrouping.model.FileNode;
import com.adobe.ac.pmd.eclipse.flexpmd.view.classGrouping.model.ViolationLeaf;
import com.adobe.ac.pmd.eclipse.utils.EditorUtils;
import com.adobe.ac.pmd.eclipse.utils.MessageUtils;

public class ClassGroupingViewStrategy extends BaseViewStrategy implements IViolationsViewStrategy
{
   private final IWorkbenchPage page;

   public ClassGroupingViewStrategy( final IWorkbenchPage page )
   {
      this.page = page;
   }

   private void createLineNumberColumn( final Tree tree )
   {
      final TreeColumn lineColumn = new TreeColumn( tree, SWT.LEFT );
      lineColumn.setText( MessageUtils.getString( FlexPMDKeys.RULE_LINE_HEADER_KEY ) );
      lineColumn.setWidth( 50 );
      lineColumn.setResizable( false );
   }

   private void createMessageColumn( final Tree tree )
   {
      final TreeColumn messageColumn = new TreeColumn( tree, SWT.LEFT );
      messageColumn.setText( MessageUtils.getString( FlexPMDKeys.RULE_MESSAGE_HEADER_KEY ) );
      messageColumn.setWidth( 500 );
   }

   private void createRuleColumn( final Tree tree )
   {
      final TreeColumn ruleColumn = new TreeColumn( tree, SWT.LEFT );
      ruleColumn.setText( MessageUtils.getString( FlexPMDKeys.RULE_HEADER_KEY ) );
      ruleColumn.setWidth( 400 );
   }

   public TreeViewer createView( final Composite parent,
                                 final IViewSite viewSite,
                                 final PmdViolationsVO violations )
   {
      viewer = new TreeViewer( parent, SWT.SINGLE
            | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );

      final Tree tree = viewer.getTree();
      tree.setHeaderVisible( true );

      createRuleColumn( tree );
      createMessageColumn( tree );
      createLineNumberColumn( tree );

      final ViewContentProvider dataprovider = new ViewContentProvider( viewSite );
      if ( violations != null )
      {
         dataprovider.loadData( violations );
      }

      viewer.setLabelProvider( new ViewLabelProvider() );
      viewer.setContentProvider( dataprovider );
      viewer.setInput( viewSite );

      hookDoubleClickAction();

      return viewer;
   }

   public void openSelectedItem()
   {
      final ISelection selection = viewer.getSelection();
      final Object selectedElement = ( ( IStructuredSelection ) selection ).getFirstElement();

      if ( selectedElement instanceof ViolationLeaf )
      {
         final ViolationLeaf violationLeaf = ( ViolationLeaf ) selectedElement;
         final File file = new File( violationLeaf.getParent().getFile().getName() );

         final ViolationVO violation = violationLeaf.getViolation();
         EditorUtils.openFilePathInEditor( file.getAbsolutePath(),
                                           page,
                                           violation.getBeginline(),
                                           violation.getBegincolumn() );
      }
      else
      {
         final FileNode fileNode = ( FileNode ) selectedElement;
         final File file = new File( fileNode.getFile().getName() );
         EditorUtils.openFilePathInEditor( file.getAbsolutePath(),
                                           page );
      }
   }
}