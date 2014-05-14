package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import com.pernixsolutions.webscripts.WebscriptUtil.ParametersConstants;
import com.pernixsolutions.webscripts.WebscriptUtil.RequestHelper;

import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;


public class CheckProperties extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(CheckInPost.class);

        LOG.debug("StartExecImpl()");
        LOG.debug(RequestHelper.getRequestString(req));

        try{
            final String nodeRefPath = req.getParameter(ParametersConstants.NODE_REF);
            res.getWriter().write(nodeRefPath);

            NodeService nodeService = registry.getNodeService();

            NodeRef nodeRef = NodeRef.getNodeRefs(nodeRefPath).get(0);
            String propertiesString = getProperties(nodeService, nodeRef);

            res.getWriter().write(propertiesString);
            LOG.debug(propertiesString);


        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("\nRaise an error in Check Version");
        }
        res.setContentType(MimetypeMap.MIMETYPE_TEXT_PLAIN);
        res.setContentEncoding(StandardCharsets.UTF_8.name());
    }

    private String getProperties(final NodeService nodeService, final NodeRef nodeRef){
        HashMap<QName, Serializable> properties = (HashMap<QName, Serializable>) nodeService.getProperties(nodeRef);
        Set<QName> keys = properties.keySet();

        String propertiesString = "";
        for(QName key : keys){
            key.getLocalName();
            propertiesString += "\n"+key.getLocalName()+": "+properties.get(key).toString();

        }
        return propertiesString;
    }

    /**
     * @param value the registry to set
     */
    public void setRegistry(final ServiceRegistry value) {
        this.registry = value;
    }

}
