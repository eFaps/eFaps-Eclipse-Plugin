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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotLane;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.efaps.eclipse.EfapsPlugin;
import org.efaps.eclipse.preferences.PreferenceConstants;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

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

        final Response response = resourceWebTarget.queryParam("type", _target).request(MediaType.TEXT_PLAIN_TYPE)
                        .get();

        EfapsPlugin.getDefault().logInfo(getClass(), "compile.response", response.getStatusInfo().getFamily(),
                        response.getStatusInfo().getStatusCode(),
                        response.getStatusInfo().getReasonPhrase());
    }

    /**
     * @param _files files to be posted
     * @throws Exception on error
     */
    public void post(final List<File> _files,
                     final File _revFile)
        throws Exception
    {
        EfapsPlugin.getDefault().logInfo(getClass(), "post", _files);

        final Map<String, String[]> fileInfo = new HashMap<>();
        if (_revFile != null) {
            final BufferedReader br = new BufferedReader(new FileReader(_revFile));
            String line;
            while ((line = br.readLine()) != null) {
                final String[] arr = line.split(" ");
                if (arr.length > 2) {
                    fileInfo.put(arr[0], new String[] { arr[1], arr[2] });
                }
            }
            br.close();
        }

        final FormDataMultiPart multiPart = new FormDataMultiPart();
        for (final File file : _files) {
            final FileDataBodyPart part = new FileDataBodyPart("eFaps_File", file);
            multiPart.bodyPart(part);
            if (_revFile == null) {
                final String[] info = getFileInformation(file);
                multiPart.field("eFaps_Revision", info[0]);
                multiPart.field("eFaps_Date", info[1]);
            } else {
                final String[] info = fileInfo.get(file.getName());
                if (info == null) {
                    multiPart.field("eFaps_Revision", "");
                    multiPart.field("eFaps_Date", "");
                } else {
                    multiPart.field("eFaps_Revision", info[0]);
                    multiPart.field("eFaps_Date", info[1]);
                }
            }
        }
        final Response response = this.webTarget.path("update").request()
                        .post(Entity.entity(multiPart, multiPart.getMediaType()));
        EfapsPlugin.getDefault().logInfo(getClass(), "post.response", response.getStatusInfo().getFamily(),
                        response.getStatusInfo().getStatusCode(),
                        response.getStatusInfo().getReasonPhrase());
    }

    protected String[] getFileInformation(final File _file)
    {
        final String[] ret = new String[2];

        try {
            final Repository repo = new FileRepository(evalGitDir(_file));

            final ObjectId lastCommitId = repo.resolve(Constants.HEAD);

            final PlotCommitList<PlotLane> plotCommitList = new PlotCommitList<PlotLane>();
            final PlotWalk revWalk = new PlotWalk(repo);

            final RevCommit root = revWalk.parseCommit(lastCommitId);
            revWalk.markStart(root);
            revWalk.setTreeFilter(AndTreeFilter.create(
                            PathFilter.create(_file.getPath().replaceFirst(repo.getWorkTree().getPath() + "/", "")),
                            TreeFilter.ANY_DIFF));
            plotCommitList.source(revWalk);
            plotCommitList.fillTo(2);
            final PlotCommit<PlotLane> commit = plotCommitList.get(0);
            if (commit != null) {
                final PersonIdent authorIdent = commit.getAuthorIdent();
                final Date authorDate = authorIdent.getWhen();
                final TimeZone authorTimeZone = authorIdent.getTimeZone();
                final DateTime dateTime = new DateTime(authorDate.getTime(), DateTimeZone.forTimeZone(authorTimeZone));
                ret[1] = dateTime.toString();
                ret[0] = commit.getId().getName();
            } else {
                ret[1] = new DateTime().toString();
                ret[0] = "UNKNOWN";
            }
        } catch (RevisionSyntaxException | IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * @param _file
     * @return
     */
    protected File evalGitDir(final File _file)
    {
        File ret = null;
        File parent = _file.getParentFile();
        ;
        while (parent != null) {
            ret = new File(parent, ".git");
            if (ret.exists()) {
                break;
            } else {
                parent = parent.getParentFile();
            }
        }
        return ret;
    }
}
