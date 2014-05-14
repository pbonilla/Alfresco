package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.servlet.FormData;
import org.springframework.extensions.webscripts.servlet.FormData.FormField;

import com.pernixsolutions.webscripts.WebscriptUtil.ParametersConstants;
import com.pernixsolutions.webscripts.WebscriptUtil.RequestHelper;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;


public class UploadContent extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;

    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CreateFileAndMetadata.class);
        LOG.debug("StartExecImpl()");
        LOG.debug(RequestHelper.getRequestString(req));

        final String parentNodeRefPath = req.getParameter(ParametersConstants.PARENT_NODE);

        try{

            final FileFolderService fileFolderService = registry.getFileFolderService();
            FormField field = findFileInParameters(req, res, parentNodeRefPath, fileFolderService);
            NodeRef uploadedFile = createFile(parentNodeRefPath, fileFolderService, field);
            writeContentOnNewFile(fileFolderService, uploadedFile, field);

            res.getWriter().write("File uploaded");
            LOG.debug("File Uploaded");
        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("Raise an error in Folder creation");
        }


        res.setContentType(MimetypeMap.MIMETYPE_TEXT_PLAIN);
        res.setContentEncoding(StandardCharsets.UTF_8.name());


    }

    private FormField findFileInParameters(final WebScriptRequest req,final WebScriptResponse res,
            final String parentNodeRefPath, final FileFolderService fileFolderService) throws IOException{
        FormData formData = (FormData)req.parseContent();
        for(FormField field : formData.getFields()){
            if(field.getIsFile()){
                res.getWriter().write(field.getFilename());
                return field;
            }
        }
        return null;
    }

    private NodeRef createFile(final String parentNodeRefPath,final FileFolderService fileFolderService,final FormField field){
        final NodeRef parentNodeRef = NodeRef.getNodeRefs(parentNodeRefPath).get(0);
        FileInfo fileInfo = fileFolderService.create(parentNodeRef, field.getFilename(), ContentModel.TYPE_CONTENT);
        return fileInfo.getNodeRef();
    }

    private void writeContentOnNewFile(final FileFolderService fileFolderService, final NodeRef uploadedFile,final FormField field ){
        ContentWriter contentWriter = fileFolderService.getWriter(uploadedFile);
        contentWriter.guessMimetype(field.getFilename());
        contentWriter.putContent(field.getInputStream());
    }

    /**
     * @param value the registry to set
     */
    public void setRegistry(final ServiceRegistry value) {
        this.registry = value;
    }



}
