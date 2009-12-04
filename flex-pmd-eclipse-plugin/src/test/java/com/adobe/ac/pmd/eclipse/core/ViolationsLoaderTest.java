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
package com.adobe.ac.pmd.eclipse.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.junit.Test;

import com.adobe.ac.pmd.eclipse.flexpmd.cmd.FlexPMDResultsParser;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.FlexPmdFileVO;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.PmdViolationsVO;
import com.adobe.ac.pmd.eclipse.flexpmd.cmd.data.ViolationVO;

public class ViolationsLoaderTest
{

   @Test
   public void testLoadViolations() throws FileNotFoundException,
                                   URISyntaxException
   {
      final PmdViolationsVO violations = FlexPMDResultsParser.parse( new File( getClass().getResource( "/pmd.xml" )
                                                                                              .toURI()
                                                                                              .getPath() ) );

      assertEquals( "Mon Sep 14 12:09:30 CEST 2009",
                    violations.getTimestamp() );
      assertEquals( "4.2.1",
                    violations.getVersion() );
      assertEquals( 1,
                    violations.getFilesInViolation().size() );

      final FlexPmdFileVO file = violations.getFilesInViolation().get( 0 );

      assertEquals( "test/AbstractRowData.as",
                    file.getName() );
      assertEquals( 2,
                    file.getViolations().size() );

      final ViolationVO firstViolation = file.getViolations().get( 0 );

      assertEquals( "adobe.ac.pmd.rules.binding.ChangeWatcher",
                    firstViolation.getRule() );
      assertEquals( 35,
                    firstViolation.getBeginline() );
      assertEquals( 36,
                    firstViolation.getEndline() );
      assertEquals( 11,
                    firstViolation.getBegincolumn() );
      assertEquals( 12,
                    firstViolation.getEndcolumn() );
      assertEquals( 1,
                    firstViolation.getPriority() );
      assertEquals( "ChangeWatcher",
                    firstViolation.getMessage() );
   }
}
