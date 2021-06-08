package data;

public enum Weapon {
    COMBI_FLAMER,
    PLASMA_GUN,
    FLAMER,
    INFERNO_PISTOL,
    MISSILE_LAUNCHER;
    public static String outWeapon() {
        String nameList = "";
        for (Weapon weapon : values()) {
            nameList += weapon.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
