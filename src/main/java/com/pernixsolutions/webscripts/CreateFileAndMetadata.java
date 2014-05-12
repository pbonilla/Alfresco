package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;


public class CreateFileAndMetadata extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;
    /** Alfresco repository helper */
    private Repository repository;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CreateFileAndMetadata.class);

        LOG.debug("StartExecImpl()");
        final String parenteNodeRefPath = req.getParameter("parentFolder");
        final String filename = req.getParameter("filename");
        final String author = req.getParameter("author");
        final String title = req.getParameter("title");

        try{
            final NodeService nodeService = registry.getNodeService();
            final FileFolderService fileFolderService = registry.getFileFolderService();
            final NodeRef parentNodeRef = NodeRef.getNodeRefs(parenteNodeRefPath).get(0);
            FileInfo fileInfo = fileFolderService.create(parentNodeRef, filename, ContentModel.TYPE_CONTENT);
            NodeRef newFile = fileInfo.getNodeRef();
            nodeService.setProperty(newFile, ContentModel.PROP_TITLE, title);
            nodeService.setProperty(newFile, ContentModel.PROP_AUTHOR, author);

            res.getWriter().write("File created");

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
    /**
     * @param value the repository to set
     */
    public void setRepository(final Repository value) {
        this.repository = value;
    }


}
