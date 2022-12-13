package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class MIP1B extends Chemokine{
    public static final String NAME = "MIP1B";
    public static final int NUM_STATES = 1;
    
    private static MIP1B molecule = null;    

    private MIP1B(double[][][][] qttys, Diffuse diffuse) {
        super(qttys, diffuse);
        Macrophage.setChemokine(MIP1B.NAME);
    }
    
    public static MIP1B getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new MIP1B(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static MIP1B getMolecule() {
    	return molecule;
    }

    public void degrade(int x, int y, int z) {
        degrade(Constants.MIP1B_HALF_LIFE, x, y, z);
        degrade(Util.turnoverRate(1, 0, Constants.TURNOVER_RATE, Constants.REL_CYT_BIND_UNIT_T, 1), x, y, z);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.values[0][x][y][z];
    }
    
    public Set<String> getNegativeInteractionList(){
    	
    	if(this.negativeInteractList == null) {
    		synchronized(this) {
    			this.negativeInteractList = new HashSet<>();
    			this.negativeInteractList.add(TGFb.NAME);
    			this.negativeInteractList.add(MIP1B.NAME);
    			this.negativeInteractList.add(MIP2.NAME);
    			this.negativeInteractList.add(Hepcidin.NAME);
    			this.negativeInteractList.add(IL10.NAME);
    			this.negativeInteractList.add(Iron.NAME);
    			this.negativeInteractList.add(Transferrin.NAME);
    			this.negativeInteractList.add(TAFC.NAME);
    			this.negativeInteractList.add(Lactoferrin.NAME);
    			this.negativeInteractList.add(Afumigatus.NAME);
    			this.negativeInteractList.add(IL6.NAME);
    			this.negativeInteractList.add(TNFa.NAME);
    			this.negativeInteractList.add(Neutrophil.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Pneumocyte) {
            if (((Pneumocyte)interactable).isTnfa())//#interactable.status == Phagocyte.ACTIVE:
                this.inc(Constants.P_MIP1B_QTTY, 0, x, y, z);
            return true;
        }
        if (interactable instanceof Macrophage) {
            if (((Macrophage)interactable).isTnfa())//#interactable.status == Phagocyte.ACTIVE:# and interactable.state == Neutrophil.INTERACTING:
                this.inc(Constants.MA_MIP1B_QTTY, 0, x, y, z);
            return true;
        }
        return interactable.interact(this, x, y, z); 
    }

    public double chemoatract(int x, int y, int z) {
    	//System.out.println("MIP: " + this.values[0][x][y][z]);
        return Util.activationFunction(this.values[0][x][y][z], Constants.Kd_MIP1B, Constants.STD_UNIT_T) + Constants.DRIFT_BIAS;
    }

	@Override
	public String getName() {
		return NAME;
	} 

	@Override
	public double getThreshold() {
		return -1;
	}

	@Override
	public int getNumState() {
		return NUM_STATES;
	}
    
}
