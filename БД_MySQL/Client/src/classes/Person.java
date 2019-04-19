package classes;

import java.io.Serializable;

/**
 * This is <b>auxiliary</b> class.
 * Objects of this class comes as parametres
 * @author Dashchynski Nikita
 * @version 1.0
 * @see Serializable
 */
public class Person implements Serializable{
    private String name;
    private String adress;
    private String city;

    Person(){}

    Person(String name, String adress, String city) {
        this.name = name;
        this.adress = adress;
        this.city = city;
    }

    /**
     * Get the name of a person
     * @return <b>name</b> of a person
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name to a person
     * @param name set the <b>name</b> for person
     */
    public void setname(String name) {
        this.name = name;
    }

    /**
     * Get the adress of a person
     * @return <b>adress</b> of a person
     */
    public String getAdress() {
        return adress;
    }

    /**
     * Set the adress to a person
     * @param adress set the <b>adress</b> for person
     */
    public void setAdress(String adress) {
        this.adress = adress;
    }

    /**
     * Get the city of a person
     * @return <b>city</b> where person live
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the city to a person
     * @param city set the <b>city</b> for person
     */
    public void setCity(String city) {
        this.city = city;
    }
}