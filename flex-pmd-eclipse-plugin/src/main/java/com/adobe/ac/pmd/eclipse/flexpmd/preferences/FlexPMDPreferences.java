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

import org.eclipse.jface.preference.IPreferenceStore;

import com.adobe.ac.pmd.eclipse.FlexPMDPlugin;

public final class FlexPMDPreferences
{
   public final static FlexPMDPreferences get()
   {

      final IPreferenceStore preferenceStore = FlexPMDPlugin.getDefault().getPreferenceStore();

      return new FlexPMDPreferences( preferenceStore.getString( PreferenceConstants.COMMAND_LINE_INSTALLATION_PATH ),
                                     preferenceStore.getString( PreferenceConstants.JAVA_COMMAND_LINE_ARGUMENTS ),
                                     preferenceStore.getString( PreferenceConstants.RULESET ),
                                     preferenceStore.getString( PreferenceConstants.CPD_MINIMUM_TOKENS ) );
   }
   private final String cpdMinimumLines;
   private String       javaVmMemory;
   private String       pmdCommandLinePath;

   private String       rulesetPath;

   private FlexPMDPreferences( final String commandLinePathToBeSet,
                               final String javaVmMemoryToBeSet,
                               final String rulesetPathToBeSet,
                               final String cpdMinimumLinesToBeSet )
   {
      pmdCommandLinePath = commandLinePathToBeSet;
      javaVmMemory = javaVmMemoryToBeSet;
      rulesetPath = rulesetPathToBeSet;
      cpdMinimumLines = cpdMinimumLinesToBeSet;
   }

   public String getCpdMinimumLines()
   {
      return cpdMinimumLines;
   }

   public final String getJavaVmCommandLine()
   {
      return javaVmMemory;
   }

   public final String getPmdCommandLinePath()
   {
      return pmdCommandLinePath;
   }

   public final String getRulesetPath()
   {
      return rulesetPath;
   }

   public final void setCommandLinePath( final String commandLinePathToBeSet )
   {
      pmdCommandLinePath = commandLinePathToBeSet;
   }

   public final void setJavaVmMemory( final String javaVmMemoryToBeSet )
   {
      javaVmMemory = javaVmMemoryToBeSet;
   }

   public final void setRulesetPath( final String rulesetPathToBeSet )
   {
      rulesetPath = rulesetPathToBeSet;
   }
}
