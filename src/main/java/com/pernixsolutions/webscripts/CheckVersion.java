package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.version.Version;
import org.alfresco.service.cmr.version.VersionService;


public class CheckVersion extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;
    /** Alfresco repository helper */
    private Repository repository;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CheckInPost.class);

        LOG.debug("StartExecImpl()");


        try{
            final String nodeRefPath = req.getParameter("nodeRef");
            res.getWriter().write(nodeRefPath);

            VersionService versionService = registry.getVersionService();
            FileFolderService fileFolderService = registry.getFileFolderService();

            NodeRef nodeRef = NodeRef.getNodeRefs(nodeRefPath).get(0);

            Version currentlyVersion = versionService.getCurrentVersion(nodeRef);
            String versionLabel = currentlyVersion.getVersionLabel();
            res.getWriter().write("\nVersion Number: " + versionLabel);
            //Version newVersion = versionService.createVersion(nodeRef, new HashMap<String,Serializable>());

        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("\nRaise an error in Check Version");
        }
        res.setContentType(MimetypeMap.MIMETYPE_TEXT_PLAIN);
        res.setContentEncoding(StandardCharsets.UTF_8.name());
    }

    /**
     * @param value the registry to set
     */
    public void setRegistry(final ServiceRegistry value) {
        this.registry = value;
    }
    /**
     * @param value the repository to set
     */
    public void setRepository(final Repository value) {
        this.repository = value;
    }


}
