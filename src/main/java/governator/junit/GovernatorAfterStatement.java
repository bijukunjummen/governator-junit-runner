package governator.junit;

import com.netflix.governator.lifecycle.LifecycleManager;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * Junit {@link org.junit.runners.model.Statement} that will close a {@link com.netflix.governator.lifecycle.LifecycleManager}
 * after the tests are complete
 *
 * @author Biju Kunjummen
 */
public class GovernatorAfterStatement extends Statement {

    private final LifecycleManager lifecycleManager;
    private final Statement next;

    public GovernatorAfterStatement(LifecycleManager lifecycleManager, Statement next) {
        this.lifecycleManager = lifecycleManager;
        this.next = next;
    }

    @Override
    public void evaluate() throws Throwable {
        try {
            next.evaluate();
        } finally {
            stopLifecycleManager(this.lifecycleManager);
        }
    }

    private void stopLifecycleManager(LifecycleManager lifecycleManager) throws InitializationError {
        try {
            lifecycleManager.close();
        } catch (Exception e) {
            throw new InitializationError(e);
        }
    }
}
