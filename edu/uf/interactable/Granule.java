package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Granule extends Molecule{
    
	public static final String NAME = "IL10";
	public static final int NUM_STATES = 1;
	
	private static Granule molecule = null;    
    
    private Granule(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Granule getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Granule(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Granule getMolecule() {
    	return molecule;
    }

    public void degrade(int x, int y, int z) {
        degrade(Constants.GRANULE_HALF_LIFE, x, y, z);
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
    			this.negativeInteractList.add(IL1.NAME);
    			this.negativeInteractList.add(IL6.NAME);
    			this.negativeInteractList.add(IL10.NAME);
    			this.negativeInteractList.add(Iron.NAME);
    			this.negativeInteractList.add(Transferrin.NAME);
    			this.negativeInteractList.add(TAFC.NAME);
    			this.negativeInteractList.add(Lactoferrin.NAME);
    			this.negativeInteractList.add(Heme.NAME);
    			this.negativeInteractList.add(Hepcidin.NAME);
    			this.negativeInteractList.add(Haptoglobin.NAME);
    			this.negativeInteractList.add(Hemopexin.NAME);
    			this.negativeInteractList.add(Hemoglobin.NAME);
    			//this.negativeInteractList.add(Afumigatus.NAME);
    			this.negativeInteractList.add(TNFa.NAME);
    			this.negativeInteractList.add(TGFb.NAME);
    			this.negativeInteractList.add(MIP1B.NAME);
    			this.negativeInteractList.add(MIP2.NAME);
    			this.negativeInteractList.add(Macrophage.NAME);
    			this.negativeInteractList.add(Pneumocyte.NAME);
    			this.negativeInteractList.add(Erythrocyte.NAME);
    			this.negativeInteractList.add(Granule.NAME);
    			this.negativeInteractList.add(Liver.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if (interactable instanceof Neutrophil){//# or type(interactable) is Neutrophil: 
        	Neutrophil neutrophil = (Neutrophil) interactable;
        	if(neutrophil.getStatus() == Neutrophil.ACTIVE) {
        	//if((neutrophil.getStatus() == Macrophage.ACTIVE)){ // || Rand.getRand().getRand().randunif() < Constants.PR_DEPLETION) && !neutrophil.depleted){
        		this.inc(Constants.GRANULE_QTTY, 0, x, y, z);
        		//neutrophil.depleted = true;
        	}
            
            return true; 
        }
        
        if(interactable instanceof Afumigatus) {
        	Afumigatus af = (Afumigatus) interactable;
        	if(Util.activationFunction(this.get(0, x, y, z), Constants.Kd_Granule, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
        		af.die();
        	}
        	return true;
        }
        //System.out.println(interactable);
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