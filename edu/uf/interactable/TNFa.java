package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class TNFa extends Molecule{

	public static final String NAME = "TNFa";
	public static final int NUM_STATES = 1;
	
	private static TNFa molecule = null;
    
    private TNFa(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static TNFa getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new TNFa(values, diffuse);
    	}
    	return molecule;
    }
    
    public static TNFa getMolecule() {
    	return molecule;
    }
    
    public void degrade(int x, int y, int z) {
        degrade(Constants.TNF_HALF_LIFE, x, y, z);
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
    			
    			this.negativeInteractList.add(Iron.NAME);
    			this.negativeInteractList.add(Transferrin.NAME);
    			this.negativeInteractList.add(TAFC.NAME);
    			this.negativeInteractList.add(Lactoferrin.NAME);
    			this.negativeInteractList.add(Afumigatus.NAME);
    			this.negativeInteractList.add(IL6.NAME);
    		
    			this.negativeInteractList.add(TNFa.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) {
            Macrophage macro = (Macrophage) interactable;
        	if (macro.getStatus() == Phagocyte.ACTIVE)//# and interactable.state == Neutrophil.INTERACTING:
                this.inc(Constants.MA_TNF_QTTY, 0, x, y, z);
            if (macro.getStatus() == Phagocyte.RESTING || macro.getStatus() == Phagocyte.ACTIVE) {
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_TNF, Constants.STD_UNIT_T) > Rand.getRand().randunif()){
                	macro.setStatus(macro.getStatus() == Phagocyte.RESTING ? Phagocyte.ACTIVATING : Phagocyte.ACTIVE);
                	//macro.iteration = 0;
                	macro.setTnfa(true);
                }
            }
            return true;
        }
        if (interactable instanceof Neutrophil) {
            Neutrophil neutro = (Neutrophil) interactable;
        	if (neutro.getStatus() == Phagocyte.ACTIVE) //# and interactable.state == Neutrophil.INTERACTING:
                this.inc(Constants.N_TNF_QTTY, 0, x, y, z);
            if (neutro.getStatus() == Phagocyte.RESTING || neutro.getStatus() == Phagocyte.ACTIVE) {
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_TNF, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
                	neutro.setStatus(neutro.getStatus() == Phagocyte.RESTING ? Phagocyte.ACTIVATING : Phagocyte.ACTIVE);
                	//neutro.iteration = 0;
                	neutro.setTnfa(true); 
                }
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
		return 0;
	}

	@Override
	public int getNumState() {
		return NUM_STATES;
	}
}
