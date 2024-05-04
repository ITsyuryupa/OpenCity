package tsurupa.opencity.model.utils;





public enum Tag {

    танцы(0), вокал(1), музыка(2), настолки(3), фестиваль(4), спорт(5),
    интелектуальное_соревнование(6), культура(7),
    другое(100);

    private final int value;
    private Tag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Tag fromValue(int value) {
        for (Tag tag : values()) {
            if (tag.getValue() == value) {
                return tag;
            }
        }
        return null;
    }
}

