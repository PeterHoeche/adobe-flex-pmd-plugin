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
import org.eclipse.swt.graphics.Image;

import com.adobe.ac.pmd.eclipse.common.view.Columns;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.ViolationVO;
import com.adobe.ac.pmd.eclipse.flexpmd.view.BaseViewLabelProvider;
import com.adobe.ac.pmd.eclipse.flexpmd.view.ruleGrouping.model.FileLeaf;
import com.adobe.ac.pmd.eclipse.flexpmd.view.ruleGrouping.model.ViolationNode;

class ViewLabelProvider extends BaseViewLabelProvider implements ITableLabelProvider
{
   public Image getColumnImage( final Object selectedRow,
                                final int columnIndex )
   {
      Image icon = null;

      if ( columnIndex == Columns.FIRST )
      {
         if ( isFileRow( selectedRow ) )
         {
            icon = getFileIcon();
         }
         else
         {
            icon = getPriorityIcon( ( ( ViolationNode ) selectedRow ).getViolation().getPriority() );
         }
      }

      return icon;
   }

   private String getColumnText( final int columnIndex,
                                 final FileLeaf fileLeaf )
   {
      final ViolationVO violation = fileLeaf.getViolation();

      switch ( columnIndex )
      {
      case Columns.FIRST:
         return fileLeaf.getFile().toString();
      case Columns.SECOND:
         return violation.getMessage();
      case Columns.THIRD:
         return String.valueOf( violation.getBeginline() );
      default:
         return "";
      }
   }

   private String getColumnText( final int columnIndex,
                                 final ViolationNode fileNode )
   {
      String columnText = "";

      if ( columnIndex == Columns.FIRST )
      {
         columnText = getRuleName( fileNode.getViolation() );
      }
      else if ( columnIndex == Columns.SECOND )
      {
         columnText = getNumberOfViolationsMessage( fileNode.getChildren().length );
      }

      return columnText;
   }

   public String getColumnText( final Object selectedRow,
                                final int columnIndex )
   {
      String columnText = "";

      if ( isViolationRow( selectedRow ) )
      {
         columnText = getColumnText( columnIndex,
                                     ( ViolationNode ) selectedRow );
      }
      else
      {
         columnText = getColumnText( columnIndex,
                                     ( FileLeaf ) selectedRow );
      }

      return columnText;
   }

   private boolean isFileRow( final Object obj )
   {
      return obj instanceof FileLeaf;
   }

   private boolean isViolationRow( final Object selectedRow )
   {
      return selectedRow instanceof ViolationNode;
   }
}
