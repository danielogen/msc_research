/**
 * Mars Simulation Project
 * GroundVehicle.java
 * @version 3.1.0 2017-08-08
 * @author Scott Davis
 */
package org.mars_sim.msp.core.vehicle;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mars_sim.msp.core.Direction;
import org.mars_sim.msp.core.Inventory;
import org.mars_sim.msp.core.LocalAreaUtil;
import org.mars_sim.msp.core.LogConsolidated;
import org.mars_sim.msp.core.mars.TerrainElevation;
import org.mars_sim.msp.core.structure.Settlement;
import org.mars_sim.msp.core.structure.building.Building;
import org.mars_sim.msp.core.structure.building.BuildingManager;
import org.mars_sim.msp.core.structure.building.function.FunctionType;
import org.mars_sim.msp.core.tool.RandomUtil;

/**
 * The GroundVehicle class represents a ground-type vehicle. It is abstract and
 * should be extended to a particular type of ground vehicle.
 */
public abstract class GroundVehicle extends Vehicle implements Serializable {

	/** default serial id. */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(GroundVehicle.class.getName());
	private static final String loggerName = logger.getName();
	private static final String sourceName = loggerName.substring(loggerName.lastIndexOf(".") + 1, loggerName.length());
	
	public static final String LANDER_HAB = "Lander Hab";
	public static final String OUTPOST_HUB = "Outpost Hub";
			
	// public final static String STUCK = "Stuck - using winch";

	/** Comparison to indicate a small but non-zero amount of fuel (methane) in kg that can still work on the fuel cell to propel the engine. */
    public static final double LEAST_AMOUNT = .001D;
    
	// Data members
	/** Current elevation in km. */
	private double elevation;
	/** Ground vehicle's basic terrain handling capability. */
	private double terrainHandlingCapability;
	/** True if vehicle is stuck. */
	private boolean isStuck;

//	private static TerrainElevation terrain;

	/**
	 * Constructs a {@link GroundVehicle} object at a given settlement.
	 * 
	 * @param name                name of the ground vehicle
	 * @param description         the configuration description of the vehicle.
	 * @param settlement          settlement the ground vehicle is parked at
	 * @param maintenanceWorkTime the work time required for maintenance (millisols)
	 */
	public GroundVehicle(String name, String description, Settlement settlement, double maintenanceWorkTime) {
		// use Vehicle constructor
		super(name, description, settlement, maintenanceWorkTime);

		// Add scope to malfunction manager.
//		malfunctionManager.addScopeString(SystemType.VEHICLE.getName());// "GroundVehicle");

		setTerrainHandlingCapability(0D); // Default terrain capability
	}

//	/**
//	 * Returns vehicle's current status
//	 * 
//	 * @return the vehicle's current status
//	 */
//	public StatusType getStatus() {
//		StatusType status = null;
//
//		if (isStuck)
//			super.addStatusType(StatusType.STUCK);
//
//		return status;
//	}

	/**
	 * Returns the elevation of the vehicle in km.
	 * 
	 * @return elevation of the ground vehicle (in km)
	 */
	public double getElevation() {
		return elevation;
	}

	/**
	 * Sets the elevation of the vehicle (in km.)
	 * 
	 * @param elevation new elevation for ground vehicle
	 */
	public void setElevation(double elevation) {
		this.elevation = elevation;
	}

	/**
	 * Returns the vehicle's terrain capability
	 * 
	 * @return terrain handling capability of the ground vehicle
	 */
	public double getTerrainHandlingCapability() {
		return terrainHandlingCapability;
	}

	/**
	 * Sets the vehicle's terrain capability
	 * 
	 * @param c sets the ground vehicle's terrain handling capability
	 */
	public void setTerrainHandlingCapability(double c) {
		terrainHandlingCapability = c;
	}

	/**
	 * Gets the average angle of terrain over next 7.4km distance in direction
	 * vehicle is traveling.
	 * 
	 * @return ground vehicle's current terrain grade angle from horizontal
	 *         (radians)
	 */
	public double getTerrainGrade() {
		return getTerrainGrade(getDirection());
	}

	/**
	 * Gets the average angle of terrain over next 7.4km distance in a given
	 * direction from the vehicle.
	 * 
	 * @return ground vehicle's current terrain grade angle from horizontal
	 *         (radians)
	 */
	public double getTerrainGrade(Direction direction) {
		// Determine the terrain grade in a given direction from the vehicle.
		if (terrainElevation == null)
			terrainElevation = surfaceFeatures.getTerrainElevation();
		return terrainElevation.determineTerrainSteepness(getCoordinates(), direction);
	}

	/**
	 * Returns true if ground vehicle is stuck
	 * 
	 * @return true if vehicle is currently stuck, false otherwise
	 */
	public boolean isStuck() {
		return isStuck;
	}

