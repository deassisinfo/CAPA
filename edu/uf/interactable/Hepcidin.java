package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Hepcidin extends Molecule{

	public static final String NAME = "Hepcidin";
	public static final int NUM_STATES = 1;
	
	private static Hepcidin molecule = null;    
    
    private Hepcidin(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Hepcidin getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Hepcidin(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
    }

    public void degrade(int x, int y, int z) {}

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
    			this.negativeInteractList.add(Hepcidin.NAME);
    			this.negativeInteractList.add(IL10.NAME);
    			this.negativeInteractList.add(Iron.NAME);
    			this.negativeInteractList.add(TAFC.NAME);
    			this.negativeInteractList.add(IL6.NAME);
    			this.negativeInteractList.add(TNFa.NAME);
    			this.negativeInteractList.add(Neutrophil.NAME);
    			this.negativeInteractList.add(Pneumocyte.NAME);
    			this.negativeInteractList.add(Afumigatus.NAME);
    			this.negativeInteractList.add(Transferrin.NAME);
    			this.negativeInteractList.add(Lactoferrin.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) {
        	Macrophage macro = (Macrophage) interactable;
        	if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_Hep, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
        		macro.setFpn(false);
        		macro.setFpnIteration(0); 
        	}
            return true; 
        }
        return interactable.interact(this, x, y, z); 
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
