package commands;

import interaction.User;

public abstract class AbstractCommand implements Commands{
    String name;
    String description;
    private String usage;
    public AbstractCommand(String name,String description,String usage){
        this.name = name;
        this.description = description;
        this.usage = usage;
    }

    /**
     * @return Name of the command.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return Usage of the command.
     */
    @Override
    public String getUsage() {
        return usage;
    }

    /**
     * @return Description of the command.
     */
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " " + usage + " (" + description + ")";
    }

    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AbstractCommand other = (AbstractCommand) obj;
        return name.equals(other.getName()) && usage.equals(other.getUsage()) &&
                description.equals(other.getDescription());
    }
}
