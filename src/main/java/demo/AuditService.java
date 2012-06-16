package demo;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Service, der es erlaubt Audits zu erzeugen und Abfragen auf Audits vorzunehmen. Dieser Service ist nicht an einen
 * Prozess-Kontext gebunden und kann auch außerhalb von Prozessen verwendet werden.
 */
public class AuditService implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AuditService.class.getName());

    public void audit(String s) {
        LOGGER.info("I AUDIT" +s);
    }

}
