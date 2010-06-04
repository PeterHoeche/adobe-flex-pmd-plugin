package com.adobe.ac.pmd.eclipse.utils.changeDetector;

import org.eclipse.core.resources.IFile;

public interface IActivePmdEditorChangeListener extends IActiveEditorContentChangeListener
{
   public void activePartMarkersChange( IFile file );
}
