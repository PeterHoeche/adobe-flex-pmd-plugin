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
package com.adobe.ac.pmd.eclipse.utils;

import java.util.logging.Logger;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public final class EditorUtils
{
   private static final Logger LOGGER = Logger.getLogger( EditorUtils.class.getName() );

   private static ITextEditor getTextEditor( final IWorkbenchPage page )
   {
      return ( ITextEditor ) page.getActiveEditor().getAdapter( ITextEditor.class );
   }

   public static void gotoLine( final IWorkbenchPage page,
                                final int line,
                                final int column,
                                final int lastLineToSelect )
   {

      final ITextEditor editor = getTextEditor( page );
      final IDocumentProvider documentProvider = editor.getDocumentProvider();
      final IDocument document = documentProvider.getDocument( editor.getEditorInput() );

      final int lineToGo = line - 1 >= 0 ? line - 1
                                        : 0;
      try
      {
         int initialLineOffSet = document.getLineOffset( lineToGo );
         int numCharactersToSelect = 0;

         if ( lastLineToSelect > 0 )
         {
            numCharactersToSelect = document.getLineOffset( lastLineToSelect )
                  - initialLineOffSet;
         }

         editor.selectAndReveal( initialLineOffSet
                                       + column,
                                 numCharactersToSelect );
      }
      catch ( final BadLocationException e )
      {
         LOGGER.warning( e.getMessage() );
      }
   }

   public static IEditorPart openFilePathInEditor( final String absolutePath,
                                                   final IWorkbenchPage page )
   {

      final IFileStore fileStore = EFS.getLocalFileSystem().getStore( new Path( absolutePath ) );
      final IFileInfo fetchInfo = fileStore.fetchInfo();

      IEditorPart part = null;

      if ( !fetchInfo.isDirectory()
            && fetchInfo.exists() )
      {
         try
         {
            part = IDE.openEditorOnFileStore( page,
                                              fileStore );
         }
         catch ( final PartInitException e )
         {
            LOGGER.warning( e.getMessage() );
         }
      }

      return part;
   }

   public static IEditorPart openFilePathInEditor( final String absolutePath,
                                                   final IWorkbenchPage page,
                                                   final int line,
                                                   final int column )
   {

      return openFilePathInEditor( absolutePath,
                                   page,
                                   line,
                                   column,
                                   0 );
   }

   public static IEditorPart openFilePathInEditor( final String absolutePath,
                                                   final IWorkbenchPage page,
                                                   final int line,
                                                   final int column,
                                                   final int lastLineToSelect )
   {

      final IEditorPart part = openFilePathInEditor( absolutePath,
                                                     page );

      if ( part != null )
      {
         gotoLine( page,
                   line,
                   column,
                   lastLineToSelect );
      }

      return part;
   }

   public static void openMarkerInEditor( final IWorkbenchPage page,
                                          final IMarker marker )
   {
      final ITextEditor editor = getTextEditor( page );

      final IGotoMarker gotoMarker = ( IGotoMarker ) editor.getAdapter( IGotoMarker.class );
      if ( gotoMarker != null )
      {
         gotoMarker.gotoMarker( marker );
      }
   }

   private EditorUtils()
   {
   }
}
