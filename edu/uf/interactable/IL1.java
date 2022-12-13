package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class IL1 extends Molecule{
	public static final String NAME = "IL1";
	public static final int NUM_STATES = 1;
	
	private static IL1 molecule = null;   
	
	public boolean hasInteractWithLiver = false;
    
    private IL1(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static IL1 getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new IL1(values, diffuse);
    	}
    	return molecule;
    }
    
    public static IL1 getMolecule() {
    	return molecule;
    }

    public void degrade(int x, int y, int z) {
        degrade(Constants.IL1_HALF_LIFE, x, y, z);
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
    			//this.negativeInteractList.add(Liver.NAME);
    			//this.negativeInteractList.add(Macrophage.NAME);
    			//this.negativeInteractList.add(Neutrophil.NAME);
    			//this.negativeInteractList.add(Pneumocyte.NAME);
    			this.negativeInteractList.add(Erythrocyte.NAME);
    			this.negativeInteractList.add(Afumigatus.NAME);
    			this.negativeInteractList.add(Heme.NAME);
    			this.negativeInteractList.add(Hemoglobin.NAME);
    			this.negativeInteractList.add(Haptoglobin.NAME);
    			this.negativeInteractList.add(Hemopexin.NAME);
    			this.negativeInteractList.add(IL1.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if (interactable instanceof Macrophage) {
            Macrophage macro = (Macrophage) interactable;
        	if (macro.getStatus() == Phagocyte.ACTIVE)//# and interactable.state == Neutrophil.INTERACTING:
                this.inc(Constants.MA_IL1_QTTY, 0, x, y, z);
            if (macro.getStatus() == Phagocyte.RESTING || macro.getStatus() == Phagocyte.ACTIVE) {
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_IL1, Constants.STD_UNIT_T) > Rand.getRand().randunif()){
                	macro.setStatus(macro.getStatus() == Phagocyte.RESTING ? Phagocyte.ACTIVATING : Phagocyte.ACTIVE);
                	//macro.iteration = 0;
                }
            }
            return true;
        }
    	if (interactable instanceof Pneumocyte) {
    		Pneumocyte pneumo = (Pneumocyte) interactable;
        	if (pneumo.getStatus() == Phagocyte.ACTIVE)//# and interactable.state == Neutrophil.INTERACTING:
                this.inc(Constants.MA_IL1_QTTY, 0, x, y, z);
            if (pneumo.getStatus() == Phagocyte.RESTING || pneumo.getStatus() == Phagocyte.ACTIVE) {
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_IL1, Constants.STD_UNIT_T) > Rand.getRand().randunif()){
                	pneumo.setStatus(pneumo.getStatus() == Phagocyte.RESTING ? Phagocyte.ACTIVATING : Phagocyte.ACTIVE);
                	//pneumo.iteration = 0;
                }
            }
            return true;
        }
        if (interactable instanceof Neutrophil) {
            Neutrophil neutro = (Neutrophil) interactable;
        	if (neutro.getStatus() == Phagocyte.ACTIVE) //# and interactable.state == Neutrophil.INTERACTING:
                this.inc(Constants.N_IL1_QTTY, 0, x, y, z);
            if (neutro.getStatus() == Phagocyte.RESTING || neutro.getStatus() == Phagocyte.ACTIVE) {
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_IL1, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
                	neutro.setStatus(neutro.getStatus() == Phagocyte.RESTING ? Phagocyte.ACTIVATING : Phagocyte.ACTIVE);
                	//neutro.iteration = 0;
                	neutro.setTnfa(true); 
                }
            }
            return true;
        }
        if(interactable instanceof Liver) {
        	if(hasInteractWithLiver)return true;
        	Liver liver = (Liver) interactable; 
        	for(int k = 0; k < Liver.ENSEMBLE_SIZE; k++) {
        		double globalQtty = this.getTotalMolecule(0)/(2*Constants.SPACE_VOL);
        		if (Util.activationFunction(globalQtty, Constants.Kd_IL1, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
        			liver.getBooleanNetwork()[k][Liver.IL1R] = 1;
        		}else {
        			liver.getBooleanNetwork()[k][Liver.IL1R] = 0;
        		}
        	}
        }
        hasInteractWithLiver = true; 
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

	@Override
	public void resetCount() {
		super.resetCount();
		hasInteractWithLiver = false;
	}
	
}
