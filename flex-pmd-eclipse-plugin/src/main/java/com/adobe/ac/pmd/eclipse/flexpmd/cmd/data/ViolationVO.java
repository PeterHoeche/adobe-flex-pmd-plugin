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
package com.adobe.ac.pmd.eclipse.flexpmd.cmd.data;

public class ViolationVO
{
   private int    begincolumn;
   private int    beginline;
   private String className;
   private int    endcolumn;
   private int    endline;
   private String message;
   private String packageName;
   private int    priority;
   private String rule;

   public final int getBegincolumn()
   {
      return begincolumn;
   }

   public final int getBeginline()
   {
      return beginline;
   }

   final String getClassName()
   {
      return className;
   }

   public final int getEndcolumn()
   {
      return endcolumn;
   }

   public final int getEndline()
   {
      return endline;
   }

   public final String getMessage()
   {
      return message;
   }

   final String getPackageName()
   {
      return packageName;
   }

   public final int getPriority()
   {
      return priority;
   }

   public final String getRule()
   {
      return rule;
   }

   public final void setBegincolumn( final int begincolumnToBeSet )
   {
      begincolumn = begincolumnToBeSet;
   }

   public final void setBeginline( final int beginlineToBeSet )
   {
      beginline = beginlineToBeSet;
   }

   public final void setClassName( final String classNameToBeSet )
   {
      className = classNameToBeSet;
   }

   public final void setEndcolumn( final int endcolumnToBeSet )
   {
      endcolumn = endcolumnToBeSet;
   }

   public final void setEndline( final int endlineToBeSet )
   {
      endline = endlineToBeSet;
   }

   public final void setMessage( final String messageToBeSet )
   {
      message = messageToBeSet;
   }

   public final void setPackageName( final String packageNameToBeSet )
   {
      packageName = packageNameToBeSet;
   }

   public final void setPriority( final int priorityToBeSet )
   {
      priority = priorityToBeSet;
   }

   public final void setRule( final String ruleToBeSet )
   {
      rule = ruleToBeSet;
   }
}
