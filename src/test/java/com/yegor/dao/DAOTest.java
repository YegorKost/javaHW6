package com.yegor.dao;

import com.yegor.entities.Car;
import com.yegor.entities.Mechanic;
import com.yegor.entities.ServiceStation;
import com.yegor.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.*;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Created by YegorKost on 27.02.2017.
 */
public class DAOTest {

    private static Car car1, car2;
    private static ServiceStation station1, station2;
    private static Mechanic mechanic1, mechanic2;
    private CarDAO carDAO = new CarDAO();
    private ServiceStationDAO stationDAO = new ServiceStationDAO();
    private MechanicDAO mechanicDAO = new MechanicDAO();

    static {
        car1 = new Car();
        car1.setId(1);
        car1.setMake("Audi");
        car1.setModel("A7");
        car1.setPrice(70000d);
        car1.setDate(LocalDate.of(2017, 3, 1));

        car2 = new Car();
        car2.setId(2);
        car2.setMake("BMW");
        car2.setModel("M5");
        car2.setPrice(65000d);
        car2.setDate(LocalDate.of(2017, 1, 17));

        station1 = new ServiceStation();
        station1.setId(1);
        station1.setAddress("Poltava");

        station2 = new ServiceStation();
        station2.setId(2);
        station2.setAddress("Kiev");

        mechanic1 = new Mechanic();
        mechanic1.setId(1);
        mechanic1.setName("Bob");
        mechanic1.setSurname("Dilan");
        mechanic1.setServiceStation(station1);

        mechanic2 = new Mechanic();
        mechanic2.setId(2);
        mechanic2.setName("Dik");
        mechanic2.setSurname("Roy");
        mechanic2.setServiceStation(station2);
    }

    @Before
    public void setTestData() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.getTransaction().begin();
            String queryS = "INSERT INTO service_station (address) VALUES (:a1), (:a2); " +
                    "INSERT INTO car (make, model, price, date) VALUES (:mk1, :m1, :p1, :d1), (:mk2, :m2, :p2, :d2); " +
                    "INSERT INTO mechanic (name, surname, service_station_id) VALUES (:n1, :s1, 1), (:n2, :s2, 2); " +
                    "INSERT INTO car_service_station (car_id, service_station_id) VALUES (1, 1), (2, 1);";

