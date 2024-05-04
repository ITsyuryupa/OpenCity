package tsurupa.opencity.model.utils;

public enum Status {

    verification(0),
    activ(1),
    archive(2),
    canceled(5);

    private final int value;
    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Status getStatusByValue(int value) {
        for (Status status : Status.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("No status found with value: " + value);
    }

}
