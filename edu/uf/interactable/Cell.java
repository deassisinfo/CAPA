package edu.uf.interactable;

import edu.uf.geometry.Voxel;

public abstract class Cell extends Interactable{
	
	private int id;
	
    private int bnIteration;
    
    private double ironPool;
    private int statusIteration;
    private int stateIteration;
    private int state;
    private int status;
    private boolean engulfed;
    protected int[] booleanNetwork;
	
	public int getBnIteration() {
		return bnIteration; 
	}


	public void setBnIteration(int bnIteration) {
		this.bnIteration = bnIteration;
	}


	public double getIronPool() {
		return ironPool;
	}


	public void setIronPool(double ironPool) {
		this.ironPool = ironPool;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}
	
	
	public int getStatusIteration() {
		return statusIteration;
	}

	public void setStatusIteration(int statusIteration) {
		this.statusIteration = statusIteration;
	}

	public int getStateIteration() {
		return stateIteration;
	}

	public void setStateIteration(int stateIteration) {
		this.stateIteration = stateIteration;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public boolean isEngulfed() {
		return engulfed;
	}


	public void setEngulfed(boolean engulfed) {
		this.engulfed = engulfed;
	}


	public int getBooleanNetwork(int index) {
		return booleanNetwork[index];
	}


	public void setBooleanNetwork(int index, int value) {
		this.booleanNetwork[index] = value;
	}
	
	
	public void setBooleanNetwork(int[] booleanNetwork) {
		this.booleanNetwork = booleanNetwork;
	}
	
	
	public int getId() {
    	return id;
    }


	public void setId(int id) {
		this.id = id;
	}


	public Cell() {
		this.id = Id.getId(); 
	}


    public abstract void processBooleanNetwork();

    public abstract void updateStatus();

    public abstract boolean isDead();

    public abstract void move(Voxel oldVoxel, int steps);

    public abstract void die();
    
    public abstract void incIronPool(double ironPool);
    
    public abstract int getMaxMoveSteps();

    public String attractedBy() {
        return null;
    }
	
}
