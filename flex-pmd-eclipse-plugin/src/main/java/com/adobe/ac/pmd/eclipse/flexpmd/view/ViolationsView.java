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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

import com.adobe.ac.pmd.eclipse.flexpmd.FlexPMDKeys;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.PmdViolationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.view.actions.ClearViolationsAction;
import com.adobe.ac.pmd.eclipse.flexpmd.view.classGrouping.ClassGroupingViewStrategy;
import com.adobe.ac.pmd.eclipse.flexpmd.view.ruleGrouping.RuleGroupingViewStrategy;
import com.adobe.ac.pmd.eclipse.utils.MessageUtils;

public class ViolationsView extends ViewPart
{
   private class ShowGroupedViolationsAction extends Action
   {
      IViolationsViewStrategy strategy;

      public ShowGroupedViolationsAction( final String text,
                                          final IViolationsViewStrategy strategy )
      {
         setText( text );
         this.strategy = strategy;
      }

      @Override
      public void run()
      {
         currentStrategy.dispose();
         viewer = strategy.createView( parent,
                                       getViewSite(),
                                       violations );
         viewer.setInput( getViewSite() );
         parent.layout( true );
         currentStrategy = strategy;
      }
   }

   public static final String      VIEW_ID = "com.adobe.ac.pmd.eclipse.flexpmd.view.ViolationsView";

   private IViolationsViewStrategy currentStrategy;
   private Composite               parent;
   private TreeViewer              viewer;
   private PmdViolationsVO         violations;

   private void configureToolBar( final Composite parent )
   {
      final IActionBars actionBars = getViewSite().getActionBars();
      final IMenuManager viewMenu = actionBars.getMenuManager();

      final IWorkbenchPage page = getSite().getPage();
      viewMenu.add( createViolationsGroupingAction( page ) );
      viewMenu.add( createClassGroupingAction( page ) );

      final IToolBarManager toolBar = actionBars.getToolBarManager();
      toolBar.add( new ClearViolationsAction( this ) );
   }

   private ShowGroupedViolationsAction createClassGroupingAction( final IWorkbenchPage page )
   {
      return new ShowGroupedViolationsAction( MessageUtils.getString( FlexPMDKeys.GROUP_BY_CLASS ),
                                              new ClassGroupingViewStrategy( page ) );
   }

   @Override
   public void createPartControl( final Composite parent )
   {
      this.parent = parent;

      currentStrategy = new RuleGroupingViewStrategy( getSite().getPage() );
      configureToolBar( parent );

      viewer = currentStrategy.createView( parent,
                                           getViewSite(),
                                           null );
   }

   private ShowGroupedViolationsAction createViolationsGroupingAction( final IWorkbenchPage page )
   {
      return new ShowGroupedViolationsAction( MessageUtils.getString( FlexPMDKeys.GROUP_BY_VIOLATION ),
                                              new RuleGroupingViewStrategy( page ) );
   }

   public PmdViolationsVO getViolations()
   {
      return violations;
   }

   public void loadViolations( final PmdViolationsVO violations )
   {
      this.violations = violations;

      if ( viewer != null )
      {
         final ILoadableContentProvider dataprovider = ( ILoadableContentProvider ) viewer.getContentProvider();
         dataprovider.loadData( violations );
         viewer.setContentProvider( dataprovider );
         viewer.setInput( getViewSite() );
      }
   }

   @Override
   public void setFocus()
   {
      viewer.getControl().setFocus();
   }
}