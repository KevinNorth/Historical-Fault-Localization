package edu.unl.knorth.historical_fault_localization.intermediate_data;

import java.util.Objects;

/**
 * Represents a single statement in the code under test.
 */
public final class StatementData {
    private final int lineNumber;
    private final String file;

    /**
     * @param lineNumber The line number of the statement in its file.
     * @param file The path to the source code file containing the statement.
     */
    public StatementData(int lineNumber, String file) {
        this.lineNumber = lineNumber;
        this.file = file;
    }

    /**
     * @return The line number of the statement in its file.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return The path to the source code file containing the statement.
     */
    public String getFile() {
        return file;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.lineNumber;
        hash = 71 * hash + Objects.hashCode(this.file);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StatementData other = (StatementData) obj;
        if (this.lineNumber != other.lineNumber) {
            return false;
        }
        if (!Objects.equals(this.file, other.file)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Statement{" + "lineNumber=" + lineNumber
                + ", file=" + file + '}';
    }
}