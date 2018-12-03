package by.epam.courses.homeTask3.service;

import by.epam.courses.homeTask3.domain.Dock;
import by.epam.courses.homeTask3.domain.Ship;
import by.epam.courses.homeTask3.domain.exceptions.ShipException;
import org.apache.log4j.Logger;

import java.util.Random;

public class ShipService {
    private static Logger logger = Logger.getLogger(ShipService.class);


    public void loadToPort(Dock dock, Ship ship) throws ShipException {

        int containersNumberToMove = containersCount(ship.getContainers().size(),ship);//ship.getContainers().size();
        boolean result=false;

        logger.info("The ship " + ship.getShipId() + " is going to upload " + containersNumberToMove + " containers to port storage.");
        if (!ship.getContainers().isEmpty())
            result = dock.addContainerToPort(ship, containersNumberToMove);

        if (result) {
            logger.info("Port received " + containersNumberToMove + " containers from ship " + ship.getShipId() + ", port size: " + dock.getPortContainers().size());
        } else {
            logger.warn("Port haven't place to load containers from ship" + ship.getShipId() + " or this ship haven't any containers");
        }
    }

    public void loadFromPort(Dock dock,Ship ship) throws ShipException {

        int containersNumberToMove = containersCount(ship.getFreeSpace(),ship);//ship.getFreeSpace();

        boolean result;

        logger.info("The ship " + ship.getShipId() + " want to load " + containersNumberToMove + " containers from port storage.");

        result = dock.loadContainersFromPort(ship, containersNumberToMove);

        if (result) {
            logger.info("The ship " + ship.getShipId() + " loaded " + containersNumberToMove + " containers from port, port size: " + dock.getPortContainers().size());
        } else {
            logger.warn("Port haven't containers to load ship " + ship.getShipId() + " or this ship is maximally loaded");
        }

    }

    public void loadToOtherShip(Dock dock,Ship ship) throws ShipException {
        boolean result;
        int containersNumberToMove = containersCount(ship.getContainers().size(),ship);
        logger.info("Ship " + ship.getShipId() + " want to upload " + containersNumberToMove + " on other dock.");
        result = dock.addContainerToOtherShip(ship,containersNumberToMove);

        if (!result) {
            logger.info("The ship " + ship.getShipId() + " is going to upload " + containersNumberToMove + " containers to port storage.");
            if (dock.addContainerToPort(ship,containersNumberToMove)) {
                logger.info("Port storage  load containers from ship " + ship.getShipId());
            } else {
                logger.warn("Port haven't place to load containers from ship" + ship.getShipId());
            }
        }

    }

    public void loadFromOtherShip(Dock dock,Ship ship) throws ShipException {
        int containersNumberToMove = containersCount(ship.getFreeSpace(),ship);
        logger.info("Ship " + ship.getShipId() + " want to load " + containersNumberToMove + " from other ship.");

        if (!dock.loadContainerFromOtherShip(ship, containersNumberToMove)) {
            logger.info("Ship " + ship.getShipId() + " want to load " + containersNumberToMove + " from port.");
            if (!dock.loadContainersFromPort(ship, containersNumberToMove)) {
                logger.info("Port received containers to ship " + ship.getShipId());
            } else {
                logger.warn("Port haven't containers to load ship " + ship.getShipId());
            }
        }
    }

    private int containersCount(int freeSpaceSize,Ship ship) throws ShipException {
        if (freeSpaceSize > 0) {
            Random random = new Random();
            int result = random.nextInt(freeSpaceSize);
            if (result == 0) {
                result++;
            }
            logger.info(result + " containers will be used");
            return result;
        } else {
            throw new ShipException("Invalid action in ship " + ship.getShipId());
        }

    }
}
