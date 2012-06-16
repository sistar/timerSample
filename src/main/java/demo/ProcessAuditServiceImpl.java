package demo;

import org.activiti.cdi.BusinessProcess;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;

import javax.inject.Inject;
import javax.inject.Named;

@Named("processAuditService")
public class ProcessAuditServiceImpl implements ProcessAuditService {

    @Inject
    private BusinessProcess businessProcess;

    @Inject
    private AuditService auditService;

    @Inject
    private RepositoryService repositoryService;

    public void audit(String businessState, Integer taskState) throws Exception {
        Long wasabiId = businessProcess.getVariable("X");
        String processInstanceId = businessProcess.getProcessInstanceId();
        ProcessDefinition startingProcessDef = findStartingProcessDefinition();
        auditService.audit(businessState + " " + "" + taskState + startingProcessDef.toString());
    }

    private ProcessDefinition findStartingProcessDefinition() {
        ExecutionEntity execution = (ExecutionEntity) businessProcess.getExecution();
        while (execution.getParent() != null) {
            execution = execution.getParent();
        }
        return findProcessDefinition(execution.getProcessDefinitionId());
    }

    private ProcessDefinition findProcessDefinition(String processDefinitionId) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
    }

}
