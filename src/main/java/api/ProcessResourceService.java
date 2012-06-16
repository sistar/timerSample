package api;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class ProcessResourceService implements ProcessResource {

    @Inject
    private TaskService taskService;

    @Inject
    private RuntimeService runtimeService;

    @Override
    public List<String> listTimerProcesses() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void receiveChecks(String msg) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("support-297");
    }
}
