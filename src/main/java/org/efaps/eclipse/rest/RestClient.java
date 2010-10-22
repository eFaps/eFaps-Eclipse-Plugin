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

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.codec.binary.Base64;
import org.efaps.eclipse.EfapsPlugin;
import org.efaps.eclipse.preferences.PreferenceConstants;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.MultiPartMediaTypes;
import com.sun.jersey.multipart.file.FileDataBodyPart;

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
     * Resource to be used in the request.
     */
    private WebResource resource;

    /**
     * Url to be used in the request by this client.
     */
    private final String url;

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

        final DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
        config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);
        this.client = ApacheHttpClient.create(config);
        this.resource = this.client.resource(this.url);
        final Builder builder = this.resource.path("update").header(HttpHeaders.AUTHORIZATION, new String(Base64
                        .encodeBase64((user + ":" + pwd).getBytes())));
        final String re = builder.get(String.class);
        EfapsPlugin.getDefault().logInfo(getClass(), "init.response", re);
    }

    /**
     * Compile the target in the server.
     *
     * @param _target target to be compiled
     */
    public void compile(final String _target)
    {
        EfapsPlugin.getDefault().logInfo(getClass(), "compile", _target);
        final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("type", _target);
        final ClientResponse response = this.resource.path("compile").queryParams(queryParams)
                        .get(ClientResponse.class);
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
        final ClientResponse response = this.resource.path("update").type(MultiPartMediaTypes.MULTIPART_MIXED_TYPE)
                        .post(ClientResponse.class,
                                        multiPart);
        EfapsPlugin.getDefault().logInfo(getClass(), "post.response", response);
    }
}
