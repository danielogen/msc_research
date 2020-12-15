/**
 * Mars Simulation Project
 * Landmark.java
 * @version 3.1.0 2017-10-03
 * @author Dalen Kruse
 */

package org.mars_sim.msp.core.mars;

import java.io.Serializable;

import org.mars_sim.msp.core.Coordinates;

/**
 * The Landmark class represents a surface landmark on virtual Mars. It contains
 * information related to the landmark.
 */
public class Landmark implements Serializable {

	/** default serial id. */
	private static final long serialVersionUID = 1L;

	// Data members
	/** Name of the landmark. */
	private String landmarkName;
	/** Landing location of the landmark. */
	private String landingLocation;
	/** Coordinates of the landmark. */
	private Coordinates coord;
	/** Diameter of the landmark, rounded to int. */
	private int landmarkDiameter;
	/** Origin of the designation. */
	private String landmarkOrigin;
	/** Type of feature, e.g. Mons (MO). */
	private String landmarkType;

	/**
	 * Constructs a landmark object with the given name at the given location.
	 * 
	 * @param name
	 * @param landingLocation
	 * @param coord
	 * @param diameter
	 * @param origin
	 * @param type
	 */
	public Landmark(String name, String landingLocation, Coordinates coord, int diameter, String origin, String type) {

		this.landmarkName = name;
		this.landingLocation = landingLocation;
		this.coord = coord;
		this.landmarkDiameter = diameter;
		this.landmarkOrigin = origin;
		this.landmarkType = type;

	}

	/**
	 * Sets the landmark name.
	 * 
	 * @param landmarkName name of the landmark
	 */
	public void setLandmarkName(String landmarkName) {
		this.landmarkName = landmarkName;
	}

	/**
	 * Sets the landmark landing location.
	 * 
	 * @param loc landing location of the landmark
	 */
	public void setLandmarkLocation(String loc) {
		this.landingLocation = loc;
	}
	
	/**
	 * Sets the landmark coordinates.
	 * 
	 * @param coord coordinates of the landmark
	 */
	public void setLandmarkLocation(Coordinates coord) {
		this.coord = coord;
	}

	/**
	 * Sets the landmark diameter.
	 * 
	 * @param coord diameter of the landmark
	 */
	public void setLandmarkDiameter(int diameter) {
		this.landmarkDiameter = diameter;
	}

	/**
	 * Sets the origin.
	 * 
	 * @param origin origin of feature name
	 */
	public void setLandmarkDiameter(String origin) {
		this.landmarkOrigin = origin;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type type of feature
	 */
	public void setLandmarkType(String type) {
		this.landmarkType = type;
	}

	/**
	 * Gets the landmark name.
	 * 
	 * @return name of the landmark
	 */
	public String getLandmarkName() {
		return landmarkName;
	}

	/**
	 * Gets the landmark landing location.
	 * 
	 * @return landing location of the landmark
	 */
	public String getLandmarkLandingLocation() {
		return landingLocation;
	}
	
	/**
	 * Gets the landmark coordinates.
	 * 
	 * @return coordinates of the landmark
	 */
	public Coordinates getLandmarkCoord() {
		return coord;
	}

	/**
	 * Gets the landmark diameter.
	 * 
	 * @return location of the landmark
	 */
	public int getLandmarkDiameter() {
		return landmarkDiameter;
	}

	/**
	 * Gets the origin.
	 * 
	 * @return origin of the landmark name
	 */
	public String getLandmarkOrigin() {
		return landmarkOrigin;
	}

	/**
	 * Gets the type.
	 * 
	 * @return type of the landmark
	 */
	public String getLandmarkType() {
		return landmarkType;
	}
}