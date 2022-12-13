package edu.uf.interactable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Haptoglobin extends Molecule{

	public static final String NAME = "Haptoglobin";
	public static final int NUM_STATES = 2;
	
	//private static double xSystem = 0.0;
	
	private static Haptoglobin molecule = null;  
	
	private static final Map<String, Integer> INDEXES;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("Hp", 0); 
    	INDEXES.put("HpHb", 1); 
    }
    
    private Haptoglobin(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Haptoglobin getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Haptoglobin(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Haptoglobin getMolecule() {
    	return molecule;
    }

    public void degrade(int x, int y, int z) {} //Hp turnover done at interaction!

    public int getIndex(String str) {
        return Haptoglobin.INDEXES.get(str);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.values[0][x][y][z];
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.values[1][x][y][z];
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
    			this.negativeInteractList.add(Neutrophil.NAME);
    			this.negativeInteractList.add(Pneumocyte.NAME);
    			this.negativeInteractList.add(Erythrocyte.NAME);
    			this.negativeInteractList.add(Afumigatus.NAME);
    			this.negativeInteractList.add(Haptoglobin.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if(interactable instanceof Liver) {
    		Liver liver = (Liver) interactable;
    		double hpQtty = liver.getBooleanEnsemble()[Liver.HP]/((double)Liver.ENSEMBLE_SIZE);
        	//if(hpQtty < Liver.LIVER_BN_EPS)return true;
        	
        	hpQtty = hpQtty * Constants.L_HP_QTTY + (1 - hpQtty) * Constants.L_REST_HP_QTTY;
        	
        	double rate = Util.turnoverRate(this.values[0][x][y][z], hpQtty * Constants.VOXEL_VOL) - 1; //HEPCIDIN MODEL
        	
        	this.pinc(rate, x, y, z);
        	
    		//Util.turnoverRate(this.values[0][x][y][z], rate);
    		return true; 
    	}
        if (interactable instanceof Macrophage){//# or type(interactable) is Neutrophil: 
        	if(Util.activationFunction(this.values[1][x][y][z], Constants.Kd_HP, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
        		((Macrophage)interactable).setStatus(Macrophage.ANERGIC);
        	}
            return true;
        }
        if(interactable instanceof Hemoglobin) {
        	Hemoglobin hb = (Hemoglobin) interactable;
        	double hphb = Util.michaelianKinetics(hb.get(0,x,y,z), this.values[0][x][y][z], Constants.KM_HP, Constants.STD_UNIT_T);
        	this.dec(hphb, 0, x, y, z);
        	this.inc(hphb, 1, x, y, z);
        	hb.dec(hphb, x, y, z);
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
