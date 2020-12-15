/**
 * Mars Simulation Project
 * AreologyFieldStudy.java
 * @version 3.1.0 2017-10-14
 * @author Scott Davis
 */
package org.mars_sim.msp.core.person.ai.mission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.mars_sim.msp.core.Coordinates;
import org.mars_sim.msp.core.Direction;
import org.mars_sim.msp.core.Inventory;
import org.mars_sim.msp.core.Msg;
import org.mars_sim.msp.core.person.Person;
import org.mars_sim.msp.core.person.PhysicalCondition;
import org.mars_sim.msp.core.person.ai.task.AreologyStudyFieldWork;
import org.mars_sim.msp.core.person.ai.task.utils.Task;
import org.mars_sim.msp.core.resource.ResourceUtil;
import org.mars_sim.msp.core.science.ScienceType;
import org.mars_sim.msp.core.science.ScientificStudy;
import org.mars_sim.msp.core.structure.Settlement;
import org.mars_sim.msp.core.time.MarsClock;
import org.mars_sim.msp.core.tool.RandomUtil;
import org.mars_sim.msp.core.vehicle.Rover;
import org.mars_sim.msp.core.vehicle.Vehicle;

/**
 * A mission to do areology research at a remote field location for a scientific
 * study. TODO externalize strings
 */
public class AreologyFieldStudy extends RoverMission implements Serializable {

	/** default serial id. */
	private static final long serialVersionUID = 1L;

	/** default logger. */
	private static final Logger logger = Logger.getLogger(AreologyFieldStudy.class.getName());
	private static final String loggerName = logger.getName();
	private static final String sourceName = loggerName.substring(loggerName.lastIndexOf(".") + 1, loggerName.length());
	
	/** Default description. */
	public static final String DEFAULT_DESCRIPTION = Msg.getString("Mission.description.areologyFieldStudy"); //$NON-NLS-1$

	/** Mission Type enum. */
	public static final MissionType missionType = MissionType.AREOLOGY;
	
	/** Mission phase. */
	public static final MissionPhase RESEARCH_SITE = new MissionPhase(
			Msg.getString("Mission.phase.researchingFieldSite")); //$NON-NLS-1$

	/** Minimum number of people to do mission. */
	public static final int MIN_PEOPLE = 2;

	/** Amount of time to field a site. */
	public static final double FIELD_SITE_TIME = 1000D;

	// Data members
	/** The start time at the field site. */
	private MarsClock fieldSiteStartTime;
	/** External flag for ending research at the field site. */
	private boolean endFieldSite;
	/** The field site location. */
	private Coordinates fieldSite;
	/** Scientific study to research. */
	private ScientificStudy study;
	/** The person leading the areology research. */
	private Person leadResearcher;

	private static final int oxygenID = ResourceUtil.oxygenID;
	private static final int waterID = ResourceUtil.waterID;
	private static final int foodID = ResourceUtil.foodID;

	private static final ScienceType areology = ScienceType.AREOLOGY;

	/**
	 * Constructor.
	 * 
	 * @param startingPerson {@link Person} the person starting the mission.
	 * @throws MissionException if problem constructing mission.
	 */
	public AreologyFieldStudy(Person startingPerson) {

		// Use RoverMission constructor.
		super(DEFAULT_DESCRIPTION, missionType, startingPerson, MIN_PEOPLE);

//		// Check if it has a vehicle 
//		if (!hasVehicle()) {
//			endMission(Mission.NO_AVAILABLE_VEHICLES);
//		}

		Settlement s = startingPerson.getSettlement();

		if (!isDone() && s != null) {
			// Set the lead areology researcher and study.
			leadResearcher = startingPerson;
			study = determineStudy(leadResearcher);
			if (study == null) {
				addMissionStatus(MissionStatus.NO_ONGOING_SCIENTIFIC_STUDY);
				endMission();
			}

			setStartingSettlement(s);

			// Set mission capacity.
			if (hasVehicle())
				setMissionCapacity(getRover().getCrewCapacity());
			int availableSuitNum = Mission.getNumberAvailableEVASuitsAtSettlement(s);
			if (availableSuitNum < getMissionCapacity())
				setMissionCapacity(availableSuitNum);

			// Recruit additional members to mission.
			recruitMembersForMission(startingPerson);

			// Determine field site location.
			if (hasVehicle()) {
				double tripTimeLimit = getTotalTripTimeLimit(getRover(), getPeopleNumber(), true);
				determineFieldSite(getVehicle().getRange(missionType), tripTimeLimit);
			}

			// Add home settlement
			addNavpoint(new NavPoint(s.getCoordinates(), s, s.getName()));

			// Check if vehicle can carry enough supplies for the mission.
			if (hasVehicle() && !isVehicleLoadable()) {
				addMissionStatus(MissionStatus.VEHICLE_NOT_LOADABLE);
				endMission();
			}
		}

		if (s != null) {
			// Add researching site phase.
			addPhase(RESEARCH_SITE);

			// Set initial mission phase.
			setPhase(VehicleMission.REVIEWING);
			setPhaseDescription(Msg.getString("Mission.phase.reviewing.description"));//, s.getName())); // $NON-NLS-1$

		}
	}

