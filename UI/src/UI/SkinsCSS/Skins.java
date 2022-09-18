package UI.SkinsCSS;

public enum Skins {
    Regular("Regular Mode"),
    DarkMode("Dark Mode"),
    LovelyPink("Pink Mode"),
    Basketball("Basketball Mode");
    final String name;

    Skins(String skinName) {
        this.name=skinName;
    }

    @Override
    public String toString() {
        return name;
    }
}
