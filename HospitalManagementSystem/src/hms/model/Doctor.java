package hms.model;

/**
 * Doctor profile including medical specialization.
 */
public class Doctor extends Person {

    private String specialization;

    public Doctor(String id, String name, String specialization) {
        super(id, name);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String getSummary() {
        return getName() + " - " + specialization;
    }
}