            NativeQuery query = session.createNativeQuery(queryS);
            query.setParameter("a1", station1.getAddress());
            query.setParameter("a2", station2.getAddress());
            query.setParameter("mk1", car1.getMake());
            query.setParameter("m1", car1.getModel());
            query.setParameter("p1", car1.getPrice());
            query.setParameter("d1", car1.getDate());
            query.setParameter("mk2", car2.getMake());
            query.setParameter("m2", car2.getModel());
            query.setParameter("p2", car2.getPrice());
            query.setParameter("d2", car2.getDate());
            query.setParameter("n1", mechanic1.getName());
            query.setParameter("s1", mechanic1.getSurname());
            query.setParameter("n2", mechanic2.getName());
            query.setParameter("s2", mechanic2.getSurname());

            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            rollbackTransaction(session);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

    }

    @After
    public void cleanTestData() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.getTransaction().begin();
            NativeQuery query = session.createNativeQuery(
                    "DELETE FROM car_service_station; " +
                            "DELETE FROM mechanic; " +
                            "DELETE FROM car; " +
                            "DELETE FROM service_station; " +
                            "ALTER SEQUENCE car_car_id_seq RESTART WITH 1; " +
                            "ALTER SEQUENCE mechanic_mechanic_id_seq RESTART WITH 1; " +
                            "ALTER SEQUENCE service_station_service_station_id_seq RESTART WITH 1;");
            query.executeUpdate();
            session.getTransaction().commit();
        }catch (Exception e) {
            rollbackTransaction(session);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @AfterClass
    public static void shutdownSF() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.getTransaction().begin();
            String dropQuery = "DROP TABLE IF EXISTS car_service_station;\n" +
                    "DROP TABLE IF EXISTS mechanic;\n" +
                    "DROP TABLE IF EXISTS car;\n" +
                    "DROP TABLE IF EXISTS service_station;\n";
            NativeQuery query = session.createNativeQuery(dropQuery);
            query.executeUpdate();
            session.getTransaction().commit();
        }catch (Exception e) {
            rollbackTransaction(session);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            if (sessionFactory != null && sessionFactory.isOpen()) {
                sessionFactory.close();
            }
        }
    }

    @Test
    public void addCarTest() throws Exception {
        Car carNew = new Car();
        carNew.setModel("Bugatti");
        carDAO.add(carNew);
        assertEquals(carNew, carDAO.getById(3));
    }

    @Test
    public void updateCarTest() throws Exception {
        Car carForUpdate = carDAO.getById(1);
        carForUpdate.setPrice(90000d);
        Double price = car1.getPrice();
        carDAO.update(carForUpdate);
        car1.setPrice(90000d);
        assertEquals(car1, carDAO.getById(1));
        car1.setPrice(price);
    }

    @Test
    public void getCarByIdTest() throws Exception {
        Car car = carDAO.getById(1);
        assertEquals(car1, car);
        assertTrue(car.getServiceStations().contains(station1));
    }

    @Test
    public void deleteCarTest() throws Exception {
        Car car = carDAO.getById(1);
        assertEquals(car1, car);
        carDAO.delete(car);
        assertNotEquals(car1, carDAO.getById(1));
    }

    @Test
    public void addMechanicTest() throws Exception {
        Mechanic mechanic = new Mechanic();
        mechanic.setName("Nikola");
        mechanic.setServiceStation(station1);
        mechanicDAO.add(mechanic);
        assertEquals(mechanic, mechanicDAO.getById(3));
    }

    @Test
    public void updateMechanicTest() throws Exception {
        Mechanic mechanicForUpdate = mechanicDAO.getById(1);
        mechanicForUpdate.setName("Update");
        mechanicDAO.update(mechanicForUpdate);
        String name = mechanic1.getName();
        mechanic1.setName("Update");
        assertEquals(mechanic1, mechanicDAO.getById(1));
        mechanic1.setName(name);
    }

    @Test
    public void getMechanicByIdTest() throws Exception {
       Mechanic mechanic = mechanicDAO.getById(1);
        assertEquals(mechanic1, mechanic);
        assertEquals(mechanic1.getServiceStation(), station1);
    }

    @Test
    public void deleteMechanicTest() throws Exception {
        Mechanic mechanic = mechanicDAO.getById(2);
        assertEquals(mechanic2, mechanic);
        mechanicDAO.delete(mechanic);
        assertNotEquals(mechanic2, mechanicDAO.getById(2));
    }

    @Test
    public void addStationTest() throws Exception {
        ServiceStation station = new ServiceStation();
        station.setAddress("New York");
        stationDAO.add(station);
        assertEquals(station, stationDAO.getById(3));
    }

    @Test
    public void updateStationTest() throws Exception {
        ServiceStation stationForUpdate = stationDAO.getById(1);
        stationForUpdate.setAddress("Poltava-1");
        stationDAO.update(stationForUpdate);
        String address = station1.getAddress();
        station1.setAddress("Poltava-1");
        assertEquals(station1, stationDAO.getById(1));
        station1.setAddress(address);
    }

    @Test
    public void getStationByIdTest() throws Exception {
        ServiceStation station = stationDAO.getById(1);
        assertEquals("Poltava", station.getAddress());
        assertTrue(station.getCars().contains(car1));
        assertTrue(station.getCars().contains(car2));
        assertTrue(station.getMechanics().contains(mechanic1));
    }

    @Test
    public void deleteStationTest() throws Exception {
        ServiceStation station = stationDAO.getById(1);
        assertEquals(station1, station);
        stationDAO.delete(station);
        assertNotEquals(station1, stationDAO.getById(1));
    }

    private static void rollbackTransaction(Session session) {
        if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
            session.getTransaction().rollback();
        }
    }
}