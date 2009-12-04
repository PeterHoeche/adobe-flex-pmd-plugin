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

import java.text.MessageFormat;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.adobe.ac.pmd.eclipse.flexpmd.FlexPMDKeys;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.ViolationVO;
import com.adobe.ac.pmd.eclipse.utils.MessageUtils;

public class BaseViewLabelProvider extends LabelProvider
{

   public BaseViewLabelProvider()
   {
      super();
   }

   protected Image getFileIcon()
   {
      return PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJ_FILE );
   }

   protected String getNumberOfViolationsMessage( final int numChildren )
   {
      String columnText;
      MessageFormat format;

      if ( numChildren == 1 )
      {
         format = new MessageFormat( MessageUtils.getString( FlexPMDKeys.VIEW_LABEL_PROVIDER_VIOLATION_KEY ) );
      }
      else
      {
         format = new MessageFormat( MessageUtils.getString( FlexPMDKeys.VIEW_LABEL_PROVIDER_VIOLATIONS_KEY ) );
      }

      columnText = format.format( new String[]
      { String.valueOf( numChildren ) } );
      return columnText;
   }

   protected Image getPriorityIcon( final int priority )
   {
      switch ( priority )
      {
      case 1:
         return PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJS_ERROR_TSK );
      case 3:
         return PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJS_WARN_TSK );
      default:
         return PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJS_INFO_TSK );
      }
   }

   protected String getRuleName( final ViolationVO violation )
   {
      final String rule = violation.getRule();
      return rule.substring( rule.lastIndexOf( '.' ) + 1 );
   }
}