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
package com.adobe.ac.pmd.eclipse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.adobe.ac.pmd.eclipse.flexpmd.actions.NotifyErrorAction;

public class FlexPMDPlugin extends AbstractUIPlugin
{
   private static FlexPMDPlugin plugin;

   public static final String   PLUGIN_ID = "com.adobe.ac.pmd.eclipse";

   public static FlexPMDPlugin getDefault()
   {
      return plugin;
   }

   public static ImageDescriptor getImageDescriptor( final String path )
   {
      return imageDescriptorFromPlugin( PLUGIN_ID,
                                        path );
   }

   @Override
   public void start( final BundleContext context ) throws Exception
   {
      super.start( context );
      plugin = this;
   }

   @Override
   public void stop( final BundleContext context ) throws Exception
   {
      plugin = null;
      super.stop( context );
   }

   public void logError( String message )
   {
      getLog().log( new Status( IStatus.ERROR, getBundle().getSymbolicName(), message ) );
   }

   public void logError( String message,
                         Throwable error )
   {
      if ( error == null )
      {
         logError( message );
      }
      else
      {
         getLog().log( new Status( IStatus.ERROR, getBundle().getSymbolicName(), 0, message
               + error.getMessage(), error ) );
      }
   }

   public void logWarning( String message,
                           Throwable error )
   {
      getLog().log( new Status( IStatus.WARNING, getBundle().getSymbolicName(), 0, message
            + error.getMessage(), error ) );
   }

   public void logInfo( String message )
   {
      getLog().log( new Status( IStatus.INFO, getBundle().getSymbolicName(), message ) );
   }

   public void showError( final String message,
                          final Throwable t )
   {
      final Runnable uiAction = new NotifyErrorAction( message );
      Display.getDefault().asyncExec( uiAction );

      logError( message,
                t );
   }
}
