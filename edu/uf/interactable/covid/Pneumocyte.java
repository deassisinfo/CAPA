package edu.uf.interactable.covid;

import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Phagocyte;
import edu.uf.utils.Constants;

public class Pneumocyte extends edu.uf.interactable.Pneumocyte{

	public static final double EPS = 1e-16;
	
	private double viralLoad = 0;
	private double viralReplicationRate = Constants.DEFAULT_VIRAL_REPLICATION_RATE;
	private int iterations = 0;

	public double getViralLoad() {
		return viralLoad;
	}

	public void setViralLoad(double viralLoad) {
		this.viralLoad = viralLoad;
	}

	public double getViralReplicationRate() {
		return viralReplicationRate;
	}

	public void setViralReplicationRate(double viralReplicationRate) {
		this.viralReplicationRate = viralReplicationRate;
	}
	
	public void updateStatus() {
		if(this.getStatus() == Macrophage.DEAD)
            return;
		
		super.updateStatus();
		
		this.viralLoad *= this.viralReplicationRate;
		
		if((this.viralReplicationRate - Constants.DEFAULT_VIRAL_REPLICATION_RATE) < EPS) {
            if(this.getStatusIteration() >= Constants.ITER_TO_CHANGE_STATE) {
                this.iterations = 0;
                this.viralReplicationRate = Constants.DEFAULT_VIRAL_REPLICATION_RATE;
            }else
                this.iterations += 1;
		}
		
		
        if(this.getStatus() == Macrophage.NECROTIC) 
            this.die();
        else if(this.getStatus() == Macrophage.APOPTOTIC) 
        	this.die();
        else if(this.viralLoad > Constants.MAX_VIRAL_LOAD) {
			this.setStatus(Phagocyte.NECROTIC);
		}
		
	}
}
