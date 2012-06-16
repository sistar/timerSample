import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Diese Klasse dient der Aktivierung von JAX-RS und der Herstellung des Mappings von Anfragen an den über die
 * Annotation {@link ApplicationPath} definierten Pfad auf JAX-RS.
 */
@ApplicationPath("/api")
public class JaxRsActiviator extends Application {
}