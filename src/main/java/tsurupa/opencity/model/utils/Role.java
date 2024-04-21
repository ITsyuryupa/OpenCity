package tsurupa.opencity.model.utils;

public enum Role {

    user(0), moderator(1), admin(2);

    private final int value;
    private Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
