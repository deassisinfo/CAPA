package edu.uf.interactable.covid;

import java.util.HashSet;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Erythrocyte;
import edu.uf.interactable.Haptoglobin;
import edu.uf.interactable.Heme;
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
import edu.uf.interactable.Phagocyte;
import edu.uf.interactable.TAFC;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Transferrin;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class IFN1 extends Molecule{
	private double eps = 1e-16;
	
	public static final String NAME = "IFN1";
	public static final int NUM_STATES = 1;
	
	private static IFN1 molecule = null;    
    
    private IFN1(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static IFN1 getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new IFN1(values, diffuse);
    	}
    	return molecule;
    }
    
    public static IFN1 getMolecule() {
    	return molecule;
    }

    public void degrade(int x, int y, int z) {
        degrade(Constants.IFN1_HALF_LIFE, x, y, z);
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
    			this.negativeInteractList.add(IL1.NAME);
    			this.negativeInteractList.add(IL6.NAME);
    			this.negativeInteractList.add(IL10.NAME);
    			this.negativeInteractList.add(TNFa.NAME);
    			this.negativeInteractList.add(TGFb.NAME);
    			this.negativeInteractList.add(MIP2.NAME);
    			this.negativeInteractList.add(MIP1B.NAME);
    			this.negativeInteractList.add(Iron.NAME);
    			this.negativeInteractList.add(Hepcidin.NAME);
    			this.negativeInteractList.add(Liver.NAME);
    			this.negativeInteractList.add(SarsCoV2.NAME);
    			this.negativeInteractList.add(Hemopexin.NAME);
    			this.negativeInteractList.add(Heme.NAME);
    			this.negativeInteractList.add(Erythrocyte.NAME);
    			this.negativeInteractList.add(Haptoglobin.NAME);
    			this.negativeInteractList.add(Neutrophil.NAME);
    			this.negativeInteractList.add(IFN1.NAME);
    			
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage){//# or type(interactable) is Neutrophil: 
        	Macrophage cell = (Macrophage) interactable;
        	if(cell.getStatus() == Phagocyte.ACTIVE) {
        		this.inc(Constants.IFN1_QTTY, x, y, z);
        	}
            return true; 
        }
        if (interactable instanceof Pneumocyte){
        	Pneumocyte cell = (Pneumocyte) interactable;
        	if(cell.getStatus() != Phagocyte.APOPTOTIC || cell.getStatus() != Phagocyte.NECROTIC || cell.getStatus() != Phagocyte.DEAD) 
        		if(Util.activationFunction(this.get(x, y, z), Constants.Kd_IFN1, Constants.UNIT_T) > Rand.getRand().randunif()) 
        			if((cell.getViralReplicationRate() - Constants.DEFAULT_VIRAL_REPLICATION_RATE) < Pneumocyte.EPS) {
        				double replicationRate = 1 + (cell.getViralReplicationRate() - 1) * Constants.IFN1_INHIBITION;
        				cell.setViralReplicationRate(replicationRate);
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
