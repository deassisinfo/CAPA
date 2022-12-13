package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.utils.Constants;
import edu.uf.utils.Rand;

public class Neutrophil extends Phagocyte{
    public static final String NAME = "Neutrophils";


    private static String chemokine;
    private static int totalCells = 0;
    private static double totalIron = 0;
    private int maxMoveStep;
    
    private boolean tnfa;

    public Neutrophil(double ironPool) {
    	super(ironPool);
        Neutrophil.totalCells = Neutrophil.totalCells + 1;
        this.setStatus(Neutrophil.RESTING); 
        this.setState(Neutrophil.FREE);
        Neutrophil.totalIron = Neutrophil.totalIron + ironPool;
        this.maxMoveStep = -1;
        this.tnfa = false;
        this.setEngaged(false);
    } 
    
    public static String getChemokine() {
		return chemokine;
	}

	public static void setChemokine(String chemokine) {
		Neutrophil.chemokine = chemokine;
	}

	public static int getTotalCells() {
		return totalCells;
	}

	public static void setTotalCells(int totalCells) {
		Neutrophil.totalCells = totalCells;
	}

	public static double getTotalIron() {
		return totalIron;
	}

	public static void setTotalIron(double totalIron) {
		Neutrophil.totalIron = totalIron;
	}

	public boolean isTnfa() {
		return tnfa;
	}

	public void setTnfa(boolean tnfa) {
		this.tnfa = tnfa;
	}

    public int getMaxMoveSteps(){// ##REVIEW
        if(this.maxMoveStep == -1)
            if(this.getStatus() == Macrophage.ACTIVE)
                this.maxMoveStep = Rand.getRand().randpois(Constants.MA_MOVE_RATE_REST);
            else
                this.maxMoveStep = Rand.getRand().randpois(Constants.MA_MOVE_RATE_REST);
        return this.maxMoveStep;
    }

    public void processBooleanNetwork() {}

    public void updateStatus() {
        if(this.getStatus() == Neutrophil.DEAD)
            return;
        if(this.getStatus() == Neutrophil.NECROTIC || this.getStatus() == Neutrophil.APOPTOTIC) {
            this.die();
            for(InfectiousAgent entry : this.getPhagosome())
                entry.setState(Afumigatus.RELEASING);
        }else if(Rand.getRand().randunif() < Constants.NEUTROPHIL_HALF_LIFE)
            this.setStatus(Neutrophil.APOPTOTIC);
        else if(this.getStatus() == Neutrophil.ACTIVE) {
            if(this.getStatusIteration() > Constants.ITER_TO_CHANGE_STATE) {
                this.setStatusIteration(0);
                this.tnfa = false;
                this.setStatus(Neutrophil.RESTING);
                this.setState(Neutrophil.FREE);
            }else
                this.setStatusIteration(this.getStatusIteration() + 1);
        }else if(this.getStatus() == Neutrophil.ACTIVATING) {
            if(this.getStatusIteration() > Constants.ITER_TO_CHANGE_STATE) {
                this.setStatusIteration(0);
                this.setStatus(Neutrophil.ACTIVE);
            }else
                this.setStatusIteration(this.getStatusIteration() + 1);
        }
        //this.setMoveStep(0);
        this.maxMoveStep = -1;
        this.setEngaged(false);
    }
        

    public boolean isDead() {
        return this.getStatus() == Neutrophil.DEAD;
    }

    public void incIronPool(double qtty) {
        this.setIronPool(this.getIronPool() + qtty);
        Neutrophil.totalIron = Neutrophil.totalIron + qtty;
    }
    
    public Set<String> getNegativeInteractionList(){
    	
    	if(this.negativeInteractList == null) {
    		synchronized(this) {
    			this.negativeInteractList = new HashSet<>();
    			this.negativeInteractList.add(Neutrophil.NAME);
    			this.negativeInteractList.add(TAFC.NAME);
    			this.negativeInteractList.add(Transferrin.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof Afumigatus) {
            Afumigatus interac = (Afumigatus) interactable;
        	if(this.isEngaged())
                return true;
            if(this.getStatus() != Neutrophil.APOPTOTIC && this.getStatus() != Neutrophil.NECROTIC && this.getStatus() != Neutrophil.DEAD) {
                if(interac.getStatus() == Afumigatus.HYPHAE || interac.getStatus() == Afumigatus.GERM_TUBE) {
                    double pr = Constants.PR_N_HYPHAE;
                    if (Rand.getRand().randunif() < pr) {
                        Phagocyte.intAspergillus(this, interac);
                        interac.setStatus(Afumigatus.DYING);
                    }else
                        this.setEngaged(true);
                }else if (interac.getStatus() == Afumigatus.SWELLING_CONIDIA) {
                    if (Rand.getRand().randunif() < Constants.PR_N_PHAG) 
                        Phagocyte.intAspergillus(this, interac);
                }
            }
            
            return true;
        }
        if(interactable instanceof Macrophage) {
            Macrophage interact = (Macrophage) interactable;
        	if (this.getStatus() == Neutrophil.APOPTOTIC){// and len(interactable.phagosome.agents) == 0:
                //interactable.phagosome.agents = this.phagosome.agents
        		interact.incIronPool(this.getIronPool());
                this.incIronPool(this.getIronPool());
                this.die();
                interact.setStatus(Macrophage.INACTIVE);
        	}
            return true;
        }
        if (interactable instanceof Iron) {
        	Iron interac = (Iron) interactable;
            if(this.getStatus() == Neutrophil.NECROTIC){//# or this.status == Neutrophil.APOPTOTIC or this.status == Neutrophil.DEAD:
            	interac.inc(this.getIronPool(), x, y, z);
                this.incIronPool(-this.getIronPool());
            }
            return false;
        }
            
        ////if itype is ROS and interactable.state == Neutrophil.INTERACTING:
        ////    if this.status == Neutrophil.ACTIVE:
        ////        interactable.inc(0)
        ////    return Tru
        return interactable.interact(this, x, y, z);
    }

    public void die() {
        if(this.getStatus() != Neutrophil.DEAD) { 
            this.setStatus(Neutrophil.DEAD);
            Neutrophil.totalCells = Neutrophil.totalCells - 1;
        }
    }

    public String attractedBy() {
        return Neutrophil.chemokine;
    }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public int getMaxConidia() {
		return Constants.N_MAX_CONIDIA;
	}
}
