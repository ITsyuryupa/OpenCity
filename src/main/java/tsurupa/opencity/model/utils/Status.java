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

}
