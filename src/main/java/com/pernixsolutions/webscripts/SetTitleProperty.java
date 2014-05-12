package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;


public class SetTitleProperty extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;
    /** Alfresco repository helper */
    private Repository repository;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        String nodeRefPath = req.getParameter("nodeRef");
        String newTitle = req.getParameter("newTitle");

        try{
            NodeService nodeService = registry.getNodeService();
            NodeRef nodeRef = NodeRef.getNodeRefs(nodeRefPath).get(0);
            nodeService.setProperty(nodeRef, ContentModel.PROP_TITLE, newTitle);

            res.getWriter().write(nodeRef.getId()+"\n");
            res.getWriter().write(newTitle);
        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("Raise an error");
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
