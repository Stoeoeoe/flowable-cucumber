package org.flowable.testing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for Flowable Cucumber
 */
@Configuration
@ConfigurationProperties(prefix = "flowable.testing")
public class FlowableCucumberProperties {

    private int asyncInterval = 100;
    private int asyncTimeout = 1000;

    /**
     * Gets how many milliseconds the async executor will wait until polling for the next job if running a feature with the @Async tag.
     * @return The async executor polling interval.
     */
    public int getAsyncInterval() {
        return asyncInterval;
    }

    /**
     * Sets how many milliseconds the async executor will wait until polling for the next job if running a feature with the @Async tag.
     * @param asyncInterval The async executor polling interval.
     */
    public void setAsyncInterval(int asyncInterval) {
        this.asyncInterval = asyncInterval;
    }

    /**
     * Gets how many milliseconds the async executor will wait in total until running into a timeout if running a feature with the @Async tag.
     * @return The async executor timeout.
     */
    public int getAsyncTimeout() {
        return asyncTimeout;
    }

    /**
     * Sets how many milliseconds the async executor will wait in total until running into a timeout if running a feature with the @Async tag.
     * @param asyncTimeout The async executor timeout.
     */
    public void setAsyncTimeout(int asyncTimeout) {
        this.asyncTimeout = asyncTimeout;
    }
}