	/**
	 * Constructor with explicit information.
	 * 
	 * @param members            the mission members.
	 * @param startingSettlement the settlement the mission starts at.
	 * @param leadResearcher     the lead researcher
	 * @param study              the scientific study.
	 * @param rover              the rover used by the mission.
	 * @param fieldSite          the field site to research.
	 * @param description        the mission description.
	 * @throws MissionException if error creating mission.
	 */
	public AreologyFieldStudy(Collection<Person> members, Settlement startingSettlement, Person leadResearcher,
			ScientificStudy study, Rover rover, Coordinates fieldSite, String description) {

		// Use RoverMission constructor.
		super(description, missionType, leadResearcher, MIN_PEOPLE, rover);

		// Check if it has a vehicle 
//		if (!hasVehicle()) {
//			endMission(Mission.NO_AVAILABLE_VEHICLES);
//		}

		setStartingSettlement(startingSettlement);
		this.study = study;
		this.leadResearcher = leadResearcher;
		this.fieldSite = fieldSite;
		addNavpoint(new NavPoint(fieldSite, "field research site"));

		// Set mission capacity.
		setMissionCapacity(getRover().getCrewCapacity());
		int availableSuitNum = Mission.getNumberAvailableEVASuitsAtSettlement(startingSettlement);
		if (availableSuitNum < getMissionCapacity()) {
			setMissionCapacity(availableSuitNum);
		}

		// Add mission members.
		Iterator<Person> i = members.iterator();
		while (i.hasNext()) {
			i.next().getMind().setMission(this);
		}

		// Add home settlement
		addNavpoint(new NavPoint(getStartingSettlement().getCoordinates(), getStartingSettlement(),
				getStartingSettlement().getName()));

		// Add researching site phase.
		addPhase(RESEARCH_SITE);

		// Set initial mission phase.
		setPhase(VehicleMission.EMBARKING);
		setPhaseDescription(Msg.getString("Mission.phase.embarking.description", getStartingSettlement().getName())); // $NON-NLS-1$
		
		// Check if vehicle can carry enough supplies for the mission.
		if (hasVehicle() && !isVehicleLoadable()) {
			addMissionStatus(MissionStatus.VEHICLE_NOT_LOADABLE);
			endMission();
		}
	}

	/**
	 * Gets the scientific study for the mission.
	 * 
	 * @return scientific study.
	 */
	public ScientificStudy getScientificStudy() {
		return study;
	}

	/**
	 * Gets the lead researcher for the mission.
	 * 
	 * @return the researcher.
	 */
	public Person getLeadResearcher() {
		return leadResearcher;
	}

