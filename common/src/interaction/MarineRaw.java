package interaction;

import data.*;

import java.io.Serializable;
/**
 * Class for get Marines value.
 */
public class MarineRaw implements Serializable {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long health; //Значение поля должно быть больше 0
    private AstartesCategory category; //Поле не может быть null
    private Weapon weaponType; //Поле не может быть null
    private MeleeWeapon meleeWeapon; //Поле не может быть null
    private Chapter chapter; //Поле может быть null

    public MarineRaw(String name, Coordinates coordinates, long health,
                     AstartesCategory category, Weapon weaponType,
                     MeleeWeapon meleeWeapon, Chapter chapter) {
        this.name = name;
        this.coordinates = coordinates;
        this.health = health;
        this.category = category;
        this.weaponType = weaponType;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
    }
    /**
     * @return Id of Marine
     * */
    public Long getId(){
        return id;
    }
    /**
     * @return Name of the marine.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Coordinates of the marine.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return Health of the marine*/
    public long getHealth() {
        return health;
    }

    /**
     * @return Category of the marine.
     */
    public AstartesCategory getCategory() {
        return category;
    }


    /**
     * @return Weapon type of the marine.
     */
    public Weapon getWeaponType() {
        return weaponType;
    }

    /**
     * @return Melee weapon of the marine.
     */
    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    /**
     * @return Chapter of the marine.
     */
    public Chapter getChapter() {
        return chapter;
    }

    @Override
    public String toString() {
        String info = "";
        info += "Marine";
        info += "\n Name: " + name;
        info += "\n Coordinates: " + coordinates;
        info += "\n Health: " + health;
        info += "\n Category: " + category;
        info += "\n WeaponType: " + weaponType;
        info += "\n MeleeWeapon: " + meleeWeapon;
        info += "\n Chapter: " + chapter;
        return info;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + (int) health + category.hashCode() + weaponType.hashCode() +
                meleeWeapon.hashCode() + chapter.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof SpaceMarine){
            SpaceMarine marineObj = (SpaceMarine) obj;
            return name.equals(marineObj.getName()) && coordinates.equals(marineObj.getCoordinates()) &&
                    (health == marineObj.getHealth()) && (category == marineObj.getCategory()) &&
                    (weaponType == marineObj.getWeaponType()) && (meleeWeapon == marineObj.getMeleeWeapon()) &&
                    chapter.equals(marineObj.getChapter());
        }
        return false;
    }
}

