package edu.uf.interactable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.invitro.Invitro;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Heme extends Molecule{

	public static final String NAME = "Heme";
	public static final int NUM_STATES = 1;
	
	//private static double xSystem = 0.0;
	
	private static Heme molecule = null;  
  
    
    private Heme(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Heme getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Heme(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Heme getMolecule() {
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
    			//this.negativeInteractList.add(Macrophage.NAME);
    			this.negativeInteractList.add(Neutrophil.NAME);
    			this.negativeInteractList.add(Pneumocyte.NAME);
    			this.negativeInteractList.add(Erythrocyte.NAME);
    			this.negativeInteractList.add(Haptoglobin.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if(interactable instanceof Invitro)return interactable.interact(this, x, y, z);
    	/*if(interactable instanceof Liver) {
    		Liver.getLiver().setHeme(Liver.getLiver().getHeme() + this.values[0][x][y][z]/2.0); //BASED ON IL6 ...
            return true;
    	}*/
    	if(interactable instanceof Heme) {
    		if(this.values[1][x][y][z] > 0) {
    			this.values[0][x][y][z] += Constants.HEME_TURNOVER_RATE * (Constants.HEME_SYSTEM_CONCENTRATION - this.values[0][x][y][z]);
    		}
    		return true;
    	}
        if(interactable instanceof Hemoglobin) {
        	Hemoglobin hb = (Hemoglobin) interactable;
        	double heme = Constants.K_HB * hb.values[0][x][y][z];
        	this.inc(heme, x, y, z);
        	hb.dec(heme, x, y, z);
        	return true; 
        }
        if(interactable instanceof Afumigatus) {
        	if(((Afumigatus) interactable).getStatus() == Afumigatus.HYPHAE) 
        		this.values[1][x][y][z] = 1.0;
        		
        	Afumigatus afumigatus = (Afumigatus) interactable;
        	double heme = Constants.HEME_UP * this.values[0][x][y][z];
        	afumigatus.incIronPool(heme);
        	this.dec(heme, x, y, z);
        	return true;
        }
        if(interactable instanceof Macrophage) {
        	Macrophage macrophage = (Macrophage) interactable;
        	if(Util.activationFunction(this.values[0][x][y][z], Constants.Kd_Heme, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
        		macrophage.setStatus(Macrophage.ACTIVATING);
        		//macrophage.setHeme(true);
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