	/**
	 * Determine the scientific study used for the mission.
	 * 
	 * @param researcher the science researcher.
	 * @return scientific study or null if none determined.
	 */
	public static ScientificStudy determineStudy(Person researcher) {
		ScientificStudy result = null;

		// ScienceType areology = ScienceType.AREOLOGY;
		List<ScientificStudy> possibleStudies = new ArrayList<ScientificStudy>();

		// Add primary study if in research phase.
		ScientificStudy primaryStudy = scientificManager.getOngoingPrimaryStudy(researcher);
		if (primaryStudy != null) {
			if (ScientificStudy.RESEARCH_PHASE.equals(primaryStudy.getPhase())
					&& !primaryStudy.isPrimaryResearchCompleted()) {
				if (areology == primaryStudy.getScience()) {
					// Primary study added twice to double chance of random selection.
					possibleStudies.add(primaryStudy);
					possibleStudies.add(primaryStudy);
				}
			}
		}

		// Add all collaborative studies in research phase.
		Iterator<ScientificStudy> i = scientificManager.getOngoingCollaborativeStudies(researcher).iterator();
		while (i.hasNext()) {
			ScientificStudy collabStudy = i.next();
			if (ScientificStudy.RESEARCH_PHASE.equals(collabStudy.getPhase())
					&& !collabStudy.isCollaborativeResearchCompleted(researcher)) {
				if (areology == collabStudy.getCollaborativeResearchers().get(researcher.getIdentifier())) {
					possibleStudies.add(collabStudy);
				}
			}
		}

		// Randomly select study.
		if (possibleStudies.size() > 0) {
			int selected = RandomUtil.getRandomInt(possibleStudies.size() - 1);
			result = possibleStudies.get(selected);
		}

		return result;
	}

	/**
	 * Gets the time limit of the trip based on life support capacity.
	 * 
	 * @param useBuffer use time buffer in estimation if true.
	 * @return time (millisols) limit.
	 * @throws MissionException if error determining time limit.
	 */
	public static double getTotalTripTimeLimit(Rover rover, int memberNum, boolean useBuffer) {

		Inventory vInv = rover.getInventory();

		double timeLimit = Double.MAX_VALUE;

		// Check food capacity as time limit.
		double foodConsumptionRate = personConfig.getFoodConsumptionRate();// * Mission.FOOD_MARGIN;
		double foodCapacity = vInv.getAmountResourceCapacity(foodID, false);
		double foodTimeLimit = foodCapacity / (foodConsumptionRate * memberNum);
		if (foodTimeLimit < timeLimit) {
			timeLimit = foodTimeLimit;
		}

		// Check dessert1 capacity as time limit.
//        AmountResource dessert1 = AmountResource.findAmountResource("Soymilk");
//        double dessert1ConsumptionRate = config.getFoodConsumptionRate() / 6D;
//        double dessert1Capacity = vInv.getAmountResourceCapacity(dessert1, false);
//        double dessert1TimeLimit = dessert1Capacity / (dessert1ConsumptionRate * memberNum);
//        if (dessert1TimeLimit < timeLimit)
//            timeLimit = dessert1TimeLimit;

		// Check water capacity as time limit.
		double waterConsumptionRate = personConfig.getWaterConsumptionRate();// * Mission.WATER_MARGIN;
		double waterCapacity = vInv.getAmountResourceCapacity(waterID, false);
		double waterTimeLimit = waterCapacity / (waterConsumptionRate * memberNum);
		if (waterTimeLimit < timeLimit) {
			timeLimit = waterTimeLimit;
		}

		// Check oxygen capacity as time limit.
		double oxygenConsumptionRate = personConfig.getNominalO2ConsumptionRate();// * Mission.OXYGEN_MARGIN;
		double oxygenCapacity = vInv.getAmountResourceCapacity(oxygenID, false);
		double oxygenTimeLimit = oxygenCapacity / (oxygenConsumptionRate * memberNum);
		if (oxygenTimeLimit < timeLimit) {
			timeLimit = oxygenTimeLimit;
		}

		// Convert timeLimit into millisols and use error margin.
		timeLimit = (timeLimit * 1000D);
		if (useBuffer) {
			timeLimit /= Vehicle.getLifeSupportRangeErrorMargin();
		}

		return timeLimit;
	}

	/**
	 * Determine the location of the research site.
	 * 
	 * @param roverRange    the rover's driving range
	 * @param tripTimeLimit the time limit (millisols) of the trip.
	 * @throws MissionException of site can not be determined.
	 */
	private void determineFieldSite(double roverRange, double tripTimeLimit) {

		// Determining the actual traveling range.
		double range = roverRange;
		double timeRange = getTripTimeRange(tripTimeLimit, true);
		if (timeRange < range) {
			range = timeRange;
		}

		// Get the current location.
		Coordinates startingLocation = getCurrentMissionLocation();

		// Determine the research site.
		Direction direction = new Direction(RandomUtil.getRandomDouble(2 * Math.PI));
		double limit = range / 4D;
		double siteDistance = RandomUtil.getRandomDouble(limit);
		fieldSite = startingLocation.getNewLocation(direction, siteDistance);
		addNavpoint(new NavPoint(fieldSite, "field research site"));
	}

