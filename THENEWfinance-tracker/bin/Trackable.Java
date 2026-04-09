package model;

/**
 * Interface that enforces tracking behavior across transaction types.
 * Any class implementing Trackable must define how it is tracked/reported.
 */
public interface Trackable {

    /**
     * Returns a tracking summary string for the implementing transaction.
     */
    String track();

    /**
     * Default method: returns whether this transaction should be flagged.
     * Can be overridden by implementing classes for custom logic.
     */
    default boolean isFlagged(double threshold) {
        return false; // Default: not flagged — override for specific behavior
    }
}