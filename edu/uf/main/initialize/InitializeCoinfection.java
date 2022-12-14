package edu.uf.main.initialize;

import java.util.ArrayList;
import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.control.MultiThreadExec;
import edu.uf.geometry.Voxel;
import edu.uf.interactable.AfumigatusCoinfection.AfumigatusNoIron;
import edu.uf.interactable.Granule;
import edu.uf.interactable.IL10;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;

public class InitializeCoinfection  extends InitializeBaseModel{
	
	private boolean granule = false;
	
	public void setGranule(boolean granule) {
		this.granule = granule;
	}
	
	public void initializeMolecules(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, boolean verbose) {
    	if(verbose) {
    		//System.out.println("Initializing Iron, TAFC, Lactoferrin, Transferrin, Hepcidin, IL6, TNF-a, IL10, TGF-b, MIP2, MIP1-b");
    	}
    	Granule gran = null;
    	TNFa tnfa = TNFa.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	IL10 il10 = IL10.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	TGFb tgfb = TGFb.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	MIP2 mip2 = MIP2.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	MIP1B mip1b = MIP1B.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	if(granule)gran = Granule.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	
    	MultiThreadExec.setMolecule(tnfa);
    	MultiThreadExec.setMolecule(il10);
    	MultiThreadExec.setMolecule(tgfb);
    	MultiThreadExec.setMolecule(mip2);
    	MultiThreadExec.setMolecule(mip1b);
    	if(granule)MultiThreadExec.setMolecule(gran);
    	
    	
    	Voxel.setMolecule(TNFa.NAME, tnfa);
    	Voxel.setMolecule(IL10.NAME, il10);
    	Voxel.setMolecule(TGFb.NAME, tgfb);
    	Voxel.setMolecule(MIP2.NAME, mip2);
    	Voxel.setMolecule(MIP1B.NAME, mip1b);
    	if(granule)Voxel.setMolecule(Granule.NAME, gran, true);
    }

	
	public List<AfumigatusNoIron> coInjuryInfect(int numAspergillus, Voxel[][][] grid, int xbin, int ybin, int zbin, int status, double initIron, double sigma, boolean verbose) {
    	if(verbose)System.out.println("Infecting with " + numAspergillus + " conidia!");
    	List<AfumigatusNoIron> list = new ArrayList<>();
        for (int i = 0; i < numAspergillus; i++) {
        	int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            AfumigatusNoIron a = new AfumigatusNoIron(
            		x, y, z, x, y, z, random(), random(), random(), 
            		0, initIron, 0, 0, true
            );
            list.add(a);
            a.setStatus(status);
            grid[x][y][z].setCell(a);
        }
    	return list;
    }
	
	/*public List<Macrophage> initializeMacrophage(Voxel[][][] grid, int  xbin, int ybin, int zbin,  int numMacrophages) {
    	List<Macrophage> list = new ArrayList<>();
        for (int i = 0; i < numMacrophages; i++) {
            int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            Macrophage m = new Macrophage(Constants.MA_INTERNAL_IRON);
            list.add(m);
            grid[x][y][z].setCell(m);
        }
        return list;
    }*/
	
}
