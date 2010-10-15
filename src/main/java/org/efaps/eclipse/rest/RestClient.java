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

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.codec.binary.Base64;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class RestClient
{
    private Client client;
    private WebResource resource;
    private final String url;

    public RestClient(final String _url) {
        this.url = _url;
    }

    public void init()
    {
        final DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
        config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);

        this.client = ApacheHttpClient.create(config);
        // http://localhost:8888/eFaps/servlet/rest/update
        // http://www.distribuidorafederal.com:8060/df/
        //http://localhost:9999/df/servlet/rest
        //http://192.168.1.31:8060/gyg/servlet/rest
        this.resource = this.client.resource(this.url);
        final Builder builder = this.resource.path("update").header(HttpHeaders.AUTHORIZATION, new String(Base64
                        .encodeBase64("Administrator:Administrator".getBytes())));
        final String re = builder.get(String.class);
        System.out.println(re);
    }


    public void compile(final String _target)
    {
        final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("type", _target);
        final ClientResponse response = this.resource.path("compile").queryParams(queryParams).get(ClientResponse.class);
        System.out.println(response);
    }

}
