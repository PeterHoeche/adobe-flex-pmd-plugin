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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PreferencesValidatorTest
{

   @Test
   public void testValidateFlexPmdInstallation()
   {
      assertEquals( PreferencesValidator.PmdInstallationValid.VALID,
                    PreferencesValidator.validateFlexPmdInstallation( "/Users/xagnetti/Work/repositories/opensource.adobe/flexpmd/trunk/flex-pmd-command-line/target/release/flex-pmd-command-line-1.0.RC4-SNAPSHOT.jar" ) );
      assertEquals( PreferencesValidator.PmdInstallationValid.NOT_COMMAND_LINE,
                    PreferencesValidator.validateFlexPmdInstallation( "/Users/xagnetti/Work/repositories/opensource.adobe/flexpmd/trunk/flex-pmd-command-line/target/release/flex-pmd-ruleset-1.0.RC4-SNAPSHOT.jar" ) );
      assertEquals( PreferencesValidator.PmdInstallationValid.NOT_JAVA,
                    PreferencesValidator.validateFlexPmdInstallation( "/Applications/flex-pmd/" ) );
      assertEquals( PreferencesValidator.PmdInstallationValid.NOT_JAVA,
                    PreferencesValidator.validateFlexPmdInstallation( "frgergre" ) );
      assertEquals( PreferencesValidator.PmdInstallationValid.EMPTY,
                    PreferencesValidator.validateFlexPmdInstallation( "" ) );
      assertEquals( PreferencesValidator.PmdInstallationValid.EMPTY,
                    PreferencesValidator.validateFlexPmdInstallation( null ) );
   }

   @Test
   public void testValidateJavaCommandLine()
   {
      assertEquals( PreferencesValidator.JavaVmValid.VALID,
                    PreferencesValidator.validateJavaCommandLine( "java -Xmx256m" ) );
      assertEquals( PreferencesValidator.JavaVmValid.NO_JVM,
                    PreferencesValidator.validateJavaCommandLine( "-Xmx256m" ) );
      assertEquals( PreferencesValidator.JavaVmValid.NO_JVM,
                    PreferencesValidator.validateJavaCommandLine( "jav -Xmx256m" ) );
      assertEquals( PreferencesValidator.JavaVmValid.EMPTY,
                    PreferencesValidator.validateJavaCommandLine( "" ) );
      assertEquals( PreferencesValidator.JavaVmValid.EMPTY,
                    PreferencesValidator.validateJavaCommandLine( null ) );
   }
}
