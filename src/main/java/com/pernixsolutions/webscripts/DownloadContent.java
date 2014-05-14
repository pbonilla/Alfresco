package com.pernixsolutions.webscripts;

import java.io.File;
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
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.NodeRef;


public class DownloadContent extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;

    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CreateFileAndMetadata.class);
        LOG.debug("StartExecImpl()");
        LOG.debug(RequestHelper.getRequestString(req));

        final String fileToDownloadPath = req.getParameter(ParametersConstants.NODE_REF);

        try{
            FileFolderService fileFolderService = registry.getFileFolderService();

            NodeRef nodeRef = NodeRef.getNodeRefs(fileToDownloadPath).get(0);

            FileInfo fileInfo = fileFolderService.getFileInfo(nodeRef);
            ContentReader contentReader = fileFolderService.getReader(nodeRef);
            File temp = new File(fileInfo.getName());
            contentReader.getContent(temp);
            res.getWriter().write("File downloaded");
            LOG.debug("File Downloaded");
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

}
