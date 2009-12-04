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
package com.adobe.ac.pmd.eclipse.common.view;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.PmdViolationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.view.IChild;
import com.adobe.ac.pmd.eclipse.flexpmd.view.IParent;

public class BaseViewContentProvider implements IStructuredContentProvider, ITreeContentProvider
{
   protected IParent< ? >    invisibleRoot;
   private final IViewSite   viewSite;
   protected PmdViolationsVO violations;

   public BaseViewContentProvider( final IViewSite viewSiteToBeSet )
   {
      viewSite = viewSiteToBeSet;
   }

   public void dispose()
   {
      // nothing to do
   }

   public Object[] getChildren( final Object parent )
   {
      if ( parent != null
            && parent instanceof IParent< ? > )
      {
         return ( ( IParent< ? > ) parent ).getChildren();
      }
      return new Object[ 0 ];
   }

   public Object[] getElements( final Object parent )
   {
      if ( parent.equals( viewSite ) )
      {
         return getChildren( invisibleRoot );
      }
      return getChildren( parent );
   }

   public Object getParent( final Object child )
   {
      if ( child instanceof IChild< ? > )
      {
         return ( ( IChild< ? > ) child ).getParent();
      }
      return null;
   }

   public boolean hasChildren( final Object parent )
   {
      if ( parent instanceof IParent< ? > )
      {
         return ( ( IParent< ? > ) parent ).hasChildren();
      }
      return false;
   }

   public void inputChanged( final Viewer viewer,
                             final Object obj,
                             final Object obj1 )
   {
   }
}