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
package com.adobe.ac.pmd.eclipse.flexpmd.view.ruleGrouping.model;

import java.util.ArrayList;
import java.util.List;

import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.ViolationVO;
import com.adobe.ac.pmd.eclipse.flexpmd.view.IChild;
import com.adobe.ac.pmd.eclipse.flexpmd.view.IParent;
import com.adobe.ac.pmd.eclipse.flexpmd.view.classGrouping.model.ViolationLeaf;

public class ViolationNode implements IParent< FileLeaf >, IChild< ViolationsNode >
{
   private final List< FileLeaf > children;
   private final ViolationsNode   parent;
   private final ViolationVO      violation;

   public ViolationNode( final ViolationsNode parentToBeSet,
                         final ViolationVO violation )
   {
      children = new ArrayList< FileLeaf >();
      parent = parentToBeSet;
      this.violation = violation;
   }

   public void addChild( final FileLeaf child )
   {
      children.add( child );
   }

   public FileLeaf[] getChildren()
   {
      return children.toArray( new FileLeaf[ children.size() ] );
   }

   public ViolationsNode getParent()
   {
      return parent;
   }

   public ViolationVO getViolation()
   {
      return violation;
   }

   public boolean hasChildren()
   {
      return !children.isEmpty();
   }

   public void removeChild( final ViolationLeaf child )
   {
      children.remove( child );
   }
}