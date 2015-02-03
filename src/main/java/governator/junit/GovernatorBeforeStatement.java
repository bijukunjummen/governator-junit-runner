package governator.junit;

import com.netflix.governator.lifecycle.LifecycleManager;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * Junit {@link org.junit.runners.model.Statement} that will start up a {@link com.netflix.governator.lifecycle.LifecycleManager}
 * before the tests run
 *
 * @author Biju Kunjummen
 */
public class GovernatorBeforeStatement extends Statement {

    private final LifecycleManager lifecycleManager;
    private final Statement next;

    public GovernatorBeforeStatement(LifecycleManager lifecycleManager, Statement next) {
        this.lifecycleManager = lifecycleManager;
        this.next = next;
    }

    @Override
    public void evaluate() throws Throwable {
        startLifecycleManager(this.lifecycleManager);
        next.evaluate();
    }

    private void startLifecycleManager(LifecycleManager lifecycleManager) throws Exception {
        lifecycleManager.start();
    }
}
