package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.utils.Constants;
import edu.uf.utils.Rand;

public class Macrophage extends Phagocyte{

    public static final String NAME = "Macrophage";

    private static String chemokine = null;

    //private static final int[] InitMacrophageBooleanState = new int[] {1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1};

    private static int totalCells = 0;
    private static double totalIron = 0; 
    
    private boolean tnfa;
    private boolean heme;
    private boolean fpn;
    private int fpnIteration; 
    //public int[] booleanNetwork;
    private int maxMoveStep;
    private boolean engaged;

    public Macrophage(double ironPool) {
    	super(ironPool);
        Macrophage.totalCells = Macrophage.totalCells + 1;
        //this.boolean_network = Macrophage.InitMacrophageBooleanState.clone();
        this.setStatus(Macrophage.RESTING);
        this.setState(Macrophage.FREE);
        this.fpn = true;
        this.fpnIteration = 0;
        this.maxMoveStep = -1; 
        this.tnfa = false;
        this.engaged = false; //### CHANGE HERE!!!
        Macrophage.totalIron = Macrophage.totalIron + ironPool;
    }
    
    public static String getChemokine() {
		return chemokine;
	}

	public static void setChemokine(String chemokine) {
		Macrophage.chemokine = chemokine;
	}

	public static int getTotalCells() {
		return totalCells;
	}

	public static void setTotalCells(int totalCells) {
		Macrophage.totalCells = totalCells;
	}

	public static double getTotalIron() {
		return totalIron;
	}

	public static void setTotalIron(double totalIron) {
		Macrophage.totalIron = totalIron;
	}

	public boolean isTnfa() {
		return tnfa;
	}

	public void setTnfa(boolean tnfa) {
		this.tnfa = tnfa;
	}
	
	public void setHeme(boolean heme) {
		this.heme = heme;
	}

	public boolean isFpn() {
		return fpn;
	}

	public void setFpn(boolean fpn) {
		this.fpn = fpn;
	}

	public int getFpnIteration() {
		return fpnIteration;
	}

	public void setFpnIteration(int fpnIteration) {
		this.fpnIteration = fpnIteration;
	}

	public boolean isEngaged() {
		return engaged;
	}

	public void setEngaged(boolean engaged) {
		this.engaged = engaged;
	}

    public int getMaxMoveSteps() { 
        if(this.maxMoveStep == -1) {
            if(this.getStatus() == Macrophage.ACTIVE)
                this.maxMoveStep = Rand.getRand().randpois(Constants.MA_MOVE_RATE_ACT);
            else
                this.maxMoveStep = Rand.getRand().randpois(Constants.MA_MOVE_RATE_ACT);
        }
        return this.maxMoveStep;
    }
    
    public Set<String> getNegativeInteractionList(){
    	
    	if(this.negativeInteractList == null) {
    		synchronized(this) {
    			this.negativeInteractList = new HashSet<>();
    			this.negativeInteractList.add(Macrophage.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        return interactable.interact(this, x, y, z);
    }

    public void processBooleanNetwork(){}


    public void updateStatus() {
        if(this.getStatus() == Macrophage.DEAD)
            return;
        if(this.getStatus() == Macrophage.NECROTIC) {
            this.die();
            for(InfectiousAgent entry : this.getPhagosome())
            	entry.setState(Afumigatus.RELEASING);
        }else if(this.getPhagosome().size() > Constants.MA_MAX_CONIDIA)
            this.setStatus(Phagocyte.NECROTIC);
        else if(this.getStatus() == Macrophage.ACTIVE) {
            if(this.getStatusIteration() >= Constants.ITER_TO_REST){//#30*6:#Constants.ITER_TO_CHANGE_STATE:
                this.setStatusIteration(0);
                this.tnfa = false;
                if(this.heme)
                	this.setStatus(Macrophage.ANERGIC);
                else
                	this.setStatus(Macrophage.RESTING);
            }else
                this.setStatusIteration(this.getStatusIteration() + 1);
        }else if(this.getStatus() == Macrophage.INACTIVE) {
            if(this.getStatusIteration() >= Constants.ITER_TO_CHANGE_STATE) {
                this.setStatusIteration(0);
                this.setStatus(Macrophage.RESTING);
            }else
                this.setStatusIteration(this.getStatusIteration() + 1);
        }else if(this.getStatus() == Macrophage.ACTIVATING) {
            if(this.getStatusIteration() >= Constants.ITER_TO_CHANGE_STATE) {
                this.setStatusIteration(0);
                this.setStatus(Macrophage.ACTIVE);
            }else
                this.setStatusIteration(this.getStatusIteration() + 1);
        }else if(this.getStatus() == Macrophage.INACTIVATING){
            if(this.getStatusIteration() >= Constants.ITER_TO_CHANGE_STATE) {
                this.setStatusIteration(0);
                this.setStatus(Macrophage.INACTIVE);
            }else
                this.setStatusIteration(this.getStatusIteration() + 1);
        }else if(this.getStatus() == Macrophage.ANERGIC) {
            if(this.getStatusIteration() >= Constants.ITER_TO_CHANGE_STATE) {
                this.setStatusIteration(0);
                this.setStatus(Macrophage.RESTING);
            }else
                this.setStatusIteration(this.getStatusIteration() + 1);
        }
        /*
        #if not (this.status == Macrophage.DEAD or this.status == Macrophage.NECROTIC or this.status == Macrophage.APOPTOTIC):
        #    if random() < Util.activation_function((this.iron_pool - Constants.MA_INTERNAL_IRON), Constants.Kd_MA_IRON, Constants.STD_UNIT_T, Constants.MA_VOL):
        #        this.status = Macrophage.ANERGIC
        #        this.status_iteration = 0

        #this.engaged = False  CHANGE HERE!!!
        */

        if(Rand.getRand().randunif() < Constants.MA_HALF_LIFE && this.getPhagosome().size() == 0 &&
        		Macrophage.totalCells > Constants.MIN_MA) 
            this.die();

        if (!this.fpn){
            if(this.fpnIteration >= Constants.ITER_TO_CHANGE_STATE) {
                this.fpnIteration = 0;
                this.fpn = true;
            }else
                this.fpnIteration = this.fpnIteration + 1;
        }
        //this.setMoveStep(0);
        this.maxMoveStep = -1;
    }
        
        

    public boolean isDead() {
        return this.getStatus() == Macrophage.DEAD;
    }

    public void incIronPool(double qtty) {
        this.setIronPool(this.getIronPool() + qtty);
        Macrophage.totalIron = Macrophage.totalIron + qtty;
    }

    public void die() {
        if(this.getStatus() != Macrophage.DEAD) {
            this.setStatus(Macrophage.DEAD);
            Macrophage.totalCells = Macrophage.totalCells - 1;
        }
    }

    public String attractedBy() {
        return Macrophage.chemokine;
    }

	@Override
	public int getMaxConidia() {
		return Constants.MA_MAX_CONIDIA;
	}
	
	public String getName() {
    	return NAME;
    }
	
}
