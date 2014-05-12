package com.pernixsolutions.webscripts;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService.PersonInfo;
import org.alfresco.service.namespace.QName;


public class CopyrightedImages extends AbstractWebScript {

    /**
     * {@inheritDoc}
     */

    /** Alfresco service registry */
    private ServiceRegistry registry;
    /** Alfresco repository helper */
    private Repository repository;

    /** QName for the copyrighted aspect */
    private static final QName ASPECT_COPYRIGHTED = QName.createQName("http://www.flatironssolutions.com/model/fsctraining/1.0", "copyrighted");
    /** QName for the copyrighted property */
    private static final QName PROP_COPYRIGHT = QName.createQName("http://www.flatironssolutions.com/model/fsctraining/1.0", "copyright");

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


    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {

        // TODO Auto-generated method stub
        try {
            JSONObject jsonObject = new JSONObject();
            NodeRef person = repository.getPerson();

            NodeService nodeService = registry.getNodeService();
            NodeRef userhome = repository.getUserHome(person);

            PersonInfo personInfo = registry.getPersonService().getPerson(person);
            jsonObject.put("username", personInfo.getUserName());
            jsonObject.put("firstName", personInfo.getFirstName());
            jsonObject.put("lastName", personInfo.getLastName());


            JSONArray picturesArray = new JSONArray();
            NodeRef pictures = nodeService.getChildByName(
                        userhome, ContentModel.ASSOC_CONTAINS, "Pictures");
            if (pictures != null) {
                List<ChildAssociationRef> children =
                    nodeService.getChildAssocs(pictures);
                for (ChildAssociationRef child : children) {
                    NodeRef picture = child.getChildRef();
                    if (nodeService.hasAspect(picture, ASPECT_COPYRIGHTED)) {
                        JSONObject jsonPicture = new JSONObject();
                        jsonPicture.put("name",
                            nodeService.getProperty(picture, ContentModel.PROP_NAME));
                        jsonPicture.put("copyright",
                            nodeService.getProperty(picture, PROP_COPYRIGHT));
                        jsonPicture.put("noderef", picture.toString());
                        picturesArray.put(jsonPicture);
                    }
                }
            }
            jsonObject.put("pictures", picturesArray);

            res.getWriter().write(jsonObject.toString());
        } catch (JSONException e) {
            throw new WebScriptException("Unable to serialize JSON");
        }
    }



}
