/**
 * Mars Simulation Project
 * ObserveAstronomicalObjectsMeta.java
 * @version 3.1.0 2017-10-23
 * @author Scott Davis
 */
package org.mars_sim.msp.core.person.ai.task.meta;

import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Logger;

import org.mars_sim.msp.core.Msg;
import org.mars_sim.msp.core.person.FavoriteType;
import org.mars_sim.msp.core.person.Person;
import org.mars_sim.msp.core.person.PhysicalCondition;
import org.mars_sim.msp.core.person.ai.job.Job;
import org.mars_sim.msp.core.person.ai.task.ObserveAstronomicalObjects;
import org.mars_sim.msp.core.person.ai.task.utils.MetaTask;
import org.mars_sim.msp.core.person.ai.task.utils.Task;
import org.mars_sim.msp.core.robot.Robot;
import org.mars_sim.msp.core.science.ScienceType;
import org.mars_sim.msp.core.science.ScientificStudy;
import org.mars_sim.msp.core.structure.building.function.AstronomicalObservation;
import org.mars_sim.msp.core.tool.RandomUtil;

/**
 * Meta task for the ObserveAstronomicalObjects task.
 */
public class ObserveAstronomicalObjectsMeta implements MetaTask, Serializable {

    /** default serial id. */
    private static final long serialVersionUID = 1L;
    
    /** Task name */
    private static final String NAME = Msg.getString(
            "Task.description.observeAstronomicalObjects"); //$NON-NLS-1$

    /** default logger. */
    private static Logger logger = Logger.getLogger(ObserveAstronomicalObjectsMeta.class.getName());

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Task constructInstance(Person person) {
        return new ObserveAstronomicalObjects(person);
    }

    @Override
    public double getProbability(Person person) {

        double result = 0D;

        // Get local observatory if available.
        AstronomicalObservation observatory = ObserveAstronomicalObjects.determineObservatory(person);
        
        if (null != observatory && person.isInSettlement()) {

            // Probability affected by the person's stress and fatigue.
            PhysicalCondition condition = person.getPhysicalCondition();
            double fatigue = condition.getFatigue();
            double stress = condition.getStress();
            double hunger = condition.getHunger();
            
            if (fatigue > 1000 || stress > 50 || hunger > 500)
            	return 0;
            
            // Check if it is completely dark outside.
            double sunlight = surface.getSolarIrradiance(person.getCoordinates());

            if (sunlight == 0D) {

                ScienceType astronomy = ScienceType.ASTRONOMY;

                // Add probability for researcher's primary study (if any).
                ScientificStudy primaryStudy = scientificStudyManager.getOngoingPrimaryStudy(person);
                if ((primaryStudy != null) && ScientificStudy.RESEARCH_PHASE.equals(
                        primaryStudy.getPhase())) {
                    if (!primaryStudy.isPrimaryResearchCompleted() &&
                            astronomy == primaryStudy.getScience()) {
                        try {
                            double primaryResult = 100D;

                            // Get observatory building crowding modifier.
                            primaryResult *= ObserveAstronomicalObjects.getObservatoryCrowdingModifier(person, observatory);

                            // If researcher's current job isn't related to astronomy, divide by two.
                            Job job = person.getMind().getJob();
                            if (job != null) {
                                ScienceType jobScience = ScienceType.getJobScience(job);
                                if (astronomy != jobScience) {
                                    primaryResult /= 2D;
                                }
                            }

                            result += primaryResult;
                        }
                        catch (Exception e) {
                            logger.severe("getProbability(): " + e.getMessage());
                        }
                    }
                }

                // Add probability for each study researcher is collaborating on.
                Iterator<ScientificStudy> i = scientificStudyManager.getOngoingCollaborativeStudies(person).iterator();
                while (i.hasNext()) {
                    ScientificStudy collabStudy = i.next();
                    if (ScientificStudy.RESEARCH_PHASE.equals(collabStudy.getPhase())) {
                        if (!collabStudy.isCollaborativeResearchCompleted(person)) {
                            if (astronomy == collabStudy.getCollaborativeResearchers().get(person.getIdentifier())) {
                                try {
                                    double collabResult = 50D;


                                    // Get observatory building crowding modifier.
                                    collabResult *= ObserveAstronomicalObjects.getObservatoryCrowdingModifier(person, observatory);

                                    // If researcher's current job isn't related to astronomy, divide by two.
                                    Job job = person.getMind().getJob();
                                    if (job != null) {
                                        ScienceType jobScience = ScienceType.getJobScience(job);
                                        if (astronomy != jobScience) {
                                            collabResult /= 2D;
                                        }
                                    }

                                    result += collabResult;
                                }
                                catch (Exception e) {
                                    logger.severe("getProbability(): " + e.getMessage());
                                }
                            }
                        }
                    }
                }

                if (result <= 0) return 0;
                
                // Effort-driven task modifier.
                result *= person.getPerformanceRating();

                // Job modifier.
                Job job = person.getMind().getJob();
                if (job != null) {
                    result *= job.getStartTaskProbabilityModifier(ObserveAstronomicalObjects.class)
                    		* (person.getAssociatedSettlement().getGoodsManager().getTourismFactor()
    	               		 + person.getAssociatedSettlement().getGoodsManager().getResearchFactor())/1.5;
                }

                // Modify if research is the person's favorite activity.
                if (person.getFavorite().getFavoriteActivity() == FavoriteType.ASTRONOMY) {
                    result += RandomUtil.getRandomInt(1, 20);
                }
                
                if (person.getFavorite().getFavoriteActivity() == FavoriteType.RESEARCH) {
                    result *= 1.2D;
                }
                
    	        // 2015-06-07 Added Preference modifier
                if (result > 0)
                	result = result + result * person.getPreference().getPreferenceScore(this)/2D;

    	        if (result < 0) result = 0;
            }
        }

        return result;
    }

	@Override
	public Task constructInstance(Robot robot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getProbability(Robot robot) {
		// TODO Auto-generated method stub
		return 0;
	}
}