package edu.uf.interactable;

import java.util.ArrayList;
import java.util.List;

import edu.uf.geometry.Voxel;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public abstract class Phagocyte extends Cell{

    public static final int INACTIVE = 0;
    public static final int INACTIVATING = 1;
    public static final int RESTING = 2;
    public static final int ACTIVATING = 3;
    public static final int ACTIVE = 4;
    public static final int APOPTOTIC = 5;
    public static final int NECROTIC = 6;
    public static final int DEAD = 7;
    public static final int ANERGIC = 8;

    public static final int FREE = 0;
    public static final int INTERACTING = 1;

    
    
    //private Phagosome phagosome;
    private List<InfectiousAgent> phagosome;
    
    private boolean engaged;
    
    private int state;
    private int status;

	public Phagocyte() {
    	this(0.0);
    }
    
    public Phagocyte (double ironPool) {
    	super();
        this.setIronPool(ironPool);
        this.setStatusIteration(0);
        this.setStateIteration(0);
        //this.phagosome = new Phagosome();
        this.phagosome = new ArrayList<>();
        
        this.state = FREE;
        this.status = RESTING; 
    }
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setPhagosome(List<InfectiousAgent> phagosome) {
		this.phagosome = phagosome;
	}

    public abstract int getMaxConidia();
    

    public List<InfectiousAgent> getPhagosome() {
        return this.phagosome; 
    }

	public boolean isEngaged() {
		return engaged; 
	}

	public void setEngaged(boolean engaged) {
		this.engaged = engaged;
	}

    public void addAspergillus(Afumigatus aspergillus) {
        this.phagosome.add(aspergillus);
    }

    public void kill() {
        if(Rand.getRand().randunif() < Constants.PR_KILL) {
        	for(InfectiousAgent a : this.phagosome) {
                this.incIronPool(a.getIronPool());
                a.incIronPool(-a.getIronPool());
                a.die();
        	}
            this.phagosome.clear();
        }
    }

    public void move(Voxel oldVoxel, int steps) {
        if(steps < this.getMaxMoveSteps()) {
        	
        	Util.calcDriftProbability(oldVoxel, this);
            Voxel newVoxel = Util.getVoxel(oldVoxel, Rand.getRand().randunif());
        	
            oldVoxel.removeCell(this.getId());
            //System.out.println(oldVoxel + " " + newVoxel);
            newVoxel.setCell(this);
            steps = steps + 1;
            PositionalAgent p = null;
            for(InfectiousAgent a : this.phagosome) {
            	if(a instanceof PositionalAgent) {
            		p = (PositionalAgent) a;
            		p.setX(newVoxel.getX() + Rand.getRand().randunif());
            		p.setY(newVoxel.getY() + Rand.getRand().randunif());
            		p.setZ(newVoxel.getZ() + Rand.getRand().randunif());
            	}
            }
            move(newVoxel, steps);
        }
    }

    public static void intAspergillus(Phagocyte phagocyte, Afumigatus aspergillus) {
    	Phagocyte.intAspergillus(phagocyte, aspergillus, false);
    }
    
    public static void intAspergillus(Phagocyte phagocyte, Afumigatus aspergillus, boolean phagocytize) {
        if(aspergillus.getState() == Afumigatus.FREE) {
            if (aspergillus.getStatus() == Afumigatus.RESTING_CONIDIA || aspergillus.getStatus() == Afumigatus.SWELLING_CONIDIA || aspergillus.getStatus() == Afumigatus.STERILE_CONIDIA || phagocytize){
            	if (phagocyte.status != Phagocyte.NECROTIC && phagocyte.status != Phagocyte.APOPTOTIC && phagocyte.status != Phagocyte.DEAD) {
            		if(phagocyte.phagosome.size() < phagocyte.getMaxConidia()) {
                        //phagocyte.phagosome.hasConidia = true;
                        aspergillus.setState(Afumigatus.INTERNALIZING);
                        aspergillus.setEngulfed(true);
                        phagocyte.phagosome.add(aspergillus);
                    }
                }
            }
            if(aspergillus.getStatus() != Afumigatus.RESTING_CONIDIA) {
                phagocyte.state = Phagocyte.INTERACTING;
                if(phagocyte.status != Phagocyte.ACTIVE) 
                    phagocyte.status = Phagocyte.ACTIVATING;
                else
                    phagocyte.setStatusIteration(0);
            }
        }
    }
	
}
