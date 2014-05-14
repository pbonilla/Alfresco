package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import com.pernixsolutions.webscripts.WebscriptUtil.ParametersConstants;
import com.pernixsolutions.webscripts.WebscriptUtil.RequestHelper;

import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.version.Version;
import org.alfresco.service.cmr.version.VersionService;


public class CheckVersion extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;

    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CheckInPost.class);

        LOG.debug("StartExecImpl()");
        LOG.debug(RequestHelper.getRequestString(req));

        try{
            final String nodeRefPath = req.getParameter(ParametersConstants.NODE_REF);
            res.getWriter().write(nodeRefPath);

            VersionService versionService = registry.getVersionService();

            NodeRef nodeRef = NodeRef.getNodeRefs(nodeRefPath).get(0);

            getVersion(versionService, nodeRef, LOG, res);
        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("\nRaise an error in Check Version");
        }
        res.setContentType(MimetypeMap.MIMETYPE_TEXT_PLAIN);
        res.setContentEncoding(StandardCharsets.UTF_8.name());
    }

    private void getVersion(final VersionService versionService, final NodeRef nodeRef,
            final Logger LOG, final WebScriptResponse res) throws IOException{
        Version currentlyVersion = versionService.getCurrentVersion(nodeRef);
        String versionLabel = currentlyVersion.getVersionLabel();
        res.getWriter().write("\nVersion Number: " + versionLabel);
        LOG.debug("Version Number: "+versionLabel);
    }

    /**
     * @param value the registry to set
     */
    public void setRegistry(final ServiceRegistry value) {
        this.registry = value;
    }

}