	/**
	 * Gets the range of a trip based on its time limit.
	 * 
	 * @param tripTimeLimit time (millisols) limit of trip.
	 * @param useBuffer     Use time buffer in estimations if true.
	 * @return range (km) limit.
	 */
	private double getTripTimeRange(double tripTimeLimit, boolean useBuffer) {
		double timeAtSite = FIELD_SITE_TIME;
		double tripTimeTravelingLimit = tripTimeLimit - timeAtSite;
		double averageSpeed = getAverageVehicleSpeedForOperators();
		double millisolsInHour = MarsClock.convertSecondsToMillisols(60D * 60D);
		double averageSpeedMillisol = averageSpeed / millisolsInHour;
		return tripTimeTravelingLimit * averageSpeedMillisol;
	}

	@Override
	public double getMissionQualification(MissionMember member) {
		double result = super.getMissionQualification(member);

		if ((result > 0D) && (member instanceof Person)) {

			Person person = (Person) member;

			// Add modifier if person is a researcher on the same scientific study.
			// ScienceType areology = ScienceType.AREOLOGY;
			if (study != null) {
				if (person == study.getPrimaryResearcher()) {
					result += 2D;

					// Check if study's primary science is areology.
					if (areology.equals(study.getScience())) {
						result += 1D;
					}
				} else if (study.getCollaborativeResearchers().containsKey(person.getIdentifier())) {
					result += 1D;

					// Check if study collaboration science is in areology.
					ScienceType collabScience = study.getCollaborativeResearchers().get(person.getIdentifier());
					if (areology == collabScience) {
						result += 1D;
					}
				}
			}
		}

		return result;
	}

	@Override
	public Map<Integer, Integer> getEquipmentNeededForRemainingMission(boolean useBuffer) {
		if (equipmentNeededCache != null) {
			return equipmentNeededCache;
		} else {
			Map<Integer, Integer> result = new HashMap<>();
			equipmentNeededCache = result;
			return result;
		}
	}

	@Override
	public Settlement getAssociatedSettlement() {
		return getStartingSettlement();
	}

	@Override
	protected void determineNewPhase() {
		if (REVIEWING.equals(getPhase())) {
			setPhase(VehicleMission.EMBARKING);
			setPhaseDescription(
					Msg.getString("Mission.phase.embarking.description", getCurrentNavpoint().getDescription()));//startingMember.getSettlement().toString())); // $NON-NLS-1$
		}
		
		else if (EMBARKING.equals(getPhase())) {
			startTravelToNextNode();
			setPhase(VehicleMission.TRAVELLING);
			setPhaseDescription(
					Msg.getString("Mission.phase.travelling.description", getNextNavpoint().getDescription())); // $NON-NLS-1$
		} 
		
		else if (TRAVELLING.equals(getPhase())) {
			if (getCurrentNavpoint().isSettlementAtNavpoint()) {
				setPhase(VehicleMission.DISEMBARKING);
				setPhaseDescription(Msg.getString("Mission.phase.disembarking.description",
						getCurrentNavpoint().getSettlement().getName())); // $NON-NLS-1$
			} else {
				setPhase(RESEARCH_SITE);
				setPhaseDescription(Msg.getString("Mission.phase.researchingFieldSite.description",
						getCurrentNavpoint().getDescription())); // $NON-NLS-1$
			}
		} 
		
		else if (RESEARCH_SITE.equals(getPhase())) {
			startTravelToNextNode();
			setPhase(VehicleMission.TRAVELLING);
			setPhaseDescription(
					Msg.getString("Mission.phase.travelling.description", getNextNavpoint().getDescription())); // $NON-NLS-1$
		} 
		
//		else if (DISEMBARKING.equals(getPhase())) {
//			endMission(ALL_DISEMBARKED);
//		}
		
		else if (DISEMBARKING.equals(getPhase())) {
			setPhase(VehicleMission.COMPLETED);
			setPhaseDescription(
					Msg.getString("Mission.phase.completed.description")); // $NON-NLS-1$
		}
		
		else if (COMPLETED.equals(getPhase())) {
			addMissionStatus(MissionStatus.MISSION_ACCOMPLISHED);
			endMission();
		}
	}

