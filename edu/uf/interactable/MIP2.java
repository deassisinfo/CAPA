package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class MIP2 extends Chemokine{
    public static final String NAME = "MIP2";
    public static final int NUM_STATES = 1;
    
    private static MIP2 molecule = null;
    

    private MIP2(double[][][][] qttys, Diffuse diffuse) {
    	super(qttys, diffuse);
        Neutrophil.setChemokine(MIP2.NAME);
    }

    public static MIP2 getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new MIP2(values, diffuse);
    	}
    	return molecule;
    }
    
    public static MIP2 getMolecule() {
    	return molecule;
    }
    
    public void degrade(int x, int y, int z) {
        degrade(Constants.MIP2_HALF_LIFE, x, y, z);
        degrade(Util.turnoverRate(1, 0, Constants.TURNOVER_RATE, Constants.REL_CYT_BIND_UNIT_T, 1), x, y, z);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.values[0][x][y][z];
    }

    public int getIndex(String str) {
        return 0;
    }
    
    public Set<String> getNegativeInteractionList(){
    	
    	if(this.negativeInteractList == null) {
    		synchronized(this) {
    			this.negativeInteractList = new HashSet<>();
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
    			this.negativeInteractList.add(TGFb.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Neutrophil) {
            Neutrophil neutro = (Neutrophil) interactable;
        	if (neutro.getStatus() == Phagocyte.RESTING) {
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_MIP2, Constants.STD_UNIT_T) > Rand.getRand().randunif()) 
                	neutro.setStatus(Phagocyte.ACTIVATING);
        	}else if (neutro.isTnfa()){//#interactable.status == Phagocyte.ACTIVE and interactable.state == Neutrophil.INTERACTING:
                this.inc(Constants.N_MIP2_QTTY, 0, x, y, z);
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_MIP2, Constants.STD_UNIT_T) > Rand.getRand().randunif()){}
                    //neutro.interaction = 0
            //#if Util.activation_function(this.values[0], Constants.Kd_MIP2) > random():
            //#    this.pdec(0.5)
        	}
            return true;
        }
        if (interactable instanceof Pneumocyte) {
            if (((Pneumocyte) interactable).isTnfa())//#interactable.status == Phagocyte.ACTIVE:
                this.inc(Constants.P_MIP2_QTTY, 0, x, y, z);
            return true; 
        }
        //#if type(interactable) is Hepatocytes:
        //#    return False
        if (interactable instanceof Macrophage) {
        	Macrophage macro = (Macrophage) interactable;
        	if (macro.isTnfa()) //#interactable.status == Phagocyte.ACTIVE:# and interactable.state == Neutrophil.INTERACTING:
                this.inc(Constants.MA_MIP2_QTTY, 0, x, y, z);
            return true;
        }
        return interactable.interact(this, x, y, z); 
    }

    public double chemoatract(int x, int y, int z) {
        return Util.activationFunction(this.values[0][x][y][z], Constants.Kd_MIP2, Constants.STD_UNIT_T) + Constants.DRIFT_BIAS;
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
