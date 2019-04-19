package interfaces;

import classes.Person;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This is the <code>interface</code> with the main functions
 * @author Dashchynski Nikita
 * @version 1.0
 * @see Remote
 */
public interface WorkersInterface extends Remote {

    /**
     * This method add person to the datebase
     * @param person to add to the datebase
     * @return <b>true</b> if person added and <b>false</b> otherwise
     * @throws RemoteException {@inheritDoc}
     */
    boolean add(Person person) throws RemoteException;

    /**
     * This method delete person from the datebase according to his data
     * @param name name of person which need to be removed
     * @param adress where he lives
     * @param city where he lives
     * @return <b>true</b> if person deleted and <b>false</b> otherwise
     * @throws RemoteException {@inheritDoc}
     */
    boolean delete(String name, String adress, String city) throws RemoteException;

    /**
     * This method <b>shows</b> people who are in datebase
     * @return list of {@link Person} from datebase
     * @throws RemoteException {@inheritDoc}
     */
    ArrayList<Person> show() throws RemoteException;

    /**
     * This method shows how many people live in each city in datebase
     * @return {@link String} with information
     * @throws RemoteException {@inheritDoc}
     */
    String countPeople() throws RemoteException;
}