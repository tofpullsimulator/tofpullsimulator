package org.eos.tof.common.counters;

/**
 * Interface for a counter object.
 *
 * @author Eos
 */
public interface Counter {

    /**
     * Get the current value of the metric.
     *
     * @param metricName The metric name of the counter.
     * @return The value of the metric.
     */
    Integer get(final String metricName);

    /**
     * Set the value of the metric.
     *
     * @param metricName The metric name of the counter.
     * @param newValue   The new value of the metric.
     */
    void set(final String metricName, final int newValue);

    /**
     * Increment the metric.
     *
     * @param metricName The metric name of the counter.
     */
    void increment(final String metricName);

    /**
     * Increment the counter.
     */
    void increment();

    /**
     * Reset the counter.
     *
     * @param metricName The metric name of the counter.
     */
    void reset(final String metricName);

    /**
     * Reset the counter.
     */
    void reset();
}
