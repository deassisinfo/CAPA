package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class TGFb extends Molecule{

	public static final String NAME = "TGFb";
	public static final int NUM_STATES = 1;
	
	private static TGFb molecule = null;
    

    private TGFb(double[][][][] qtty, Diffuse diffuse) {
		super(qtty, diffuse);
	}
    
    public static TGFb getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new TGFb(values, diffuse);
    	}
    	return molecule;
    }
    
    public static TGFb getMolecule() {
    	return molecule;
    }
    
    public void  degrade(int x, int y, int z) {
        degrade(Constants.TGF_HALF_LIFE, x, y, z);
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
    			this.negativeInteractList.add(Pneumocyte.NAME);
    		}
    		
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) {
            Macrophage macro = (Macrophage) interactable;
        	if (macro.getStatus() == Phagocyte.INACTIVE) {
                this.inc(Constants.MA_TGF_QTTY, 0, x, y, z);
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_TGF, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {}
                	//macro.iteration = 0;
        	}else if (macro.getStatus() != Phagocyte.APOPTOTIC && macro.getStatus() != Phagocyte.NECROTIC && 
        			macro.getStatus() != Phagocyte.DEAD) {
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_TGF, Constants.STD_UNIT_T) > Rand.getRand().randunif()) 
                	macro.setStatus(Phagocyte.INACTIVATING);
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
