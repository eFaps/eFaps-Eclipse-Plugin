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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.efaps.eclipse.EfapsPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class CopyCIWizardPage
    extends WizardNewFileCreationPage
{

    private IResource fileResource;

    protected CopyCIWizardPage(final String _pageName,
                               final IStructuredSelection _selection)
    {
        super(_pageName, _selection);
        setFileExtension("xml");
        final Iterator<?> iter = _selection.iterator();
        if (iter.hasNext()) {
            final IAdaptable adapt = (IAdaptable) iter.next();
             this.fileResource = (IResource) adapt.getAdapter(IResource.class);
        }
    }

    @Override
    protected InputStream getInitialContents() {
        InputStream ret = null;
        if (this.fileResource != null && this.fileResource.isAccessible()) {
            final URI uri = this.fileResource.getLocationURI();
            final File file = new File(uri);
            try {
                ret = new FileInputStream(file);

                final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                final DocumentBuilder docBuilder = factory.newDocumentBuilder();
                final Document doc = docBuilder.parse(file);
                //create the root element
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
}
