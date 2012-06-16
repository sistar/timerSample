package ui;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

// Used to verify that all processes can be deployed (WARNING: nothing more than that is tested here!)
@RunWith(Arquillian.class)
public class AllProcessesDeploymentTest {

    private static final Logger LOGGER = Logger.getLogger(AllProcessesDeploymentTest.class.getName());
    @Inject
    private RepositoryService repositoryService;

    @Inject
    RuntimeService runtimeService;

    @Deployment
    public static Archive<WebArchive> deployAllProcesses() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "AllProcessesDeploymentTest.war")
                .addAsResource("processes/support_297.bpmn")
                .addAsResource("processes/support_297.png").addClass(com.camunda.support.IncCounterDelegate.class);
        foxEngineDependencies(webArchive);
        LOGGER.info(webArchive.toString(true));
        return webArchive;
    }

    /**
     * @param target the target to enrich with fox deps
     * @return Abhängigkeiten der FOX engine. Diese enthält auch den Fox Client zur Ausführung von Prozessen in Activiti.
     */
    public static WebArchive foxEngineDependencies(WebArchive target) {
        MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadEffectivePom("pom.xml").up();
        return target
                .addAsManifestResource("ARQUILLIAN-MANIFEST-JBOSS7.MF", "MANIFEST.MF")
                .addAsWebResource("META-INF/processes.xml", "WEB-INF/classes/META-INF/processes.xml")
                .addAsLibraries(resolver.artifact("com.camunda.fox.platform:fox-platform-client:6.0.0").resolveAsFiles());
    }

    @Test
    public void shouldHaveDeployedProcessesWithNames() {
        List<ProcessDefinition> processes = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        for (ProcessDefinition process : processes) {
            assertNotNull(" für Prozess" + process.getKey(), process.getName());
            LOGGER.info("Process '" + process.getName() + "' [key: " + process.getKey() + ", version: " + process.getVersion() + "]");
        }
    }

    @Test
    public void shouldHaveDeployedSupport297() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("support-297").latestVersion().singleResult();
        runProcessAsynchronously("support-297");

    }

    /**
     * Start process and wait until it is ended or suspended or a timeout is reached.
     */
    public ProcessInstance runProcessAsynchronously(String processKey) {
        try {
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
            waitForProcess(processInstance.getProcessInstanceId(), 15000);
            return processInstance;
        } catch (Exception e) {
            // we are not interested in any exception that might occur after this point in this test
            return null;
        }
    }

    private void waitForProcess(String processInstanceId, int maximumWaitTime) throws InterruptedException {
        long timeoutInMillis = System.currentTimeMillis() + maximumWaitTime;

        boolean processIsEndedOrSuspended;
        boolean timeoutReached;

        do {
            Thread.sleep(500);
            timeoutReached = System.currentTimeMillis() > timeoutInMillis;

            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processIsEndedOrSuspended = (processInstance == null) || processInstance.isEnded() || processInstance.isSuspended();
        } while (!processIsEndedOrSuspended && !timeoutReached);
    }

}
