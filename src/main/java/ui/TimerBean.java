package ui;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.jboss.solder.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

@Model
public class TimerBean {

    @Inject
    Logger logger;

    @Inject
    private RuntimeService runtimeService;

    @Inject @SessionScoped
    private TimerEntity timerEntity;

    private ProcessInstance processInstance;

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setTimerEntity(TimerEntity timerEntity) {
        this.timerEntity = timerEntity;
    }

    public TimerEntity getTimerEntity() {
        return timerEntity;
    }

    public String startProcess() {
        logger.info("Starte Prozess...");
        processInstance  = runtimeService.startProcessInstanceByKey("support-297");
        return null;
    }
}
