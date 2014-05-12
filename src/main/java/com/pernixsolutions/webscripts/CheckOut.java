package com.pernixsolutions.webscripts;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.coci.CheckOutCheckInService;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.NodeRef;


public class CheckOut extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;
    /** Alfresco repository helper */
    private Repository repository;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CreateFileAndMetadata.class);

        LOG.debug("StartExecImpl()");
        final String fileToDownloadPath = req.getParameter("nodeRef");

        try{
            NodeRef nodeRef = NodeRef.getNodeRefs(fileToDownloadPath).get(0);

            FileFolderService fileFolderService = registry.getFileFolderService();
            FileInfo fileInfo = fileFolderService.getFileInfo(nodeRef);
            ContentReader contentReader = fileFolderService.getReader(nodeRef);
            File temp = new File(fileInfo.getName());
            contentReader.getContent(temp);
            res.getWriter().write("File downloaded");

            CheckOutCheckInService checkInOutService = registry.getCheckOutCheckInService();
            checkInOutService.checkout(nodeRef);
            res.getWriter().write("CheckOut Succesfully");

        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("Raise an error downloading the file");
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