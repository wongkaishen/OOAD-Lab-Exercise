package hms.model;

/**
 * Abstract base for people stored in the hospital database.
 */
public abstract class Person {

    private String id;
    private String name;

    protected Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getSummary();
}
