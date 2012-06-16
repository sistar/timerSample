package ui;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.jboss.solder.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@ManagedBean(name = "processList")
@RequestScoped
public class ProcessListBean {

    @Inject
    private Logger log;

    @Inject
    private RepositoryService repositoryService;

    private List<ProcessDefinition> processDefinitions;

    @PostConstruct
    public void init() {
        processDefinitions = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionKey().asc().list();
        log.info(processDefinitions.toString());
    }

    public List<ProcessDefinition> getList() {
        return processDefinitions;
    }

}
