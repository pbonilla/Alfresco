
package com.pernixsolutions.workflow;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;

import org.alfresco.repo.workflow.activiti.BaseJavaDelegate;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.workflow.WorkflowInstance;
import org.alfresco.service.cmr.workflow.WorkflowService;

public class CreateCopies extends BaseJavaDelegate {

    private final Logger LOG = Logger.getLogger(AddCopyright.class);

    @Override
    public void execute(final DelegateExecution execution) throws Exception {

        ServiceRegistry registry = getServiceRegistry();
        WorkflowService workflowService = registry.getWorkflowService();
        NodeService nodeService = registry.getNodeService();
        FileFolderService fileFolderService = registry.getFileFolderService();

        String workflowInstanceId =
            (String) execution.getVariable("workflowinstanceid");
        WorkflowInstance workflowInstance =
            workflowService.getWorkflowById(workflowInstanceId);
        NodeRef workflowPackage = workflowInstance.getWorkflowPackage();

        List<ChildAssociationRef> workflowContents =
            nodeService.getChildAssocs(workflowPackage);
        if (workflowContents != null) {
            for (ChildAssociationRef child : workflowContents) {
                NodeRef doc = child.getChildRef();
                String filename = fileFolderService.getFileInfo(doc).getName();
                fileFolderService.copy(doc, nodeService.getParentAssocs(doc).get(0).getParentRef(), "(Copy)"+filename);
  } }

    }

    /**
     * @param args
     */
    public static void main(final String[] args) {

        // TODO Auto-generated method stub

    }

}
