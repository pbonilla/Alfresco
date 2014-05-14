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
import org.alfresco.service.cmr.coci.CheckOutCheckInService;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.version.Version;
import org.alfresco.service.cmr.version.VersionService;


public class CheckIn extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CheckIn.class);

        LOG.debug("StartExecImpl()");

        final String nodeRefPath = req.getParameter(ParametersConstants.NODE_REF);

        LOG.debug(RequestHelper.getRequestString(req));
        try{
            CheckOutCheckInService cocis = registry.getCheckOutCheckInService();
            VersionService versionService = registry.getVersionService();
            ContentService contentService = registry.getContentService();
            FileFolderService fileFolderService = registry.getFileFolderService();

            NodeRef nodeRef = NodeRef.getNodeRefs(nodeRefPath).get(0);
            NodeRef workingNodeRef = cocis.getWorkingCopy(nodeRef);
            cocis.checkin(workingNodeRef,null);
            Version newVersion = versionService.createVersion(nodeRef, null);
            ContentWriter contentWriter = fileFolderService.getWriter(nodeRef);
            contentWriter.putContent("Last modified by Pablo Bonilla");

            LOG.debug("CheckIn Successfully");
        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("Raise an error in Check In");
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
