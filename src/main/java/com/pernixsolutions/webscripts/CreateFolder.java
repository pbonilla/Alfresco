package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import com.pernixsolutions.webscripts.WebscriptUtil.ParametersConstants;
import com.pernixsolutions.webscripts.WebscriptUtil.RequestHelper;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.NodeRef;


public class CreateFolder extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;

    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CreateFolder.class);
        LOG.debug("StartExecImpl()");
        LOG.debug(RequestHelper.getRequestString(req));

        String parenteNodeRefPath = req.getParameter(ParametersConstants.PARENT_NODE);
        String newFolderName = req.getParameter(ParametersConstants.FOLDER_NAME);

        try{

            FileFolderService fileFolderService = registry.getFileFolderService();
            NodeRef parentNodeRef = NodeRef.getNodeRefs(parenteNodeRefPath).get(0);
            fileFolderService.create(parentNodeRef, newFolderName, ContentModel.TYPE_FOLDER);
            res.getWriter().write("Folder created");
            LOG.debug("Folder Created");
        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("Raise an error in Folder creation");
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

}
