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
package com.adobe.ac.pmd.eclipse.builder.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.adobe.ac.pmd.eclipse.FlexPMDPlugin;
import com.adobe.ac.pmd.eclipse.builder.FlexPMDNature;
import com.adobe.ac.pmd.eclipse.utils.ResourceUtils;

public abstract class BaseNatureAction implements IObjectActionDelegate
{
   private ISelection selection;

   private void changeProjectNature( final IProject project )
   {
      try
      {
         final IProjectDescription description = project.getDescription();
         final String[] natures = description.getNatureIds();

         changeProjectNature( project,
                              description,
                              natures );

         project.setDescription( description,
                                 null );
      }
      catch ( final CoreException e )
      {
         FlexPMDPlugin.getDefault().logError( "Error changing project nature",
                                              e );
      }
   }

   protected abstract void changeProjectNature( IProject project,
                                                IProjectDescription description,
                                                String[] natures );

   /**
    * @param natures
    * @return the position of the FlexPMDNature in the array of natures passed
    *         as an argument. -1 if the FlexPMDNature is not in the array.
    */
   protected int findNatureIndex( final String[] natures )
   {
      int natureIndex = -1;

      for ( int i = 0; i < natures.length; ++i )
      {
         if ( FlexPMDNature.NATURE_ID.equals( natures[ i ] ) )
         {
            natureIndex = i;
            break;
         }
      }

      return natureIndex;
   }

   public void run( final IAction action )
   {
      if ( selection instanceof IStructuredSelection )
      {
         final IStructuredSelection structuredSelection = ( IStructuredSelection ) selection;

         for ( final Iterator it = structuredSelection.iterator(); it.hasNext(); )
         {
            final IProject project = ResourceUtils.getProject( it.next() );

            if ( project != null )
            {
               changeProjectNature( project );
            }
         }
      }
   }

   public void selectionChanged( final IAction action,
                                 final ISelection selection )
   {
      this.selection = selection;
   }

   public void setActivePart( final IAction action,
                              final IWorkbenchPart targetPart )
   {
      // nothing to do
   }
}
