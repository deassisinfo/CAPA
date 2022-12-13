package edu.uf.interactable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;


public class Transferrin extends Molecule{
    

	public static final String NAME = "Transferrin";
	public static final int NUM_STATES = 3;
    private static final double THRESHOLD = Constants.K_M_TF_TAFC *Constants.VOXEL_VOL / 1.0e6;
    
    private static Transferrin molecule = null;

    private static final Map<String, Integer> INDEXES;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("Tf", 0);
    	INDEXES.put("TfFe", 1); 
    	INDEXES.put("TfFe2", 2);
    }
    
    private Transferrin(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Transferrin getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Transferrin(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Transferrin getMolecule() {
    	return molecule;
    }
    
    public void degrade(int x, int y, int z) {}

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.values[0][x][y][z];
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.values[1][x][y][z];
    	this.totalMoleculesAux[2] = this.totalMoleculesAux[2] + this.values[2][x][y][z];
    }

    public int getIndex(String str) {
        return Transferrin.INDEXES.get(str);
    }
    
    public Set<String> getNegativeInteractionList(){
    	
    	if(this.negativeInteractList == null) {
    		synchronized(this) {
    			this.negativeInteractList = new HashSet<>();
    			this.negativeInteractList.add(Transferrin.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {

        if (interactable instanceof Macrophage) {
        	Macrophage macro = (Macrophage) interactable;
        	double qttyFe2 = this.get("TfFe2", x, y, z) * Constants.MA_IRON_IMPORT_RATE * Constants.REL_IRON_IMP_EXP_UNIT_T;
            double qttyFe  = this.get("TfFe", x, y, z)  * Constants.MA_IRON_IMPORT_RATE * Constants.REL_IRON_IMP_EXP_UNIT_T;

            qttyFe2 = qttyFe2 < this.get("TfFe2", x, y, z) ? qttyFe2 : this.get("TfFe2", x, y, z);
            qttyFe = qttyFe < this.get("TfFe", x, y, z) ? qttyFe : this.get("TfFe", x, y, z);

            this.dec(qttyFe2, "TfFe2", x, y, z);
            this.dec(qttyFe, "TfFe", x, y, z);
            this.inc(qttyFe2 + qttyFe, "Tf", x, y, z);
            macro.incIronPool(2 * qttyFe2 + qttyFe);
            if (macro.isFpn() && macro.getStatus() != Macrophage.ACTIVE && macro.getStatus() != Macrophage.ACTIVATING) {
                double qtty = macro.getIronPool() * 
                		this.get("Tf", x, y, z) * Constants.MA_IRON_EXPORT_RATE * Constants.REL_IRON_IMP_EXP_UNIT_T;
                qtty = qtty <= 2*this.get("Tf", x, y, z) ? qtty : 2*this.get("Tf", x, y, z);
                double relTfFe = Util.ironTfReaction(qtty, this.get("Tf", x, y, z), this.get("TfFe", x, y, z));
                double tffeQtty  = relTfFe*qtty;
                double tffe2Qtty = (qtty - tffeQtty)/2;
                this.dec(tffeQtty + tffe2Qtty, "Tf", x, y, z);
                this.inc(tffeQtty, "TfFe", x, y, z);
                this.inc(tffe2Qtty, "TfFe2", x, y, z);
                macro.incIronPool(-qtty);
            }
            return true;
        }else if(interactable instanceof Iron) {
        	Iron iron = (Iron) interactable;
        	double qtty = iron.get("Iron", x, y, z);
        	qtty = qtty <= 2 * this.get("Tf", x, y, z) + this.get("TfFe", x, y, z) ? qtty : 2 * this.get("Tf", x, y, z) + this.get("TfFe", x, y, z);
            double relTfFe = Util.ironTfReaction(qtty, this.get("Tf", x, y, z), this.get("TfFe", x, y, z));
            double tffeQtty = relTfFe * qtty;
            double tffe2Qtty = (qtty - tffeQtty) / 2.0;
            this.dec(tffeQtty + tffe2Qtty, "Tf", x, y, z);
            this.inc(tffeQtty, "TfFe", x, y, z);
            this.inc(tffe2Qtty, "TfFe2", x, y, z);
            iron.dec(qtty, "Iron", x, y, z);

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
		return THRESHOLD;
	}

	@Override
	public int getNumState() {
		return NUM_STATES;
	}
}
