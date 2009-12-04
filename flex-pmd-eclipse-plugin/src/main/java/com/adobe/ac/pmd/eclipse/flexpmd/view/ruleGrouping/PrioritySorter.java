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
package com.adobe.ac.pmd.eclipse.flexpmd.view.ruleGrouping;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.adobe.ac.pmd.eclipse.flexpmd.view.ruleGrouping.model.ViolationNode;

public class PrioritySorter extends ViewerSorter
{
   @Override
   public int compare( final Viewer viewer,
                       final Object e1,
                       final Object e2 )
   {
      if ( e1 instanceof ViolationNode )
      {
         final ViolationNode violation1 = ( ViolationNode ) e1;
         final ViolationNode violation2 = ( ViolationNode ) e2;

         final int priority1 = violation1.getViolation().getPriority();
         final int priority2 = violation2.getViolation().getPriority();

         int order;

         if ( priority1 > priority2 )
         {
            order = 1;
         }
         else if ( priority1 < priority2 )
         {
            order = -1;
         }
         else
         {
            final TreeViewer tree = ( TreeViewer ) viewer;
            final ITableLabelProvider labelProvider = ( ITableLabelProvider ) tree.getLabelProvider();

            final String rule1Name = labelProvider.getColumnText( violation1,
                                                                  0 );
            final String rule2Name = labelProvider.getColumnText( violation2,
                                                                  0 );

            return getComparator().compare( rule1Name,
                                            rule2Name );
         }

         return order;
      }

      return super.compare( viewer,
                            e1,
                            e2 );
   }
}
