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
package com.adobe.ac.pmd.eclipse.utils.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Usage of following class can go as ...
 * <P>
 *
 * <PRE>
 * &lt;CODE&gt;
 *       SysCommandExecutor cmdExecutor = new SysCommandExecutor();
 *       cmdExecutor.setOutputLogDevice(new LogDevice());
 *       cmdExecutor.setErrorLogDevice(new LogDevice());
 *       int exitStatus = cmdExecutor.runCommand(commandLine);
 * &lt;/CODE&gt;
 * </PRE>
 *
 * </P>
 * OR
 * <P>
 *
 * <PRE>
 * &lt;CODE&gt;
 *       SysCommandExecutor cmdExecutor = new SysCommandExecutor();
 *       int exitStatus = cmdExecutor.runCommand(commandLine);
 *       String cmdError = cmdExecutor.getCommandError();
 *       String cmdOutput = cmdExecutor.getCommandOutput();
 * &lt;/CODE&gt;
 * </PRE>
 *
 * </P>
 */
public class SysCommandExecutor
{
   private static class AsyncStreamReader extends Thread
   {
      private final StringBuffer fBuffer;
      private final InputStream  fInputStream;
      private final ILogDevice   fLogDevice;
      private final String       fNewLine;
      private boolean            fStop;

      public AsyncStreamReader( final InputStream inputStream,
                                final StringBuffer buffer,
                                final ILogDevice logDevice )
      {
         super();
         fInputStream = inputStream;
         fBuffer = buffer;
         fLogDevice = logDevice;
         fNewLine = System.getProperty( "line.separator" );
         fStop = false;
      }

      private void printToDisplayDevice( final String line )
      {
         if ( fLogDevice != null )
         {
            fLogDevice.log( line );
         }
      }

      private void readCommandOutput() throws IOException
      {
         final BufferedReader bufOut = new BufferedReader( new InputStreamReader( fInputStream ) );
         String line = null;
         while ( !fStop
               && ( line = bufOut.readLine() ) != null )
         {
            fBuffer.append( line );
            fBuffer.append( fNewLine );
            printToDisplayDevice( line );
         }
         bufOut.close();
      }

      @Override
      public void run()
      {
         try
         {
            readCommandOutput();
         }
         catch ( final Exception ex )
         {
         }
      }

      public void stopReading()
      {
         fStop = true;
      }
   }

   private StringBuffer      fCmdError         = null;
   private AsyncStreamReader fCmdErrorThread   = null;
   private StringBuffer      fCmdOutput        = null;
   private AsyncStreamReader fCmdOutputThread  = null;
   private ILogDevice        fErrorLogDevice   = null;
   private ILogDevice        fOuputLogDevice   = null;
   private String            fWorkingDirectory = null;

   public String getCommandError()
   {
      return fCmdError.toString();
   }

   public String getCommandOutput()
   {
      return fCmdOutput.toString();
   }

   private void notifyOutputAndErrorReadThreadsToStopReading()
   {
      fCmdOutputThread.stopReading();
      fCmdErrorThread.stopReading();
   }

   public int runCommand( final String commandLine ) throws Exception
   {
      final Process process = runCommandHelper( commandLine );

      startOutputAndErrorReadThreads( process.getInputStream(),
                                      process.getErrorStream() );
      int exitStatus = -1;
      try
      {
         exitStatus = process.waitFor();

      }
      catch ( final Throwable ex )
      {
         throw new Exception( ex.getMessage(), ex );
      }
      finally
      {
         notifyOutputAndErrorReadThreadsToStopReading();
      }

      return exitStatus;
   }

   private Process runCommandHelper( final String commandLine ) throws IOException
   {
      Process process = null;
      if ( fWorkingDirectory == null )
      {
         process = Runtime.getRuntime().exec( commandLine,
                                              new String[]
                                              {} );
      }
      else
      {
         process = Runtime.getRuntime().exec( commandLine,
                                              new String[]
                                              {},
                                              new File( fWorkingDirectory ) );
      }

      return process;
   }

   public void setErrorLogDevice( final ILogDevice logDevice )
   {
      fErrorLogDevice = logDevice;
   }

   public void setOutputLogDevice( final ILogDevice logDevice )
   {
      fOuputLogDevice = logDevice;
   }

   public void setWorkingDirectory( final String workingDirectory )
   {
      fWorkingDirectory = workingDirectory;
   }

   private void startOutputAndErrorReadThreads( final InputStream processOut,
                                                final InputStream processErr )
   {
      fCmdOutput = new StringBuffer();
      fCmdOutputThread = new AsyncStreamReader( processOut, fCmdOutput, fOuputLogDevice );
      fCmdOutputThread.start();

      fCmdError = new StringBuffer();
      fCmdErrorThread = new AsyncStreamReader( processErr, fCmdError, fErrorLogDevice );
      fCmdErrorThread.start();
   }
}
