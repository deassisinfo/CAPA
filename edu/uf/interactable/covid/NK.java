package edu.uf.interactable.covid;

import java.util.HashSet;
import java.util.Set;

import edu.uf.geometry.Voxel;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Erythrocyte;
import edu.uf.interactable.Haptoglobin;
import edu.uf.interactable.Heme;
import edu.uf.interactable.Hemopexin;
import edu.uf.interactable.Hepcidin;
import edu.uf.interactable.IL1;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Iron;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.Liver;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.Phagocyte;
import edu.uf.interactable.PositionalAgent;
import edu.uf.interactable.TAFC;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Transferrin;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class NK extends Cell{
	
	public static final String NAME = "NK";
	
	private static int totalCells = 0;
    
    private int maxMoveStep;
    //private boolean engaged;

    public NK() {
        NK.totalCells = NK.totalCells + 1;
        //this.boolean_network = Macrophage.InitMacrophageBooleanState.clone();
        this.setStatus(Macrophage.RESTING);
        this.maxMoveStep = -1; 
    }
    
    public static int getTotalCells() {
		return totalCells;
	}

	@Override
	public void processBooleanNetwork() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateStatus() {
		if(Rand.getRand().randunif() < Constants.MA_HALF_LIFE)
            this.die();
		this.maxMoveStep = -1;
	}

	@Override
	public boolean isDead() {
		return this.getStatus() == Macrophage.DEAD;
	}

	@Override
	public void move(Voxel oldVoxel, int steps) {
		if(steps < this.getMaxMoveSteps()) {
        	
        	Util.calcDriftProbability(oldVoxel, this);
            Voxel newVoxel = Util.getVoxel(oldVoxel, Rand.getRand().randunif());
        	
            oldVoxel.removeCell(this.getId());
            //System.out.println(oldVoxel + " " + newVoxel);
            newVoxel.setCell(this);
            steps = steps + 1;
            move(newVoxel, steps);
        }
	}

	@Override
	public void die() {
		if(this.getStatus() != Macrophage.DEAD) {
            this.setStatus(Macrophage.DEAD);
            NK.totalCells = NK.totalCells - 1;
        }
	}

	@Override
	public void incIronPool(double ironPool) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Set<String> getNegativeInteractionList() {
		if(this.negativeInteractList == null) {
    		synchronized(this) {
    			this.negativeInteractList = new HashSet<>();
    			this.negativeInteractList.add(Transferrin.NAME);
    			this.negativeInteractList.add(Lactoferrin.NAME);
    			this.negativeInteractList.add(TAFC.NAME);
    			this.negativeInteractList.add(IL1.NAME);
    			this.negativeInteractList.add(IL6.NAME);
    			this.negativeInteractList.add(IL10.NAME);
    			this.negativeInteractList.add(TNFa.NAME);
    			this.negativeInteractList.add(TGFb.NAME);
    			this.negativeInteractList.add(MIP2.NAME);
    			this.negativeInteractList.add(MIP1B.NAME);
    			this.negativeInteractList.add(Iron.NAME);
    			this.negativeInteractList.add(Hepcidin.NAME);
    			this.negativeInteractList.add(Liver.NAME);
    			this.negativeInteractList.add(SarsCoV2.NAME);
    			this.negativeInteractList.add(Hemopexin.NAME);
    			this.negativeInteractList.add(Heme.NAME);
    			this.negativeInteractList.add(Erythrocyte.NAME);
    			this.negativeInteractList.add(Haptoglobin.NAME);
    			this.negativeInteractList.add(Neutrophil.NAME);
    			this.negativeInteractList.add(Macrophage.NAME);
    			this.negativeInteractList.add(NK.NAME);
    			this.negativeInteractList.add(IFN1.NAME);
    		}
    	}
    	return this.negativeInteractList;
	}

	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if (interactable instanceof Pneumocyte){
        	Pneumocyte cell = (Pneumocyte) interactable;
        	if(!(cell.getStatus() == Phagocyte.APOPTOTIC || cell.getStatus() == Phagocyte.NECROTIC || cell.getStatus() == Phagocyte.DEAD)) 
        		if(Constants.PR_INT_NK_P > Rand.getRand().randunif()) 
        			cell.setStatus(Phagocyte.APOPTOTIC);
        	return true; 
        }
        //System.out.println(interactable);
        return interactable.interact(this, x, y, z);
	}

	@Override
	public int getMaxMoveSteps() {
		if(this.maxMoveStep == -1) {
			this.maxMoveStep = Rand.getRand().randpois(Constants.MA_MOVE_RATE_ACT); //TODO CHANGE TO NK
		}
		return this.maxMoveStep;
	}

}