	@Override
	protected void performPhase(MissionMember member) {
		super.performPhase(member);
		if (RESEARCH_SITE.equals(getPhase())) {
			researchFieldSitePhase(member);
		}
	}

	/**
	 * Ends the research at a field site.
	 */
	public void endResearchAtFieldSite() {
		logger.warning("Research field site phase ended due to external trigger.");
		endFieldSite = true;

		// End each member's areology field work task.
		Iterator<MissionMember> i = getMembers().iterator();
		while (i.hasNext()) {
			MissionMember member = i.next();
			if (member instanceof Person) {
				Person person = (Person) member;
				Task task = person.getMind().getTaskManager().getTask();
				if (task instanceof AreologyStudyFieldWork) {
					((AreologyStudyFieldWork) task).endEVA();
				}
			}
		}
	}

	/**
	 * Performs the research field site phase of the mission.
	 * 
	 * @param member the mission member currently performing the mission
	 */
	private void researchFieldSitePhase(MissionMember member) {

		// Check if field site research has just started.
		if (fieldSiteStartTime == null) {
			fieldSiteStartTime = (MarsClock) marsClock.clone();
		}

		// Check if crew has been at site for more than required length of time.
		boolean timeExpired = false;
//		MarsClock currentTime = (MarsClock) Simulation.instance().getMasterClock().getMarsClock().clone();
		if (MarsClock.getTimeDiff((MarsClock) marsClock.clone(), fieldSiteStartTime) >= FIELD_SITE_TIME) {
			timeExpired = true;
		}

		if (isEveryoneInRover()) {

			// Check if end field site flag is set.
			if (endFieldSite) {
				endFieldSite = false;
				setPhaseEnded(true);
			}

			// Check if crew has been at site for more than required length of time, then
			// end this phase.
			if (timeExpired) {
				setPhaseEnded(true);
			}

			// Determine if no one can start the field work task.
			boolean nobodyFieldWork = true;
			Iterator<MissionMember> j = getMembers().iterator();
			while (j.hasNext()) {
				if (AreologyStudyFieldWork.canResearchSite(j.next(), getRover())) {
					nobodyFieldWork = false;
				}
			}

			// If no one can research the site and this is not due to it just being
			// night time, end the field work phase.
			boolean inDarkPolarRegion = surfaceFeatures.inDarkPolarRegion(getCurrentMissionLocation());
			double sunlight = surfaceFeatures.getSolarIrradiance(getCurrentMissionLocation());
			if (nobodyFieldWork && ((sunlight > 0D) || inDarkPolarRegion)) {
				setPhaseEnded(true);
			}

			// Anyone in the crew or a single person at the home settlement has a dangerous
			// illness, end phase.
			if (hasEmergency()) {
				setPhaseEnded(true);
			}

			// Check if enough resources for remaining trip. false = not using margin.
			if (!hasEnoughResourcesForRemainingMission(false)) {
				// If not, determine an emergency destination.
				determineEmergencyDestination(member);
				setPhaseEnded(true);
			}
		} else {
			// If research time has expired for the site, have everyone end their field work
			// tasks.
			if (timeExpired) {
				Iterator<MissionMember> i = getMembers().iterator();
				while (i.hasNext()) {
					MissionMember tempMember = i.next();
					if (tempMember instanceof Person) {
						Person tempPerson = (Person) tempMember;
						Task task = tempPerson.getMind().getTaskManager().getTask();
						if ((task != null) && (task instanceof AreologyStudyFieldWork)) {
							((AreologyStudyFieldWork) task).endEVA();
						}
					}
				}
			}
		}

		if (!getPhaseEnded()) {

			if (!endFieldSite && !timeExpired) {
				// If person can research the site, start that task.
				if (AreologyStudyFieldWork.canResearchSite(member, getRover())) {
					// TODO Refactor
					if (member instanceof Person) {
						Person person = (Person) member;
						assignTask(person,
								new AreologyStudyFieldWork(person, leadResearcher, study, (Rover) getVehicle()));
					}
				}
			}
		}
	}

