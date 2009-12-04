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
package com.adobe.ac.pmd.eclipse.flexpmd.preferences;

import java.io.File;

public final class PreferencesValidator
{
   public static enum JavaVmValid
   {
      EMPTY("PreferencesValidator.emptyString"),
      NO_JVM("PreferencesValidator.noJvmDetected"),
      VALID(EMPTY_STRING);

      private String key;

      private JavaVmValid( final String keyToBeSet )
      {
         key = keyToBeSet;
      }

      public String getKey()
      {
         return key;
      }
   }

   public static enum PmdInstallationValid
   {
      EMPTY("PreferencesValidator.emptyString"),
      FILE_CONTAINS_SPACE("PreferencesValidator.fileContainingSpace"),
      FILE_NOT_EXIST("PreferencesValidator.fileNotExist"),
      NOT_COMMAND_LINE("PreferencesValidator.notCommandLineExecutable"),
      NOT_JAVA("PreferencesValidator.notJavaExecutable"),
      VALID("");

      private String key;

      private PmdInstallationValid( final String keyToBeSet )
      {
         key = keyToBeSet;
      }

      public String getKey()
      {
         return key;
      }
   }

   private static final String COMMAND_LINE = "command-line";
   private static final String EMPTY_STRING = "";
   private static final String JAR          = ".jar";
   private static final String JAVA         = "java";

   public static PmdInstallationValid validateFlexPmdInstallation( final String installationPath )
   {
      PmdInstallationValid validity = PmdInstallationValid.VALID;
      if ( EMPTY_STRING.equals( installationPath )
            || installationPath == null )
      {
         validity = PmdInstallationValid.EMPTY;
      }
      else if ( !installationPath.contains( JAR ) )
      {
         validity = PmdInstallationValid.NOT_JAVA;
      }
      else if ( installationPath.contains( " " ) )
      {
         validity = PmdInstallationValid.FILE_CONTAINS_SPACE;
      }
      else
      {
         final File file = new File( installationPath );
         if ( !file.getName().contains( COMMAND_LINE ) )
         {
            validity = PmdInstallationValid.NOT_COMMAND_LINE;
         }
         else if ( !file.exists() )
         {
            validity = PmdInstallationValid.FILE_NOT_EXIST;
         }
      }
      return validity;
   }

   public static JavaVmValid validateJavaCommandLine( final String commandLine )
   {
      if ( EMPTY_STRING.equals( commandLine )
            || commandLine == null )
      {
         return JavaVmValid.EMPTY;
      }
      if ( !commandLine.contains( JAVA ) )
      {
         return JavaVmValid.NO_JVM;
      }
      return JavaVmValid.VALID;
   }

   private PreferencesValidator()
   {
   }
}
