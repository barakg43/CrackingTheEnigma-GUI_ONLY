package dtoObjects.DmDTO;

public enum BruteForceLevel {
    easyLevel("Easy"),
    middleLevel("Middle"),
    hardLevel("Hard"),
    impossibleLevel("Impossible");
    final String name;
    BruteForceLevel(String easy) {
        name=easy;
    }

    @Override
    public String toString() {
        return name;
    }
}
