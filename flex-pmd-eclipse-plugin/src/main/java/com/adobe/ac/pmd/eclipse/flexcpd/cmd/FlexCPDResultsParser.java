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
package com.adobe.ac.pmd.eclipse.flexcpd.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.adobe.ac.pmd.eclipse.flexcpd.cmd.data.CPDDuplicationsVO;
import com.adobe.ac.pmd.eclipse.flexcpd.cmd.data.CPDFileVO;
import com.adobe.ac.pmd.eclipse.flexcpd.cmd.data.DuplicationVO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;

public final class FlexCPDResultsParser
{
   public static CPDDuplicationsVO parse( final File reportFile ) throws FileNotFoundException
   {
      final XStream xstream = new XStream( new Dom4JDriver() );

      xstream.alias( "pmd-cpd",
                     CPDDuplicationsVO.class );

      xstream.alias( "duplication",
                     DuplicationVO.class );
      xstream.aliasAttribute( DuplicationVO.class,
                              "lines",
                              "lines" );
      xstream.aliasAttribute( DuplicationVO.class,
                              "tokens",
                              "tokens" );

      xstream.alias( "file",
                     CPDFileVO.class );
      xstream.aliasAttribute( CPDFileVO.class,
                              "path",
                              "path" );
      xstream.aliasAttribute( CPDFileVO.class,
                              "line",
                              "line" );

      xstream.addImplicitCollection( CPDDuplicationsVO.class,
                                     "duplications" );

      xstream.addImplicitCollection( DuplicationVO.class,
                                     "files" );

      return ( CPDDuplicationsVO ) xstream.fromXML( new FileInputStream( reportFile ) );
   }

   private FlexCPDResultsParser()
   {
   }
}
