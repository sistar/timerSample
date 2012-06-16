package com.camunda.support;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.jboss.solder.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class IncCounterDelegate implements JavaDelegate {

    @Inject
    Logger log;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Integer variable = (Integer) execution.getVariable("counter");
        if (variable == null) {
            variable = 0;
        }
        variable = variable + 1;

        log.info("increased counter to " + variable);

//    Thread.sleep(300000+6000);

        execution.setVariable("counter", variable);
    }

}
