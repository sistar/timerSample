package demo;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.jboss.solder.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;


/**
 * Listener zur Erzeugung eines Audit-Eintrages auf Basis des Namens des Elements, an dem der Listener hängt.
 * <p/>
 * Hierbei werden die Texte <code>BusinessState</code> und <code>TaskState</code> unabhängig von der Groß- und
 * Kleinschreibung ausgewertet. Es können beide Texte gleichzeitig vorkommen, dann müssen diese mit einem Semikolon
 * getrennt werden. Jeder Text hat hinter einem <code>=</code>-Zeichen den jeweiligen Wert stehen:
 * <p/>
 * Beispiele:
 * <ul>
 * <li>BusinessState=Hallo Welt;TaskState=8</li>
 * <li>TaskState=8</li>
 * <li>BusinessState=Hallo Welt</li>
 * </ul>
 * Zur Verwendung wird das IntermediateThrowingEvent im Prozess den Listener wie folgt referenziert:
 * <code>&lt;activiti:executionListener event="start" delegateExpression="#{auditStateListener}"/&gt;</code>
 */
@Named
public class AuditStateListener implements ExecutionListener {

    @Inject
    Logger logger;

    @Inject
    ProcessAuditService processAuditService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        ExecutionEntity entity = (ExecutionEntity) execution;
        final String name = removeWhitespaceAndIllegalCharacters((String) entity.getActivity().getProperty("name"));

        String businessState = null;
        Integer taskState = null;

        //filter the state:
        final String[] states = name.split(";");
        for (String state : states) {
            if (state.split("=")[0].trim().equalsIgnoreCase("businessState")) {
                businessState = state.split("=")[1].trim();
            } else if (state.split("=")[0].trim().equalsIgnoreCase("taskState")) {
                taskState = Integer.parseInt(state.split("=")[1].trim());
            }
        }
        logger.info("Writing Audit with businessState = '" + businessState + "' and taskState = '" + taskState + "'.");
        processAuditService.audit(businessState, taskState);
    }

    /**
     * Entfernt alle Control-Charaktere und ersetzt mehrere Whitespaces in Folge durch einen einzelnen Whitespace.
     */
    private String removeWhitespaceAndIllegalCharacters(String property) {
        return property.replaceAll("\\p{C}", " ").replaceAll("\\s+", " ");
    }

}
