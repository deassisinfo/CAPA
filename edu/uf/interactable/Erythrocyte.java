package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.geometry.Voxel;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;

public class Erythrocyte extends Cell{
	
	public static final String NAME = "Erythrocyte";
	private boolean hemorrhage;
	
	private static int totalCells = 0;
	
	
	private int restingRBC;
	private int burstRBC;
	
	public static final int RESTING = 0;
	public static final int HEMORRHAGE = 1;
	public static final int BURSTING = 2;
	public static final int DEAD = 3;
	
	public Erythrocyte(int rbc) {
		super();
		this.restingRBC = rbc;
		this.hemorrhage = false; 
		totalCells += rbc;
	}
	
	public static int getTotalCells() {
		return totalCells;
	}
	
	public int getBurst() {
		return this.burstRBC;
	}
	
	public int getResting() {
		return this.restingRBC;
	}
	
	public void setBurst(int burst) {
		this.burstRBC = burst;
	}
	
	public  void setHemorrhage(boolean hemorrhage) {
		this.hemorrhage = hemorrhage;
	}

	@Override
	public Set<String> getNegativeInteractionList() {
		if(this.negativeInteractList == null) {
    		synchronized(this) {
    			this.negativeInteractList = new HashSet<>();
    			this.negativeInteractList.add(Transferrin.NAME);
    			this.negativeInteractList.add(Lactoferrin.NAME);
    			this.negativeInteractList.add(TAFC.NAME);
    			this.negativeInteractList.add(IL6.NAME);
    			this.negativeInteractList.add(IL10.NAME);
    			this.negativeInteractList.add(TNFa.NAME);
    			this.negativeInteractList.add(TGFb.NAME);
    			this.negativeInteractList.add(MIP2.NAME);
    			this.negativeInteractList.add(MIP1B.NAME);
    			this.negativeInteractList.add(Iron.NAME);
    			this.negativeInteractList.add(Hepcidin.NAME);
    			this.negativeInteractList.add(Liver.NAME);
    			this.negativeInteractList.add(Macrophage.NAME);
    			this.negativeInteractList.add(Neutrophil.NAME);
    			this.negativeInteractList.add(Pneumocyte.NAME);
    			this.negativeInteractList.add(Erythrocyte.NAME);
    			
    		}
    	}
    	return this.negativeInteractList;
	}

	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if(interactable instanceof Afumigatus) {
			if(((Afumigatus) interactable).getStatus() == Afumigatus.HYPHAE) 
				this.hemorrhage = true;
			return true;
		}
		return interactable.interact(this, x, y, z);
	}

	@Override
	public void processBooleanNetwork() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateStatus() {
		if(this.hemorrhage) {
			//turnover = turnover_rate;
			double burst = Rand.getRand().randpois(Constants.HEMOLYSIS_RATE * this.restingRBC);
			//this.restingRBC -= burst; //+turnover;
			this.burstRBC += burst; // -turnover;
			//totalCells -= burst;
		}
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void move(Voxel oldVoxel, int steps) {
		// TODO Auto-generated method stub
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
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
	public int getMaxMoveSteps() {
		// TODO Auto-generated method stub
		return -1;
	}

}
