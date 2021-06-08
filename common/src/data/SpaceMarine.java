package data;

import interaction.User;

import java.util.Date;

public class SpaceMarine implements Comparable<SpaceMarine> {

    private long id;
    private String name;
    private Coordinates coordinates;
    private java.util.Date creationDate;
    private long health;
    private AstartesCategory category;
    private Weapon weaponType;
    private MeleeWeapon meleeWeapon;
    private Chapter chapter;
    private User owner;

    public SpaceMarine(long id, String name, Coordinates coordinates, java.util.Date creationDate, long health,
                       AstartesCategory category, Weapon weaponType, MeleeWeapon meleeWeapon, Chapter chapter,
                       User owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.category = category;
        this.weaponType = weaponType;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
        this.owner = owner;
    }
    /**
     * @return id of the marine
     * */
    public long getId() {
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
     * @return Creation date of the marine.
     */
    public Date getCreationDate() {
        return creationDate;
    }
    /**
     * @return Creation date of the marine.
     */
    public Long getHealth() {
        return health;
    }


    public AstartesCategory getCategory() {
        return category;
    }
    /**
     * @return Category of the marine.
     */
    public Weapon getWeaponType() {
        return weaponType;
    }
    /**
     * @return Weapon type of the marine.
     */
    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }
    /**
     * @return Melee weapon of the marine.
     */
    public void setMeleeWeapon(MeleeWeapon meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }
    /**
     * @return Chapter of the marine.
     */
    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(String name, String parentLegion) {
        Chapter chapter = new Chapter(name,parentLegion);
    }

    @Override
    public int compareTo(SpaceMarine marine) {
        if(this.getId() > marine.getId()) return -1;
        return 1;
    }

    /**  public SpaceMarine(Long  ID, String name,Coordinates coordinates1,java.util.Date date1,
     Long health1, AstartesCategory astartesCategory,Weapon weapon,
     MeleeWeapon meleeWeapon1, Chapter chapter1) {
     this.id = ID;
     this.name = name;
     this.coordinates = coordinates1;
     this.creationDate = date1;
     this.health = health1;
     this.category = astartesCategory;
     this.weaponType = weapon;
     this.meleeWeapon = meleeWeapon1;
     this.chapter = chapter1;
     }
     */

    @Override
    public String toString() {
        return "SpaceMarine{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", health=" + health +
                ", category=" + category +
                ", weaponType=" + weaponType +
                ", meleeWeapon=" + meleeWeapon +
                ", chapter=" + chapter+
                 ",User = "+ owner               +
                '}';
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
            return name.equals(((SpaceMarine) obj).getName());
        }
        return false;
    }
    public User getOwner() {
        return owner;
    }

    public void setHealth(long health) {
        this.health = health;
    }


}