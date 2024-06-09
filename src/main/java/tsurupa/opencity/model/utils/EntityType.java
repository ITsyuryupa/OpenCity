package tsurupa.opencity.model.utils;



public enum EntityType {

    event(0), community(1);

    private final int value;
    private EntityType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EntityType fromValue(int value) {
        for (EntityType entityType : values()) {
            if (entityType.getValue() == value) {
                return entityType;
            }
        }
        return null;
    }
}
