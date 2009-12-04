/**
 *    Copyright (c) 2009, Adobe Systems, Incorpoimport java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.adobe.ac.pmd.eclipse.flexpmd.utils.FlexPMDMarkerUtils;
import com.adobe.ac.pmd.eclipse.utils.ResourceUtils;
reproduce the above copyright
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
package com.adobe.ac.pmd.eclipse.flexpmd.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.adobe.ac.pmd.eclipse.flexpmd.utils.FlexPMDMarkerUtils;
import com.adobe.ac.pmd.eclipse.utils.ResourceUtils;

public class CleanFlexPmdMarkersAction implements IObjectActionDelegate
{
   private ISelection selection;

   public void run( final IAction arg0 )
   {
      if ( selection instanceof IStructuredSelection )
      {
         final IStructuredSelection structuredSelection = ( IStructuredSelection ) selection;

         for ( final Iterator it = structuredSelection.iterator(); it.hasNext(); )
         {
            final IProject project = ResourceUtils.getProject( it.next() );

            if ( project != null )
            {
               FlexPMDMarkerUtils.cleanMarkers( project );
            }
         }
      }
   }

   public void selectionChanged( final IAction action,
                                 final ISelection selection )
   {
      this.selection = selection;
   }

   public void setActivePart( final IAction arg0,
                              final IWorkbenchPart arg1 )
   {
   }
}
