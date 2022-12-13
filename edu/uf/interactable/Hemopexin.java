package edu.uf.interactable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Hemopexin extends Molecule{
	
	public static final String NAME = "Hemopexin";
	public static final int NUM_STATES = 2;
	
	//private static double xSystem = 0.0;
	
	private static Hemopexin molecule = null;  
	
	private static final Map<String, Integer> INDEXES;
	
	private double hemopexinSystemConcentration;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("Hp", 0); 
    	INDEXES.put("HpHb", 1);
    }
    
    private Hemopexin(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
		this.hemopexinSystemConcentration = Constants.DEFAULT_HPX_CONCENTRATION;
	}
    
    public static Hemopexin getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Hemopexin(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Hemopexin getMolecule() {
    	return molecule;
    }

    public void degrade(int x, int y, int z) {} //Hp turnover done at interaction!

    public int getIndex(String str) {
        return Hemopexin.INDEXES.get(str);
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
    			this.negativeInteractList.add(Haptoglobin.NAME);
    			this.negativeInteractList.add(Hemoglobin.NAME);
    		}
    	}
    	return this.negativeInteractList;
    } 

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if(interactable instanceof Hemopexin) {
    		if(this.values[2][x][y][z] > 0) {
    			this.values[0][x][y][z] += Constants.HEME_TURNOVER_RATE * (this.hemopexinSystemConcentration - this.values[0][x][y][z]);
    			this.values[1][x][y][z] -= Constants.HEME_TURNOVER_RATE * this.values[1][x][y][z];
    		}
    		return true;
    	}
    	if(interactable instanceof Afumigatus) {
        	if(((Afumigatus) interactable).getStatus() == Afumigatus.HYPHAE) 
        		this.values[2][x][y][z] = 1.0;
        	return true;
    	}
    	
    	if(interactable instanceof Liver) {
    		Liver liver = (Liver) interactable;
    		this.hemopexinSystemConcentration = liver.getBooleanEnsemble()[Liver.HPX]/((double)Liver.ENSEMBLE_SIZE);
        	//if(hpxQtty < Liver.LIVER_BN_EPS)return true;
        	
    		this.hemopexinSystemConcentration = (
    				this.hemopexinSystemConcentration * Constants.L_HPX_QTTY + 
    				(1 - this.hemopexinSystemConcentration) * Constants.L_REST_HPX_QTTY
    		) * Constants.VOXEL_VOL;

    		return true; 
    	}
        if (interactable instanceof Macrophage){//# or type(interactable) is Neutrophil: 
        	if(Util.activationFunction(this.values[1][x][y][z], Constants.Kd_HPX, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
        		((Macrophage)interactable).setStatus(Macrophage.RESTING);
        	}
            return true;
        }
        if(interactable instanceof Heme) {
        	Heme heme = (Heme) interactable;
        	double hpxheme = Util.michaelianKinetics(
        			this.values[0][x][y][z], 
        			heme.get(0,x,y,z), 
        			Constants.KM_HPX, 
        			Constants.STD_UNIT_T, 
        			Constants.KCAT_HPX, 
        			Constants.VOXEL_VOL
        	);
        	
        	//System.out.println(hpxheme + "\t" + heme.get(0,x,y,z) + "\t" + this.values[0][x][y][z]);
        	this.dec(hpxheme, 0, x, y, z);
        	this.inc(hpxheme, 1, x, y, z);
        	heme.dec(hpxheme, x, y, z);
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
