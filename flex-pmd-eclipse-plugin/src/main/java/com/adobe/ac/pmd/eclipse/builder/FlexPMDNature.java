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

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class FlexPMDNature implements IProjectNature
{
   public static final String NATURE_ID = "com.adobe.ac.pmd.eclipse.flexpmdnature";

   private IProject           project;

   public void configure() throws CoreException
   {
      final IProjectDescription desc = project.getDescription();
      final ICommand[] commands = desc.getBuildSpec();

      for ( int i = 0; i < commands.length; ++i )
      {
         if ( commands[ i ].getBuilderName().equals( FlexPMDBuilder.BUILDER_ID ) )
         {
            return;
         }
      }

      final ICommand[] newCommands = new ICommand[ commands.length + 1 ];
      System.arraycopy( commands,
                        0,
                        newCommands,
                        0,
                        commands.length );
      final ICommand command = desc.newCommand();
      command.setBuilderName( FlexPMDBuilder.BUILDER_ID );
      newCommands[ newCommands.length - 1 ] = command;
      desc.setBuildSpec( newCommands );
      project.setDescription( desc,
                              null );
   }

   public void deconfigure() throws CoreException
   {
      final IProjectDescription description = getProject().getDescription();
      final ICommand[] commands = description.getBuildSpec();
      for ( int i = 0; i < commands.length; ++i )
      {
         if ( commands[ i ].getBuilderName().equals( FlexPMDBuilder.BUILDER_ID ) )
         {
            final ICommand[] newCommands = new ICommand[ commands.length - 1 ];
            System.arraycopy( commands,
                              0,
                              newCommands,
                              0,
                              i );
            System.arraycopy( commands,
                              i + 1,
                              newCommands,
                              i,
                              commands.length
                                    - i - 1 );
            description.setBuildSpec( newCommands );
            project.setDescription( description,
                                    null );
            return;
         }
      }
   }

   public IProject getProject()
   {
      return project;
   }

   public void setProject( final IProject project )
   {
      this.project = project;
   }
}
