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
package com.adobe.ac.pmd.eclipse.flexpmd.utils;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

import com.adobe.ac.pmd.eclipse.FlexPMDPlugin;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.FlexPmdFileVO;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.PmdViolationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.ViolationVO;

public class FlexPMDMarkerUtils
{
   public static final String MARKER_TYPE = "com.adobe.ac.pmd.eclipse.FlexPMDProblem";

   private static final void addMarker( final IFile file,
                                        final String message,
                                        final int lineNumber,
                                        final int priority )
   {
      try
      {
         final IMarker marker = file.createMarker( MARKER_TYPE );
         marker.setAttribute( IMarker.MESSAGE,
                              message );
         marker.setAttribute( IMarker.SEVERITY,
                              getSeverity( priority ) );
         marker.setAttribute( IMarker.LINE_NUMBER,
                              lineNumber > 0 ? lineNumber
                                            : 1 );
      }
      catch ( final CoreException e )
      {
         FlexPMDPlugin.getDefault().logInfo( e.getMessage() );
      }
   }

   private static final void addMarkers( final FlexPmdFileVO flexPmdFile )
   {
      final IFile ifile = getFile( flexPmdFile );

      cleanMarkers( ifile );
      addMarkers( flexPmdFile,
                  ifile );
   }

   private static void addMarkers( final FlexPmdFileVO flexPmdFile,
                                   final IFile ifile )
   {
      for ( final ViolationVO violation : flexPmdFile.getViolations() )
      {
         addMarker( ifile,
                    getMarkerMessage( violation ),
                    violation.getBeginline(),
                    violation.getPriority() );
      }
   }

   private static ISchedulingRule getschedulingRule( IFile resource )
   {
      final IWorkspace workspace = ResourcesPlugin.getWorkspace();
      final IResourceRuleFactory ruleFactory = workspace.getRuleFactory();
      ISchedulingRule rule = ruleFactory.markerRule( resource );

      return rule;
   }

   public static final Job addMarkers( final PmdViolationsVO violations )
   {
      Job job = null;

      if ( violations != null )
      {
         job = new Job( "add markers job" )
         {
            @Override
            protected IStatus run( IProgressMonitor monitor )
            {
               final List< FlexPmdFileVO > filesInViolation = violations.getFilesInViolation();
               final IWorkspace workspace = ResourcesPlugin.getWorkspace();

               if ( filesInViolation.size() > 0 )
               {
                  IFile file = getFile( filesInViolation.get( 0 ) );

                  final IWorkspaceRunnable action = new IWorkspaceRunnable()
                  {
                     public void run( IProgressMonitor monitor ) throws CoreException
                     {
                        final List< FlexPmdFileVO > violatedFiles = filesInViolation;
                        monitor.beginTask( "PMD Applying markers",
                                           violatedFiles.size() );

                        for ( final FlexPmdFileVO flexPmdFile : violatedFiles )
                        {
                           addMarkers( flexPmdFile );
                           monitor.worked( 1 );
                        }
                     }
                  };

                  try
                  {
                     workspace.run( action,
                                    getschedulingRule( file ),
                                    IWorkspace.AVOID_UPDATE,
                                    monitor );
                  }
                  catch ( CoreException e )
                  {
                     e.printStackTrace();
                  }
               }

               return Status.OK_STATUS;
            }
         };
      }

      return job;
   }

   private static void cleanMarkers( final FlexPmdFileVO flexPmdFile )
   {
      cleanMarkers( getFile( flexPmdFile ) );
   }

   public static final void cleanMarkers( final File file )
   {
      final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
      final IPath location = new Path( file.getPath() );
      final IFile ifile = workspaceRoot.getFileForLocation( location );

      cleanMarkers( ifile );
   }

   public static final void cleanMarkers( final IFile file )
   {
      try
      {
         file.deleteMarkers( MARKER_TYPE,
                             false,
                             IResource.DEPTH_ZERO );
      }
      catch ( final CoreException e )
      {
         FlexPMDPlugin.getDefault().logInfo( e.getMessage() );
      }
   }

   public static final void cleanMarkers( final IProject project )
   {
      try
      {
         ( ( IResource ) project.getAdapter( IResource.class ) ).deleteMarkers( MARKER_TYPE,
                                                                                true,
                                                                                IResource.DEPTH_INFINITE );
      }
      catch ( CoreException e )
      {
         FlexPMDPlugin.getDefault().logInfo( e.getMessage() );
      }
   }

   public static void cleanMarkers( final PmdViolationsVO violations )
   {
      // && violations.getFilesInViolation().size() > 0
      if ( violations != null )
      {
         final Job job = new Job( "add markers job" )
         {
            protected IStatus run( IProgressMonitor monitor )
            {
               final IWorkspaceRunnable action = new IWorkspaceRunnable()
               {
                  public void run( IProgressMonitor monitor ) throws CoreException
                  {
                     final List< FlexPmdFileVO > violatedFiles = violations.getFilesInViolation();

                     for ( final FlexPmdFileVO flexPmdFile : violatedFiles )
                     {
                        cleanMarkers( flexPmdFile );
                     }
                  }
               };

               final IWorkspace workspace = ResourcesPlugin.getWorkspace();
               IFile file = getFile( violations.getFilesInViolation().get( 0 ) );

               try
               {
                  workspace.run( action,
                                 getschedulingRule( file ),
                                 IWorkspace.AVOID_UPDATE,
                                 monitor );
               }
               catch ( CoreException e )
               {
                  FlexPMDPlugin.getDefault().logInfo( e.getMessage() );
               }

               return Status.OK_STATUS;
            }
         };

         job.schedule();
      }
   }

   private static final IFile getFile( final FlexPmdFileVO flexPmdFile )
   {
      final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
      final IPath location = new Path( flexPmdFile.getName() );
      final IFile ifile = workspaceRoot.getFileForLocation( location );
      return ifile;
   }

   private static String getMarkerMessage( final ViolationVO violation )
   {
      String rule = getRuleName( violation );
      rule = new StringBuffer( rule ).append( ". " ).append( violation.getMessage() ).toString();
      return rule;
   }

   public static final IMarker[] getMarkers( final IResource file )
   {
      try
      {
         return file.findMarkers( MARKER_TYPE,
                                  false,
                                  IResource.DEPTH_INFINITE );
      }
      catch ( final CoreException e )
      {
         FlexPMDPlugin.getDefault().logInfo( e.getMessage() );
      }

      return new IMarker[]
      {};
   }

   private static String getRuleName( final ViolationVO violation )
   {
      String rule = violation.getRule();
      rule = rule.substring( rule.lastIndexOf( '.' ) + 1 );
      return rule;
   }

   public static final int getSeverity( final int priority )
   {
      int severity;

      if ( priority == 1 )
      {
         severity = IMarker.SEVERITY_ERROR;
      }
      else if ( priority == 2
            || priority == 3 )
      {
         severity = IMarker.SEVERITY_WARNING;
      }
      else
      {
         severity = IMarker.SEVERITY_INFO;
      }

      return severity;
   }
}
