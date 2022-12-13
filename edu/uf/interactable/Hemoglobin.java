package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Hemoglobin extends Molecule{

	public static final String NAME = "Hemoglobin";
	public static final int NUM_STATES = 1;
	
	private static Hemoglobin molecule = null;    
    
    private Hemoglobin(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Hemoglobin getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Hemoglobin(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Hemoglobin getMolecule() {
    	return molecule;
    }

    public void degrade(int x, int y, int z) {
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
    			//this.negativeInteractList.add(Erythrocyte.NAME);
    			this.negativeInteractList.add(Afumigatus.NAME);
    			this.negativeInteractList.add(Hemoglobin.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        /*if (interactable instanceof Liver){//# or type(interactable) is Neutrophil: 
        	Liver.getLiver().setHemoglobin(Liver.getLiver().getHemoglobin() + this.values[0][x][y][z]/2.0); //BASED ON IL6 ...
            return true;
        }*/
        if(interactable instanceof Erythrocyte) {
        	Erythrocyte erythrocyte = (Erythrocyte) interactable;
        	this.values[0][x][y][z] += erythrocyte.getBurst() * Constants.ERYTROCYTE_HEMOGLOBIN_CONCENTRATION;
        	erythrocyte.setBurst(0);
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
