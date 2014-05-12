/*
 * Copyright (c) 2014, Flatirons Solutions, Inc. All Rights Reserved.
 */

package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.cmr.security.PersonService;


public class UsersFromGroup extends AbstractWebScript{


    /** Alfresco service registry */
    private ServiceRegistry registry;
    /** Alfresco repository helper */
    private Repository repository;


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        String groupName = req.getParameter("groupName");

        try{
            NodeService nodeService = registry.getNodeService();
            PermissionService permissionService = registry.getPermissionService();
            AuthorityService authorityService = registry.getAuthorityService();
            PersonService personService = registry.getPersonService();
            //Set<String> authorities = authorityService.getAuthorities();
            if(authorityService.authorityExists(groupName)){
                //NodeRef groupRef = NodeRef.getNodeRefs(groupName).get(0);
                NodeRef groupRef = authorityService.getAuthorityNodeRef(groupName);
                List<ChildAssociationRef> authorities = nodeService.getChildAssocs(groupRef);
                for(ChildAssociationRef association :authorities){
                    NodeRef child = NodeRef.getNodeRefs(association.getChildRef().toString()).get(0);
                    String firstname = nodeService.getProperty(child, ContentModel.PROP_FIRSTNAME).toString();
                    String lastname = nodeService.getProperty(child, ContentModel.PROP_LASTNAME).toString();
                    res.getWriter().write(firstname+ " "+lastname +"\n");
                }
            }

            /*for(String authority : authorities){
                res.getWriter().write(authority+ "\n");
            }*/
        }catch(Exception e){
            e.printStackTrace();
            res.getWriter().write("Raise an error on UsersFromGroup : Invalid parameters");
        }


        res.setContentType(MimetypeMap.MIMETYPE_JSON);
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
