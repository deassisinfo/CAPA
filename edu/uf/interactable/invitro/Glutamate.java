package edu.uf.interactable.invitro;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Erythrocyte;
import edu.uf.interactable.Haptoglobin;
import edu.uf.interactable.Heme;
import edu.uf.interactable.Hemoglobin;
import edu.uf.interactable.Hemopexin;
import edu.uf.interactable.Hepcidin;
import edu.uf.interactable.IL1;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Iron;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.Liver;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.Pneumocyte;
import edu.uf.interactable.TAFC;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Transferrin;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Glutamate extends Molecule implements Invitro{

	public static final String NAME = "Glutamate";
	public static final int NUM_STATES = 1;
	
	private static Glutamate molecule = null; 
    
    private Glutamate(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Glutamate getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Glutamate(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Glutamate getMolecule() {
    	return molecule;
    }
    

    public int getIndex(String str) {
        return 0;
    }

    public void degrade(int x, int y, int z) {}

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.values[0][x][y][z];
    }
    
    public Set<String> getNegativeInteractionList(){
    	
    	if(this.negativeInteractList == null) {
    		synchronized(this) {
    			this.negativeInteractList = new HashSet<>();
    			this.negativeInteractList.add(Iron.NAME);
    			this.negativeInteractList.add(FreeIron.NAME);
    			this.negativeInteractList.add(Erythrocyte.NAME);
    			this.negativeInteractList.add(Haptoglobin.NAME);
    			this.negativeInteractList.add(Hemopexin.NAME);
    			this.negativeInteractList.add(Heme.NAME);
    			this.negativeInteractList.add(Hemoglobin.NAME);
    			this.negativeInteractList.add(Hepcidin.NAME);
    			this.negativeInteractList.add(IL1.NAME);
    			this.negativeInteractList.add(IL10.NAME);
    			this.negativeInteractList.add(IL6.NAME);
    			this.negativeInteractList.add(Liver.NAME);
    			this.negativeInteractList.add(Lactoferrin.NAME);
    			this.negativeInteractList.add(Macrophage.NAME);
    			this.negativeInteractList.add(MIP1B.NAME);
    			this.negativeInteractList.add(MIP2.NAME);
    			this.negativeInteractList.add(Neutrophil.NAME);
    			this.negativeInteractList.add(Pneumocyte.NAME);
    			this.negativeInteractList.add(TGFb.NAME);
    			this.negativeInteractList.add(TNFa.NAME);
    			this.negativeInteractList.add(Transferrin.NAME);
    			this.negativeInteractList.add(Glutamate.NAME);
    			this.negativeInteractList.add(TAFC.NAME);
    			this.negativeInteractList.add(TinProtoporphyrin.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Afumigatus) {
            InvitroAfumigatus afumigatus = (InvitroAfumigatus) interactable;
            if(afumigatus.getStatus() != Afumigatus.RESTING_CONIDIA) {
            	if(Util.activationFunction(this.get(x, y, z), Constants.Kd_GLU, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
            		double qtty = Constants.GLU_UPTAKE_RATE * this.get(x, y, z);
            		this.dec(qtty, x, y, z);
            		afumigatus.setBooleanNetwork(InvitroAfumigatus.Glu, 1);
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
