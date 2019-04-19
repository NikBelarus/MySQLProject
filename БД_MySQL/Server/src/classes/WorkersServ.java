package classes;

import interfaces.WorkersInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

/**
 * This class makes <b>connection</b> to the database
 * and realize methods from the {@link WorkersInterface}
 * @author Dashchynski Nikita
 * @version 1.0
 * @see UnicastRemoteObject
 */
public class WorkersServ extends UnicastRemoteObject implements WorkersInterface {
    private Connection connection;
    private Statement statement;

    private static final String SELECT_FROM_CITIES = "SELECT * from cities WHERE adress = ?";
    private static final String ADD_TO_CITIES = "insert into cities (adress, city) VALUES(?, ?)";
    private static final String SEL_IDS_CITIES = "SELECT cities_id FROM cities WHERE adress = ?";
    private static final String ADD_TO_PEOPLE = "insert into people (name, cities_id) VALUES(?, ?)";
    private static final String SELECT_ALL_PEOPLE = "SELECT * FROM people";
    private static final String SELECT_FROM_CITIES_ID = "SELECT * FROM cities WHERE cities_id = ?";
    private static final String DROP_VIEW = "DROP VIEW cityView";
    private static final String CREATE_VIEW = "CREATE VIEW cityView AS SELECT people.people_id, cities.city FROM people INNER JOIN cities ON people.cities_id = cities.cities_id";
    private static final String COUNT_POP = "SELECT COUNT(people_id), city FROM cityView GROUP BY city";
    private static final String SELLECT_ALL_CITIES = "SELECT * FROM cities WHERE adress = ? AND city = ?";
    private static final String DELETE_FROM_PEOPLE = "DELETE FROM people WHERE name = ? AND cities_id = ?";
    private static final String SELECT_FROM_PEOPLE = "SELECT * FROM people WHERE cities_id = ?";
    private static final String DELETE_FROM_CITIES = "DELETE FROM cities WHERE cities_id = ?";

    WorkersServ() throws RemoteException{
        String userName = "root";
        String userPassword = "root";
        String connectionURL = "jdbc:mysql://localhost:3306/workers?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connectionURL, userName, userPassword);
            statement = connection.createStatement();
        } catch (Exception e){
            System.out.println("Соединение с БД не установлено");
        }
    }

    /**
     * {@link WorkersInterface#add(Person)}
     * @param person to add to the datebase
     * @return <b>true</b> if person added and <b>false</b> otherwise
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public boolean add(Person person) throws RemoteException {
        try {
            PreparedStatement ps1 = connection.prepareStatement(SELECT_FROM_CITIES);
            ps1.setString(1, person.getAdress());
            try (ResultSet rs1 = ps1.executeQuery()) {
                if (!rs1.next()) {
                    //если не существует такого города в БД. Значит добавим.
                    PreparedStatement ps2 = connection.prepareStatement(ADD_TO_CITIES);
                    ps2.setString(1, person.getAdress());
                    ps2.setString(2, person.getCity());
                    ps2.executeUpdate();
                }
            }

            PreparedStatement preparedStatement = connection.prepareStatement(SEL_IDS_CITIES);
            preparedStatement.setString(1, person.getAdress());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    PreparedStatement ps3 = connection.prepareStatement(ADD_TO_PEOPLE);
                    ps3.setString(1, person.getName());
                    ps3.setInt(2, resultSet.getInt(1));
                    ps3.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    /**
     * {@link WorkersInterface#delete(String, String, String)}
     * @param name name of person which need to be removed
     * @param adress where he lives
     * @param city where he lives
     * @return <b>true</b> if person deleted and <b>false</b> otherwise
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public boolean delete(String name, String adress, String city) throws RemoteException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELLECT_ALL_CITIES);
            preparedStatement.setString(1, adress);
            preparedStatement.setString(2, city);

            int num = -1;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    num = resultSet.getInt(1);
                }
            }

            PreparedStatement preparedStatement1 = connection.prepareStatement(DELETE_FROM_PEOPLE);
            preparedStatement1.setString(1, name);
            preparedStatement1.setInt(2, num);
            preparedStatement1.executeUpdate();

            PreparedStatement preparedStatement2 = connection.prepareStatement(SELECT_FROM_PEOPLE);
            preparedStatement2.setInt(1, num);
            try (ResultSet resultSet = preparedStatement2.executeQuery()) {
                if (!resultSet.next()) {
                    PreparedStatement preparedStatement3 = connection.prepareStatement(DELETE_FROM_CITIES);
                    preparedStatement3.setInt(1, num);
                    preparedStatement3.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    /**
     * {@link WorkersInterface#show()}
     * @return list of {@link Person} from datebase
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public ArrayList<Person> show() throws RemoteException {
        try {
            ArrayList<Person> peopleHelps = new ArrayList<>();
            try (ResultSet rs6 = statement.executeQuery(SELECT_ALL_PEOPLE)) {
                while (rs6.next()) {
                    PreparedStatement preparedStatement2 = connection.prepareStatement(SELECT_FROM_CITIES_ID);
                    preparedStatement2.setInt(1, rs6.getInt(3));
                    try (ResultSet resultSet = preparedStatement2.executeQuery()) {
                        if (resultSet.next()) {
                            peopleHelps.add(new Person(rs6.getString(2), resultSet.getString(2), resultSet.getString(3)));
                        }
                    }
                }
            }
            return peopleHelps;
        } catch (SQLException e){
            return null;
        }
    }

    /**
     * {@link WorkersInterface#countPeople()}
     * @return {@link String} with information
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public String countPeople() throws RemoteException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            statement.executeUpdate(DROP_VIEW);
            statement.executeUpdate(CREATE_VIEW);
            PreparedStatement ps9 = connection.prepareStatement(COUNT_POP);
            try (ResultSet rs5 = ps9.executeQuery()) {
                while (rs5.next()) {
                    stringBuilder.append("Город: " + rs5.getString(2) + "; численность: " + rs5.getInt(1) + "\n");
                }
            }
            return stringBuilder.toString();
        } catch (SQLException e){
            return null;
        }
    }
}
