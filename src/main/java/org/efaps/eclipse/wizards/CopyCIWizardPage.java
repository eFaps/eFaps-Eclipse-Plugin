/*
 * Copyright 2003 - 2011 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision:        $Rev: 5696 $
 * Last Changed:    $Date: 2010-10-15 19:56:28 -0500 (Fri, 15 Oct 2010) $
 * Last Changed By: $Author: jan.moxter $
 */

package org.efaps.eclipse.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Iterator;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.undo.CreateFileOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.efaps.eclipse.EfapsPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CopyCIWizardPage
    extends WizardNewFileCreationPage
{

    private IResource fileResource;
    private String fileName;

    protected CopyCIWizardPage(final String _pageName,
                               final IStructuredSelection _selection)
    {
        super(_pageName, _selection);
        setFileExtension("xml");
        final Iterator<?> iter = _selection.iterator();
        if (iter.hasNext()) {
            final IAdaptable adapt = (IAdaptable) iter.next();
            this.fileResource = (IResource) adapt.getAdapter(IResource.class);
            setFileName(this.fileResource.getName());
        }
    }

    @Override
    protected void createAdvancedControls(final Composite parent)
    {

    }

    @Override
    protected IStatus validateLinkedResource()
    {
        return Status.OK_STATUS;
    }

    @Override
    protected InputStream getInitialContents()
    {
        InputStream ret = null;
        if (this.fileResource != null && this.fileResource.isAccessible()) {
            final URI uri = this.fileResource.getLocationURI();
            final File file = new File(uri);
            try {
                ret = new FileInputStream(file);

                final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                final DocumentBuilder docBuilder = factory.newDocumentBuilder();
                final Document doc = docBuilder.parse(file);
                // create the root element
                final Element root = doc.getDocumentElement();
                final NodeList children = root.getChildNodes();
                boolean def = false;
                boolean uuid = false;
                for (int i = 0; i < children.getLength(); i++) {
                    if (children.item(i) instanceof Element) {
                        final Element child = (Element) children.item(i);
                        if ("uuid".equals(child.getNodeName())) {
                            child.setTextContent(UUID.randomUUID().toString());
                            uuid = true;
                        }
                        if ("definition".equals(child.getNodeName())) {
                            final NodeList defChildren = child.getChildNodes();
                            for (int j = 0; j < defChildren.getLength(); j++) {
                                if (defChildren.item(j) instanceof Element) {
                                    final Element defChild = (Element) defChildren.item(j);
                                    if ("name".equals(defChild.getNodeName())) {
                                        defChild.setTextContent(getFileName().replace("." + getFileExtension(), ""));
                                        break;
                                    }
                                }
                            }
                            def = true;
                        }
                        if (def && uuid) {
                            break;
                        }
                    }
                }

                final TransformerFactory transfac = TransformerFactory.newInstance();
                final Transformer trans = transfac.newTransformer();
                final Source source = new DOMSource(doc);
                final StreamResult result = new StreamResult(new StringWriter());
                trans.transform(source, result);
                ret = IOUtils.toInputStream(result.getWriter().toString());

            } catch (final FileNotFoundException e) {
                EfapsPlugin.getDefault().logError(getClass(), "FileNotFoundException", e);
            } catch (final ParserConfigurationException e) {
                EfapsPlugin.getDefault().logError(getClass(), "ParserConfigurationException", e);
            } catch (final SAXException e) {
                EfapsPlugin.getDefault().logError(getClass(), "SAXException", e);
            } catch (final IOException e) {
                EfapsPlugin.getDefault().logError(getClass(), "IOException", e);
            } catch (final TransformerConfigurationException e) {
                EfapsPlugin.getDefault().logError(getClass(), "TransformerConfigurationException", e);
            } catch (final TransformerException e) {
                EfapsPlugin.getDefault().logError(getClass(), "TransformerException", e);
            }
        }
        return ret;
    }

    public void createNewFiles(final String _fileNames)
    {
        final String[] filenames = _fileNames.split("\n");
        for (final String fileNameTmp : filenames) {
            if (fileNameTmp != null && !fileNameTmp.isEmpty()) {
                this.fileName = fileNameTmp.trim();
                final IPath containerPath = getContainerFullPath();
                final IPath newFilePath = containerPath.append(fileNameTmp.trim() + ".xml");
                final IFile newFileHandle = createFileHandle(newFilePath);
                final InputStream initialContents = getInitialContents();
                final IRunnableWithProgress op = new IRunnableWithProgress()
                {

                    public void run(final IProgressMonitor monitor)
                    {
                        final CreateFileOperation op = new CreateFileOperation(newFileHandle,
                                        null, initialContents,
                                        IDEWorkbenchMessages.WizardNewFileCreationPage_title);
                        try {
                            // see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=219901
                            // directly execute the operation so that the undo state is
                            // not preserved. Making this undoable resulted in too many
                            // accidental file deletions.
                            op.execute(monitor, WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
                        } catch (final ExecutionException e) {
                            getContainer().getShell().getDisplay().syncExec(
                                            new Runnable()
                                            {

                                                public void run()
                                                {
                                                    if (e.getCause() instanceof CoreException) {
                                                        ErrorDialog.openError(getContainer().getShell(),
                                                            IDEWorkbenchMessages.WizardNewFileCreationPage_errorTitle,
                                                                        null,
                                                                        ((CoreException) e.getCause()).getStatus());
                                                    } else {
                                                        IDEWorkbenchPlugin.log(getClass(),
                                                                               "createNewFile()", e.getCause()); //$NON-NLS-1$
                                                        MessageDialog.openError(getContainer().getShell(),
                                                    IDEWorkbenchMessages.WizardNewFileCreationPage_internalErrorTitle,
                                           NLS.bind(IDEWorkbenchMessages.WizardNewFileCreationPage_internalErrorMessage,
                                           e.getCause().getMessage()));
                                                    }
                                                }
                                            });
                        }
                    }
                };
                try {
                    getContainer().run(true, true, op);
                } catch (final InterruptedException e) {

                } catch (final InvocationTargetException e) {
                    IDEWorkbenchPlugin.log(getClass(), "createNewFile()", e.getTargetException()); //$NON-NLS-1$
                    MessageDialog.open(MessageDialog.ERROR,getContainer().getShell(),
                                        IDEWorkbenchMessages.WizardNewFileCreationPage_internalErrorTitle,
                                        NLS.bind(IDEWorkbenchMessages.WizardNewFileCreationPage_internalErrorMessage,
                                        e.getTargetException().getMessage()), SWT.SHEET);

                }
            }
        }
        this.fileName = null;
    }

    @Override
    public String getFileName()
    {
        final String ret;
        if (this.fileName != null) {
            ret = this.fileName;
        } else {
            ret = super.getFileName();
        }
        return ret;
    }
}
