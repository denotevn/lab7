package data;

public enum  AstartesCategory {
    DREADNOUGHT,
    CHAPLAIN,
    APOTHECARY;
    public static String listCategory() {
        String nameList = "";
        for (AstartesCategory category : values()) {
            nameList += category.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);
    }
}

