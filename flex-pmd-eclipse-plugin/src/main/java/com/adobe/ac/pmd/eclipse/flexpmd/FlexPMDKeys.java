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
package com.adobe.ac.pmd.eclipse.flexpmd;

public final class FlexPMDKeys
{
   public static final String CLEAR_VIOLATIONS                   = "FlexPmd.clearViolations";
   public static final String CREATE_TMP_DIR_ERROR               = "FlexPmd.createTmpDirError";
   public static final String ERROR_TITLE                        = "FlexPmdHandler.errorTitle";
   public static final String EXECUTION_ERROR                    = "FlexPmd.executionError";
   public static final String GROUP_BY_CLASS                     = "Violations.GroupByClass";

   public static final String GROUP_BY_VIOLATION                 = "Violations.GroupByViolation";
   public static final String MISSING_JAVA_COMMAND_LINE          = "FlexPmd.missingJavaCommandLine";
   public static final String MISSING_PMD_COMMAND_LINE           = "FlexPmd.missingPmdCommandLine";

   public static final String NO_FLEX_PMD_INSTALLATION_SPECIFIED = "FlexPmdHandler.noFlexPmdInstallationSpecified";
   public static final String NO_SELECTED_RESOURCE               = "FlexPmdHandler.noSelectedResource";
   public static final String RULE_HEADER_KEY                    = "ViolationsViewColumn.rule";
   public static final String RULE_LINE_HEADER_KEY               = "ViolationsViewColumn.line";
   public static final String RULE_MESSAGE_HEADER_KEY            = "ViolationsViewColumn.message";
   public static final String RUNNING_FLEXPMD                    = "FlexPmd.running";
   public static final String VIEW_LABEL_PROVIDER_VIOLATION_KEY  = "ViewLabelProvider.violation";
   public static final String VIEW_LABEL_PROVIDER_VIOLATIONS_KEY = "ViewLabelProvider.violations";

   private FlexPMDKeys()
   {
   }
}
