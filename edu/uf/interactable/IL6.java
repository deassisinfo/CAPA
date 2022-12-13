package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IL6 extends Molecule{
    

	public static final String NAME = "IL6";
	public static final int NUM_STATES = 1;
	
	private static IL6 molecule = null;
    
    private IL6(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
		// TODO Auto-generated constructor stub
	}
    
    public static IL6 getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new IL6(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
    }

    public void degrade(int x, int y, int z) {
        degrade(Constants.IL6_HALF_LIFE, x, y, z);
        degrade(Util.turnoverRate(1.0, 0.0, Constants.TURNOVER_RATE, Constants.REL_CYT_BIND_UNIT_T, 1.0), x, y, z);
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
    			this.negativeInteractList.add(IL6.NAME);
    			this.negativeInteractList.add(Iron.NAME);
    			this.negativeInteractList.add(Transferrin.NAME);
    			this.negativeInteractList.add(TAFC.NAME);
    			this.negativeInteractList.add(Lactoferrin.NAME);
    			this.negativeInteractList.add(Afumigatus.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) {
            Macrophage macro = (Macrophage) interactable;
        	if (macro.getStatus() == Phagocyte.ACTIVE)//# and interactable.state == Neutrophil.INTERACTING:
                this.inc(Constants.MA_IL6_QTTY, 0, x, y, z);
            return true; 
        }
        if (interactable instanceof Neutrophil) {
        	Neutrophil netro = (Neutrophil) interactable; 
        	if (netro.getStatus() == Phagocyte.ACTIVE)//# and interactable.state == Neutrophil.INTERACTING:
                this.inc(Constants.N_IL6_QTTY, 0, x, y, z);
            return true;
        }
        return interactable.interact(this, x, y, z);
    }

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public double getThreshold() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int getNumState() {
		return NUM_STATES;
	}

}
