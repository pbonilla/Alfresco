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
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;


public class SetTitleProperty extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(SetTitleProperty.class);
        LOG.debug("StartExecImpl()");
        LOG.debug(RequestHelper.getRequestString(req));

        String nodeRefPath = req.getParameter(ParametersConstants.NODE_REF);
        String newTitle = req.getParameter(ParametersConstants.TITLE);

        try{
            NodeService nodeService = registry.getNodeService();
            setProperty(nodeService, nodeRefPath, newTitle);

            res.getWriter().write("Setted Title with "+newTitle);
            LOG.debug("Set Title Completed");
        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("Raise an error");
        }


        res.setContentType(MimetypeMap.MIMETYPE_TEXT_PLAIN);
        res.setContentEncoding(StandardCharsets.UTF_8.name());


    }

    private void setProperty(final NodeService nodeService, final String nodeRefPath, final String newTitle){
        NodeRef nodeRef = NodeRef.getNodeRefs(nodeRefPath).get(0);
        nodeService.setProperty(nodeRef, ContentModel.PROP_TITLE, newTitle);
    }

    /**
     * @param value the registry to set
     */
    public void setRegistry(final ServiceRegistry value) {
        this.registry = value;
    }

}
