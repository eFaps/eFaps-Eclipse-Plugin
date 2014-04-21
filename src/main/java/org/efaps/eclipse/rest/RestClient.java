/*
 * Copyright 2003 - 2010 The eFaps Team
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
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */

package org.efaps.eclipse.rest;

import java.io.File;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.efaps.eclipse.EfapsPlugin;
import org.efaps.eclipse.preferences.PreferenceConstants;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;



/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class RestClient
{

    /**
     * Client that makes the actual connection.
     */
    private Client client;

    /**
     * Url to be used in the request by this client.
     */
    private final String url;
    private WebTarget webTarget;

    /**
     * @param _url url for this client.
     */
    public RestClient(final String _url)
    {
        this.url = _url;
    }

    /**
     * Initialize the client.
     */
    public void init()
    {
        EfapsPlugin.getDefault().logInfo(getClass(), "init");
        final String pwd = EfapsPlugin.getDefault().getPreferenceStore()
                        .getString(PreferenceConstants.REST_PWD.getPrefName());
        final String user = EfapsPlugin.getDefault().getPreferenceStore()
                        .getString(PreferenceConstants.REST_USER.getPrefName());
        final ClientConfig clientConfig = new ClientConfig();

        final HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(user, pwd);
        clientConfig.register(feature).register(MultiPartFeature.class);

        this.client = ClientBuilder.newClient(clientConfig);
        this.webTarget = this.client.target(this.url);
    }

    /**
     * Compile the target in the server.
     *
     * @param _target target to be compiled
     */
    public void compile(final String _target)
    {
        EfapsPlugin.getDefault().logInfo(getClass(), "compile", _target);

        final WebTarget resourceWebTarget = this.webTarget.path("compile");

        final String response = resourceWebTarget.queryParam("type", "type").request(MediaType.TEXT_PLAIN_TYPE)
                        .get(String.class);

        EfapsPlugin.getDefault().logInfo(getClass(), "compile.response", response);
    }

    /**
     * @param _files files to be posted
     * @throws Exception on error
     */
    public void post(final List<File> _files)
        throws Exception
    {
        EfapsPlugin.getDefault().logInfo(getClass(), "post", _files);

        final MultiPart multiPart = new MultiPart();
        for (final File file : _files) {
            final FileDataBodyPart part = new FileDataBodyPart("eFaps", file);
            multiPart.bodyPart(part);
        }
         final Response response = this.webTarget.path("update").request()
                        .post(Entity.entity(multiPart, multiPart.getMediaType()));
        EfapsPlugin.getDefault().logInfo(getClass(), "post.response", response);
    }
}