	@Override
	protected boolean isCapableOfMission(MissionMember member) {
		boolean result = super.isCapableOfMission(member);
		if (result) {
			boolean atStartingSettlement = false;
			if (member.isInSettlement()) {
				if (member.getSettlement() == getStartingSettlement()) {
					atStartingSettlement = true;
				}
			}
			result = atStartingSettlement;
		}

		return result;
	}

	@Override
	public double getEstimatedRemainingMissionTime(boolean useBuffer) {
		double result = super.getEstimatedRemainingMissionTime(useBuffer);
		result += getEstimatedRemainingFieldSiteTime();
		return result;
	}

	/**
	 * Gets the estimated time remaining for the field site in the mission.
	 * 
	 * @return time (millisols)
	 * @throws MissionException if error estimating time.
	 */
	private double getEstimatedRemainingFieldSiteTime() {
		double result = 0D;

		// Add estimated remaining field work time at field site if still there.
		if (RESEARCH_SITE.equals(getPhase())) {
//			MarsClock currentTime = Simulation.instance().getMasterClock().getMarsClock();
			double timeSpentAtExplorationSite = MarsClock.getTimeDiff(marsClock, fieldSiteStartTime);
			double remainingTime = FIELD_SITE_TIME - timeSpentAtExplorationSite;
			if (remainingTime > 0D) {
				result += remainingTime;
			}
		}

		// If field site hasn't been visited yet, add full field work time.
		if (fieldSiteStartTime == null) {
			result += FIELD_SITE_TIME;
		}

		return result;
	}

	@Override
	public Map<Integer, Number> getResourcesNeededForRemainingMission(boolean useBuffer) {

		Map<Integer, Number> result = super.getResourcesNeededForRemainingMission(useBuffer);

		double fieldSiteTime = getEstimatedRemainingFieldSiteTime();
		double timeSols = fieldSiteTime / 1000D;

		int crewNum = getPeopleNumber();

		// Determine life support supplies needed for trip.
		double oxygenAmount = PhysicalCondition.getOxygenConsumptionRate() * timeSols * crewNum;
		if (result.containsKey(oxygenID)) {
			oxygenAmount += (Double) result.get(oxygenID);
		}
		result.put(oxygenID, oxygenAmount);

		double waterAmount = PhysicalCondition.getWaterConsumptionRate() * timeSols * crewNum;
		if (result.containsKey(waterID)) {
			waterAmount += (Double) result.get(waterID);
		}
		result.put(waterID, waterAmount);

		double foodAmount = PhysicalCondition.getFoodConsumptionRate() * timeSols * crewNum;
//				* PhysicalCondition.FOOD_RESERVE_FACTOR;
		if (result.containsKey(foodID)) {
			foodAmount += (Double) result.get(foodID);
		}
		result.put(foodID, foodAmount);

		// Add the chosen dessert for the journey
//		String [] availableDesserts = PreparingDessert.getArrayOfDesserts();
//	  	// Added PreparingDessert.DESSERT_SERVING_FRACTION since eating desserts is optional and is only meant to help if food is low.
//		double dessertAmount = PhysicalCondition.getDessertConsumptionRate() * timeSols * crewNum * PreparingDessert.DESSERT_SERVING_FRACTION ;
//	  	// Put together a list of available dessert
//        for(String n : availableDesserts) {
//    		AmountResource dessert = AmountResource.findAmountResource(n);
//        	// match the chosen dessert for the journey
//            if (result.containsKey(dessert))
//                dessertAmount += (Double) result.get(dessert);
//            result.put(dessert, dessertAmount);
//        }

//        AmountResource dessert1 = AmountResource.findAmountResource("Soymilk");
//        double dessert1Amount = PhysicalCondition.getFoodConsumptionRate() / 6D * timeSols * crewNum;
//        if (result.containsKey(dessert1))
//            dessert1Amount += (Double) result.get(dessert1);
//        result.put(dessert1, dessert1Amount);
		return result;
	}

	@Override
	public void destroy() {
		super.destroy();
		fieldSiteStartTime = null;
		fieldSite = null;
		study = null;
		leadResearcher = null;
	}
}