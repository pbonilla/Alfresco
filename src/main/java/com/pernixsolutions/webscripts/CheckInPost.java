package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.extensions.surf.util.Content;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.servlet.FormData;
import org.springframework.extensions.webscripts.servlet.FormData.FormField;

import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.coci.CheckOutCheckInService;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.version.Version;
import org.alfresco.service.cmr.version.VersionService;


public class CheckInPost extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;
    /** Alfresco repository helper */
    private Repository repository;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CheckInPost.class);

        LOG.debug("StartExecImpl()");
        final String nodeRefPath = req.getParameter("nodeRef");
        res.getWriter().write(nodeRefPath);
        Content fileContent = null;

            FormData formData = (FormData)req.parseContent();
            for(FormField field : formData.getFields()){
                if(field.getIsFile()){

                    fileContent = field.getContent();

                }
            }
            res.getWriter().write("\nFile uploaded");

        try{
            CheckOutCheckInService cocis = registry.getCheckOutCheckInService();
            VersionService versionService = registry.getVersionService();
            FileFolderService fileFolderService = registry.getFileFolderService();

            NodeRef nodeRef = NodeRef.getNodeRefs(nodeRefPath).get(0);
            NodeRef workingNodeRef = cocis.getWorkingCopy(nodeRef);
            res.getWriter().write("\nI'm here");
            cocis.checkin(workingNodeRef,new HashMap<String,Serializable>());
            Version newVersion = versionService.createVersion(nodeRef, new HashMap<String,Serializable>());
            ContentWriter contentWriter = fileFolderService.getWriter(nodeRef);
            contentWriter.putContent(fileContent.getContent()+"\nLast modified by Pablo Bonilla");

        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("\nRaise an error in Check In");
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
