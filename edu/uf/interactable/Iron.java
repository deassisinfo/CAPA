package edu.uf.interactable;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;

public class Iron extends Molecule{

	public static final String NAME = "Iron";
	public static final int NUM_STATES = 1;
	
	private static Iron molecule = null; 
    
    private Iron(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Iron getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Iron(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Iron getMolecule() {
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
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) {
            Macrophage macro = (Macrophage) interactable;
        	if (macro.getStatus() == Macrophage.NECROTIC){
                this.inc(macro.getIronPool(), "Iron", x, y, z);
                macro.incIronPool(-macro.getIronPool());
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
