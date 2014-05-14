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
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;


public class CreateFileAndMetadata extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CreateFileAndMetadata.class);
        LOG.debug("StartExecImpl()");
        LOG.debug(RequestHelper.getRequestString(req));

        final String parenteNodeRefPath = req.getParameter(ParametersConstants.PARENT_NODE);
        final String filename = req.getParameter(ParametersConstants.FILENAME);
        final String author = req.getParameter(ParametersConstants.AUTHOR);
        final String title = req.getParameter(ParametersConstants.TITLE);

        try{
            final NodeService nodeService = registry.getNodeService();
            final FileFolderService fileFolderService = registry.getFileFolderService();
            FileInfo fileInfo = createFile(parenteNodeRefPath, fileFolderService, filename);
            setMetadata(nodeService, fileInfo, title, author);

            res.getWriter().write("File created");
            LOG.debug("File Created");
        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("Raise an error in Folder creation");
        }


        res.setContentType(MimetypeMap.MIMETYPE_TEXT_PLAIN);
        res.setContentEncoding(StandardCharsets.UTF_8.name());


    }

    private FileInfo createFile(final String parenteNodeRefPath, final FileFolderService fileFolderService, final String filename){
        final NodeRef parentNodeRef = NodeRef.getNodeRefs(parenteNodeRefPath).get(0);
         return fileFolderService.create(parentNodeRef, filename, ContentModel.TYPE_CONTENT);
    }

    private void setMetadata(final NodeService nodeService, final FileInfo fileInfo, final String title, final String author ){
        NodeRef newFile = fileInfo.getNodeRef();
        nodeService.setProperty(newFile, ContentModel.PROP_TITLE, title);
        nodeService.setProperty(newFile, ContentModel.PROP_AUTHOR, author);
    }

    /**
     * @param value the registry to set
     */
    public void setRegistry(final ServiceRegistry value) {
        this.registry = value;
    }

}
