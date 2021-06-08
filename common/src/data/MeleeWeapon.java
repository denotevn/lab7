package data;

public enum MeleeWeapon {
    CHAIN_SWORD,
    POWER_SWORD,
    CHAIN_AXE,
    LIGHTING_CLAW;
    public static String listMeleeWeapon() {
        String nameList = "";
        for (MeleeWeapon weapon : values()) {
            nameList += weapon.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
