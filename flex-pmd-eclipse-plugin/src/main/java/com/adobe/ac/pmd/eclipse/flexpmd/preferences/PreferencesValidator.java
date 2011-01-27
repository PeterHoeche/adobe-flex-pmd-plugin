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
import java.io.FilenameFilter;

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
      FILE_NOT_EXIST("PreferencesValidator.fileNotExist"),
      NOT_A_FOLDER("PreferencesValidator.notAFolder"),
      NOT_FLEXPMD_INSTALLACTION_FOLDER("PreferencesValidator.notInstallationFolder"),
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

   public static final String  FLEX_PMD_TOOL = "flex-pmd-command-line-";
   public static final String  FLEX_CPD_TOOL = "flex-pmd-cpd-command-line-";
   private static final String EMPTY_STRING  = "";

   public static PmdInstallationValid validateFlexPmdRuleset( final String rulesetPath )
   {
      PmdInstallationValid validity = PmdInstallationValid.VALID;

      if ( !( EMPTY_STRING.equals( rulesetPath ) || rulesetPath == null ) )
      {
         final File file = new File( rulesetPath );

         if ( !file.exists() )
         {
            validity = PmdInstallationValid.FILE_NOT_EXIST;
         }
      }

      return validity;
   }

   public static PmdInstallationValid validateRuntimeFolder( final String installationPath )
   {
      PmdInstallationValid validity = PmdInstallationValid.VALID;
      if ( EMPTY_STRING.equals( installationPath )
            || installationPath == null )
      {
         validity = PmdInstallationValid.EMPTY;
      }
      else
      {
         final File file = new File( installationPath );

         if ( !file.exists() )
         {
            validity = PmdInstallationValid.FILE_NOT_EXIST;
         }
         else if ( !file.isDirectory() )
         {
            validity = PmdInstallationValid.FILE_NOT_EXIST;
         }
      }

      return validity;
   }

   public static PmdInstallationValid validateFlexPmdInstallation( final String installationPath )
   {
      PmdInstallationValid validity = validateRuntimeFolder( installationPath );

      if ( PmdInstallationValid.VALID.equals( validity ) )
      {
         if ( !isFlexPmdFolder( installationPath ) )
         {
            validity = PmdInstallationValid.NOT_FLEXPMD_INSTALLACTION_FOLDER;
         }
      }
      return validity;
   }

   public static PmdInstallationValid validateFlexCpdInstallation( final String installationPath )
   {
      PmdInstallationValid validity = validateRuntimeFolder( installationPath );

      if ( PmdInstallationValid.VALID.equals( validity ) )
      {
         if ( !isFlexCpdFolder( installationPath ) )
         {
            validity = PmdInstallationValid.NOT_FLEXPMD_INSTALLACTION_FOLDER;
         }
      }

      return validity;
   }

   public static boolean isFlexPmdFolder( String folderPath )
   {
      File folder = new File( folderPath );

      if ( hasTool( FLEX_PMD_TOOL,
                    folder ) )
      {
         return true;
      }

      return false;
   }

   public static boolean isFlexCpdFolder( String folderPath )
   {
      File folder = new File( folderPath );

      if ( hasTool( FLEX_CPD_TOOL,
                    folder ) )
      {
         return true;
      }

      return false;
   }

   private static boolean hasTool( final String toolName,
                                   final File file )
   {
      String[] matchingFiles = file.list( new FilenameFilter()
      {
         public boolean accept( File dir,
                                String name )
         {

            return name.contains( toolName );
         }
      } );

      return matchingFiles.length > 0;
   }

   public static String getTool( final String toolName,
                                 final String installationPath )
   {
      final File file = new File( installationPath );

      String[] matchingFiles = file.list( new FilenameFilter()
      {
         public boolean accept( File dir,
                                String name )
         {

            return name.contains( toolName );
         }
      } );

      if ( matchingFiles.length > 0 )
      {
         return matchingFiles[ 0 ];
      }

      return null;
   }

   private PreferencesValidator()
   {
   }
}
