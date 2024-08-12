
package org.traccar.storage.query;

public class Order {

    private final String column;
    private final boolean descending;
    private final int limit;

    public Order(String column) {
        this(column, false, 0);
    }

    public Order(String column, boolean descending, int limit) {
        this.column = column;
        this.descending = descending;
        this.limit = limit;
    }

    public String getColumn() {
        return column;
    }

    public boolean getDescending() {
        return descending;
    }

    public int getLimit() {
        return limit;
    }

}