	/**
	 * Sets the ground vehicle's stuck value
	 * 
	 * @param stuck true if vehicle is currently stuck, false otherwise
	 */
	public void setStuck(boolean stuck) {
		isStuck = stuck;
		if (isStuck) {
			addStatus(StatusType.STUCK);
			setSpeed(0D);
			setParkedLocation(0D, 0D, getDirection().getDirection());
		}
	}

//	/**
//	 * Gets the driver of the ground vehicle.
//	 * 
//	 * @return the vehicle driver.
//	 */
//	public VehicleOperator getDriver() {
//		return getOperator();
//	}
//
//	/**
//	 * Sets the driver of the ground vehicle.
//	 * 
//	 * @param operator the driver
//	 */
//	public void setDriver(VehicleOperator operator) {
//		setOperator(operator);
//	}

	/**
	 * Find a new location and facing if a rover overlaps with a building
	 */
	@Override
	public void determinedSettlementParkedLocationAndFacing() {

		Settlement settlement = getSettlement();
		if (settlement == null) {
			// throw new IllegalStateException("Vehicle not parked at a settlement");
			logger.warning(this.getName() + " no longer parks at a settlement.");
		}

		else {
			double centerXLoc = 0D;
			double centerYLoc = 0D;

			// Place the vehicle starting from the settlement center (0,0).

			int oX = 10;
			int oY = 0;

			int weight = 2;

			long numHab = settlement.getBuildingManager().getNumBuildingsOfSameType(LANDER_HAB);
			long numHub = settlement.getBuildingManager().getNumBuildingsOfSameType(OUTPOST_HUB);
			int numGarages = settlement.getBuildingManager().getBuildings(FunctionType.GROUND_VEHICLE_MAINTENANCE)
					.size();
			int total = (int)(numHab + numHub + numGarages * weight - 1);
			if (total < 0)
				total = 0;
			int rand = RandomUtil.getRandomInt(total);

			if (rand != 0) {

				if (rand < numHab + numHub) {
					int r0 = RandomUtil.getRandomInt((int)numHab - 1);
					Building hab = settlement.getBuildingManager().getBuildingsOfSameType(LANDER_HAB).get(r0);
					int r1 = 0;
					Building hub = null;
					if (numHub > 0) {
						r1 = RandomUtil.getRandomInt((int)numHub - 1);
						hub = settlement.getBuildingManager().getBuildingsOfSameType(OUTPOST_HUB).get(r1);
					}

					if (hab != null) {
						centerXLoc = (int) hab.getXLocation();
						centerYLoc = (int) hab.getYLocation();
					} else if (hub != null) {
						centerXLoc = (int) hub.getXLocation();
						centerYLoc = (int) hub.getYLocation();
					}
				}

				else {
					Building garage = BuildingManager.getAGarage(getSettlement());
					centerXLoc = (int) garage.getXLocation();
					centerYLoc = (int) garage.getYLocation();
				}
			}

			double newXLoc = 0D;
			double newYLoc = 0D;

			double newFacing = 0D;

			double step = 10D;
			boolean foundGoodLocation = false;

			// Try iteratively outward from 10m to 500m distance range.
			for (int x = oX; (x < 500) && !foundGoodLocation; x += step) {
				// Try ten random locations at each distance range.
				for (int y = oY; (y < step) && !foundGoodLocation; y++) {
					double distance = RandomUtil.getRandomDouble(step) + x;
					double radianDirection = RandomUtil.getRandomDouble(Math.PI * 2D);
					newXLoc = centerXLoc - (distance * Math.sin(radianDirection));
					newYLoc = centerYLoc + (distance * Math.cos(radianDirection));
					newFacing = RandomUtil.getRandomDouble(360D);

					// Check if new vehicle location collides with anything.
					foundGoodLocation = LocalAreaUtil.isGoodLocation(this, newXLoc, newYLoc, newFacing,
							getCoordinates());
				}
			}

			setParkedLocation(newXLoc, newYLoc, newFacing);

		}
	}

	/**
	 * Checks if the vehicle has enough amount of fuel as prescribed
	 * 
	 * @param fuelConsumed
	 * @return
	 */
	protected boolean hasEnoughFuel(double fuelConsumed) {
		Vehicle v = getVehicle();
	    Inventory vInv = v.getInventory();
        int fuelType = v.getFuelType();
        
    	try {
    		double remainingFuel = vInv.getAmountResourceStored(fuelType, false);
//		    	vInv.retrieveAmountResource(fuelType, fuelConsumed);
    		
    		if (remainingFuel < LEAST_AMOUNT) {
    			v.addStatus(StatusType.OUT_OF_FUEL);
    			return false;
    		}
    			
    		if (fuelConsumed > remainingFuel) {
            	fuelConsumed = remainingFuel;
            	return false;
    		}
    		else
    			return true;
	    }
	    catch (Exception e) {
	    	LogConsolidated.log(Level.SEVERE, 0, sourceName, "[" + v.getName() + "] " 
					+ "can't retrieve methane. Cannot drive.");
	    	return false;
	    }
	}
	
	/**
	 * Prepare object for garbage collection.
	 */
	public void destroy() {
//		surface = null;
//		terrain = null;
	}
}