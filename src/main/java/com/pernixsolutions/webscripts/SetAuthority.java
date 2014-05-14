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
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.cmr.security.PersonService;


public class SetAuthority extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(SetAuthority.class);
        LOG.debug("StartExecImpl()");
        LOG.debug(RequestHelper.getRequestString(req));

        String nodeRefPath = req.getParameter(ParametersConstants.NODE_REF);
        String username = req.getParameter(ParametersConstants.USERNAME);

        try{

            PermissionService permissionService = registry.getPermissionService();
            PersonService personService = registry.getPersonService();

            if(personService.personExists(username)){
                setAuthority(permissionService, nodeRefPath, username);
            }

            res.getWriter().write("Set Authority Completed");
            LOG.debug("Set Authority Completed");
        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("Raise an error");
        }


        res.setContentType(MimetypeMap.MIMETYPE_TEXT_PLAIN);
        res.setContentEncoding(StandardCharsets.UTF_8.name());


    }

    private void setAuthority(final PermissionService permissionService, final String nodeRefPath, final String username){
        NodeRef nodeRef = NodeRef.getNodeRefs(nodeRefPath).get(0);
        permissionService.setPermission(nodeRef, username, PermissionService.COORDINATOR, true);
    }

    /**
     * @param value the registry to set
     */
    public void setRegistry(final ServiceRegistry value) {
        this.registry = value;
    }

}
