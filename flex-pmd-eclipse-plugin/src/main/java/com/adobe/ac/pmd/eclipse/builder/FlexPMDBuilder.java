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
package com.adobe.ac.pmd.eclipse.builder;

import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import com.adobe.ac.pmd.eclipse.FlexPMDPlugin;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.FlexPMD;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.FlexPmdExecutionException;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.PmdViolationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.utils.FlexPMDMarkerUtils;
import com.adobe.ac.pmd.eclipse.utils.FileUtils;
import com.adobe.ac.pmd.eclipse.utils.IProcessable;

public class FlexPMDBuilder extends IncrementalProjectBuilder
{
   class FullBuildVisitor implements IResourceVisitor
   {
      public boolean visit( final IResource resource )
      {
         if ( resource instanceof IProject )
         {
            final IProject project = ( IProject ) resource;
            final IPath location = project.getLocation();

            processPMDAndAddMarkers( location.toFile() );
         }

         return false;
      }
   }

   class IncrementalDeltaVisitor implements IResourceDeltaVisitor
   {
      public boolean visit( final IResourceDelta delta ) throws CoreException
      {
         final IPath resource = delta.getResource().getLocation();

         switch ( delta.getKind() )
         {
         case IResourceDelta.ADDED:
            checkViolationsIfFlexFile( resource );
            break;

         case IResourceDelta.CHANGED:
            checkViolationsIfFlexFile( resource );
            break;

         case IResourceDelta.REMOVED:
         default:
            // nothing todo
            break;
         }

         return true;
      }
   }

   public static final String                    BUILDER_ID = "com.adobe.ac.pmd.eclipse.flexpmdbuilder";

   private final IProcessable< PmdViolationsVO > flexPMD;

   public FlexPMDBuilder()
   {
      flexPMD = new FlexPMD();
   }

   public FlexPMDBuilder( final IProcessable< PmdViolationsVO > flexPMD )
   {
      super();
      this.flexPMD = flexPMD;
   }

   @Override
   @SuppressWarnings("unchecked")
   protected IProject[] build( final int kind,
                               final Map args,
                               final IProgressMonitor monitor ) throws CoreException
   {
      if ( kind == FULL_BUILD )
      {
         fullBuild( monitor );
      }
      else
      {
         final IResourceDelta delta = getDelta( getProject() );

         if ( delta == null )
         {
            fullBuild( monitor );
         }
         else
         {
            incrementalBuild( delta,
                              monitor );
         }
      }

      return new IProject[]
      {};
   }

   private void checkViolationsIfFlexFile( final IPath resource )
   {
      if ( FileUtils.isFlexFile( resource.getFileExtension() ) )
      {
         processPMDAndAddMarkers( resource.toFile() );
      }
   }

   protected void fullBuild( final IProgressMonitor monitor ) throws CoreException
   {
      getProject().accept( new FullBuildVisitor() );
   }

   protected void incrementalBuild( final IResourceDelta delta,
                                    final IProgressMonitor monitor ) throws CoreException
   {
      delta.accept( new IncrementalDeltaVisitor() );
   }

   private void processPMDAndAddMarkers( final File resource )
   {
      PmdViolationsVO results;

      try
      {
         results = flexPMD.process( resource );
         FlexPMDMarkerUtils.addMarkers( results );
      }
      catch ( final FlexPmdExecutionException e )
      {
         FlexPMDPlugin.getDefault().showError( e.getMessage(),
                                               e );
      }
   }
}
