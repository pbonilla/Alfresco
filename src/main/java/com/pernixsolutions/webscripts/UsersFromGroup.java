package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import com.pernixsolutions.webscripts.WebscriptUtil.ParametersConstants;
import com.pernixsolutions.webscripts.WebscriptUtil.RequestHelper;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthorityService;


public class UsersFromGroup extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;

    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        Logger LOG = Logger.getLogger(UsersFromGroup.class);
        LOG.debug("StartExecImpl()");
        LOG.debug(RequestHelper.getRequestString(req));

        String groupName = req.getParameter(ParametersConstants.GROUP_NAME);

        try{
            NodeService nodeService = registry.getNodeService();
            AuthorityService authorityService = registry.getAuthorityService();
            String usersString ="";
            if(authorityService.authorityExists(groupName)){
                usersString = getUsers(authorityService, nodeService, groupName);
                res.getWriter().write(usersString);
            }
            LOG.debug("List of Users:" + usersString);

        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("Raise an error on UsersFromGroup : Invalid parameters");
        }
        res.setContentType(MimetypeMap.MIMETYPE_JSON);
        res.setContentEncoding(StandardCharsets.UTF_8.name());

    }

    private String getUsers(final AuthorityService authorityService, final NodeService nodeService, final String groupName){
        String usersString = "";
        NodeRef groupRef = authorityService.getAuthorityNodeRef(groupName);
        List<ChildAssociationRef> authorities = nodeService.getChildAssocs(groupRef);
        for(ChildAssociationRef association :authorities){
            usersString = getUserInfo(association, nodeService);
        }

        return usersString;
    }

    private String getUserInfo(final ChildAssociationRef association, final NodeService nodeService){
        NodeRef child = NodeRef.getNodeRefs(association.getChildRef().toString()).get(0);
        String firstname = nodeService.getProperty(child, ContentModel.PROP_FIRSTNAME).toString();
        String lastname = nodeService.getProperty(child, ContentModel.PROP_LASTNAME).toString();
        return firstname+ " "+lastname +"\n";
    }

    /**
     * @param value the registry to set
     */
    public void setRegistry(final ServiceRegistry value) {
        this.registry = value;
    }



}
