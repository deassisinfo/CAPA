package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.geometry.Voxel;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Pneumocyte extends Cell {
    public static final String NAME = "Pneumocyte";

    private static int totalCells = 0;
    
    private int iteration;
    private boolean tnfa;

    public Pneumocyte() {
        super();
        this.setStatus(Phagocyte.RESTING);
        this.iteration = 0;
        this.tnfa = false;
        Pneumocyte.totalCells = Pneumocyte.totalCells + 1;
    }

    public static int getTotalCells() {
		return totalCells;
	}

	public static void setTotalCells(int totalCells) {
		Pneumocyte.totalCells = totalCells;
	}

	public int getIteration() {
		return iteration;
	}

	public boolean isTnfa() {
		return tnfa; 
	}

    
    public void die() {
        if(this.getStatus() != Phagocyte.DEAD) {
            this.setStatus(Neutrophil.DEAD);  //##CAUTION!!!
            Pneumocyte.totalCells = Pneumocyte.totalCells - 1;
        }
    }
    
    public Set<String> getNegativeInteractionList(){
    	
    	if(this.negativeInteractList == null) {
    		synchronized(this) {
    			this.negativeInteractList = new HashSet<>();
    			this.negativeInteractList.add(IL10.NAME);
    			this.negativeInteractList.add(Iron.NAME);
    			this.negativeInteractList.add(Transferrin.NAME);
    			this.negativeInteractList.add(TAFC.NAME);
    			this.negativeInteractList.add(Lactoferrin.NAME);
    			this.negativeInteractList.add(Macrophage.NAME);
    			this.negativeInteractList.add(Neutrophil.NAME);
    			this.negativeInteractList.add(Pneumocyte.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Afumigatus) {
            Afumigatus interac = (Afumigatus) interactable;
        	if(this.getStatus() != Phagocyte.APOPTOTIC && this.getStatus() != Phagocyte.NECROTIC && this.getStatus() != Phagocyte.DEAD) {
                if(interac.getStatus() != Afumigatus.RESTING_CONIDIA) {
                    if(this.getStatus() != Phagocyte.ACTIVE) {
                        if(Rand.getRand().randunif() < Constants.PR_P_INT) {
                            this.setStatus(Phagocyte.ACTIVATING);
                        }
                    }else {
                    	//System.out.println("reset count ...");
                        this.iteration = 0;
                    }
                }
        	}
            return true;
        }
        
        if (interactable instanceof IL6) { 
            if (this.getStatus() == Phagocyte.ACTIVE)
                ((Molecule)interactable).inc(Constants.P_IL6_QTTY, 0, x, y, z);
            return true;
        }
        
        if (interactable instanceof TNFa) {
            Molecule interact = (Molecule) interactable;
        	if (Rand.getRand().randunif() < Util.activationFunction(interact.values[0][x][y][z], Constants.Kd_TNF, Constants.UNIT_T)) {
                if(this.getStatus() == Phagocyte.ACTIVE) {
                    this.iteration = 0;
                    this.tnfa = true;
                }
        	}
            if(this.getStatus() == Phagocyte.ACTIVE) {
            	interact.inc(Constants.P_TNF_QTTY, 0, x, y, z);
            }
            return true;
        }
		
        return interactable.interact(this, x, y, z);
    }

    public void processBooleanNetwork() {}

    public boolean isDead() {
        return this.getStatus() == Phagocyte.DEAD;
    }

    public void incIronPool(double qtty) {}

    public void updateStatus() {
        if (this.getStatus() == Phagocyte.ACTIVE) {
            if (this.iteration > Constants.ITER_TO_REST) {//#30*6:
                this.iteration = 0;
                this.setStatus(Phagocyte.RESTING);
                this.tnfa = false;
            }
            this.iteration = this.iteration + 1;
            //System.out.println(this.iteration + "  " + Constants.ITER_TO_REST);
        }else if (this.getStatus() == Phagocyte.ACTIVATING) {
            if (this.iteration > Constants.ITER_TO_CHANGE_STATE) {
                this.iteration = 0;
                this.setStatus(Phagocyte.ACTIVE);
            }
            this.iteration = this.iteration + 1;
        }
    }
            
    public void move(Voxel oldVoxel, int steps) {}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public int getMaxMoveSteps() {
		// TODO Auto-generated method stub
		return -1;
	}
}