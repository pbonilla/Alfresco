package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.servlet.FormData;
import org.springframework.extensions.webscripts.servlet.FormData.FormField;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;


public class UploadContent extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;
    /** Alfresco repository helper */
    private Repository repository;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CreateFileAndMetadata.class);

        LOG.debug("StartExecImpl()");
        final String parentNodeRefPath = req.getParameter("parentFolder");
        /*final String filename = req.getParameter("filename");
        final String author = req.getParameter("author");
        final String title = req.getParameter("title");*/

        try{
            FormData formData = (FormData)req.parseContent();
            for(FormField field : formData.getFields()){
                if(field.getIsFile()){
                    res.getWriter().write(field.getFilename());

                    final FileFolderService fileFolderService = registry.getFileFolderService();
                    final NodeRef parentNodeRef = NodeRef.getNodeRefs(parentNodeRefPath).get(0);
                    FileInfo fileInfo = fileFolderService.create(parentNodeRef, field.getFilename(), ContentModel.TYPE_CONTENT);
                    NodeRef uploadedFile = fileInfo.getNodeRef();
                    ContentWriter contentWriter = fileFolderService.getWriter(uploadedFile);
                    contentWriter.guessMimetype(field.getFilename());
                    contentWriter.putContent(field.getInputStream());

                }
            }

            res.getWriter().write("File uploaded");

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
