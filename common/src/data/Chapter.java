package data;

import java.io.Serializable;

public class Chapter implements Serializable {
    private String name;
    private String parentLegion;

    public Chapter(String nameAsker, String legionAsker) {
        this.name = nameAsker;
        this.parentLegion = legionAsker;
    }

    public void setChapter(String nameAsker, String legionAsker) {
        this.name = nameAsker;
        this.parentLegion = legionAsker;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public String getParentLegion() {
        return parentLegion;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "name='" + name + '\'' +
                ", parentLegion='" + parentLegion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Chapter) {
            Chapter chapterObj = (Chapter) obj;
            return name.equals(chapterObj.getName()) && (parentLegion == chapterObj.getParentLegion());
        }
        return false;
    }
}
