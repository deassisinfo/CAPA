package edu.uf.main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.uf.Diffusion.ADI;
import edu.uf.Diffusion.Diffuse;
import edu.uf.Diffusion.FADIClosed;
import edu.uf.Diffusion.FADIPeriodic;
import edu.uf.control.Exec;
import edu.uf.control.MultiThreadExec;
import edu.uf.control.MultiThreadExecDiffusion;
import edu.uf.geometry.MacrophageRecruiter;
import edu.uf.geometry.Voxel;
import edu.uf.geometry.NeutrophilRecruiter;
import edu.uf.geometry.Quadrant;
import edu.uf.geometry.Recruiter;
import edu.uf.geometry.Voxel;
import edu.uf.interactable.*;
import edu.uf.utils.Constants;
import edu.uf.utils.LinAlg;
import edu.uf.utils.Rand;

public class ModelTmp {
	
    public static boolean SA = false;
    public static int aspergillusCount = 0;
    public static int maxCount = 0;
    public static int iter = 0;
    
    static final List<Integer> l = new ArrayList<>();
    //static Molecule[] molList;
    
    static {
    	l.add(0);
    	l.add(1);
    	l.add(2);
    	l.add(3);
    }
    
    
    
    
    static Random rand = new Random();
    
    public static double random() {
    	return rand.nextDouble();
    }

    public static int randint(int min, int max) {
    	return rand.nextInt(max - min) + min;
    }
    
    public static Voxel[][][] createPeriodicGrid(int xbin, int ybin, int zbin){
    	Voxel[][][] grid = new Voxel[xbin][ybin][zbin];
        for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) {
        			grid[x][y][z] = new Voxel(x,y,z);
        		}
        
        
        for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) {
                    if (x + 1 < xbin)
                        grid[x][y][z].getNeighbors().add(grid[x + 1][y][z]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[0][y][z]);
                    if (y + 1 < ybin)
                        grid[x][y][z].getNeighbors().add(grid[x][y + 1][z]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[x][0][z]);
                    if (x - 1 >= 0)
                        grid[x][y][z].getNeighbors().add(grid[x - 1][y][z]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[xbin-1][y][z]);
                    if (y - 1 >= 0)
                        grid[x][y][z].getNeighbors().add(grid[x][y - 1][z]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[x][ybin-1][z]);
                    if (z + 1 < zbin)
                        grid[x][y][z].getNeighbors().add(grid[x][y][z + 1]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[x][y][0]);
                    if (z - 1 >= 0)
                        grid[x][y][z].getNeighbors().add(grid[x][y][z - 1]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[x][y][zbin-1]);
        		}
        
        return grid;
    }

    public static List<Quadrant> createQuadrant(
    Voxel[][][] grid, 
    int xbin, 
    int ybin, 
    int zbin, 
    int xSize, 
    int ySize, 
    int zSize){
        List<Quadrant> quadrants = new ArrayList<>();
        for(int i = 0; i < Math.ceil(xbin/xSize); i++)
        	for(int j = 0; j < Math.ceil(ybin/ySize); j++)
        		for(int k = 0; k < Math.ceil(zbin/zSize); k++) {
                    int xMin = i * xSize;
                    int xMax = xMin + xSize;
                    int yMin = j * ySize;
                    int yMax = yMin + ySize;
                    int zMin = k * zSize;
                    int zMax = zMin + zSize;
                    Quadrant q = new Quadrant();
                    quadrants.add(q);
                    for(int x = xMin; x < xMax; x++)
                    	for(int y = yMin; y < yMax; y++)
                    		for(int z = zMin; z < zMax; z++) {
                                if (x < xbin && y < ybin && z < zbin)
                                    grid[x][y][z].setQuadrant(q);
                    		}
        		}
        
        return quadrants;
    }

    public static void initializeMolecules(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse) {
    	//System.out.println("creating molecules ...");
    	Iron iron = Iron.getMolecule(new double[1][xbin][ybin][zbin], null);
    	TAFC tafc = TAFC.getMolecule(new double[2][xbin][ybin][zbin], diffuse);
    	Lactoferrin lactoferrin = Lactoferrin.getMolecule(new double[3][xbin][ybin][zbin], diffuse);
    	Transferrin transferrin = Transferrin.getMolecule(new double[3][xbin][ybin][zbin], diffuse);
    	Hepcidin hepcidin = Hepcidin.getMolecule(new double[1][xbin][ybin][zbin], null);
    	IL6 il6 = IL6.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	TNFa tnfa = TNFa.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	IL10 il10 = IL10.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	TGFb tgfb = TGFb.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	MIP2 mip2 = MIP2.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	MIP1B mip1b = MIP1B.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	
    	//IL1 il1 = IL1.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	//Hemoglobin hb = Hemoglobin.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	//Haptoglobin hp = Haptoglobin.getMolecule(new double[2][xbin][ybin][zbin], diffuse);
    	//Hemopexin hpx = Hemopexin.getMolecule(new double[2][xbin][ybin][zbin], diffuse);
    	//Heme heme = Heme.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	
    	
    	MultiThreadExec.setMolecule(iron);
    	MultiThreadExec.setMolecule(tafc);
    	MultiThreadExec.setMolecule(lactoferrin);
    	MultiThreadExec.setMolecule(transferrin);
    	MultiThreadExec.setMolecule(hepcidin);
    	MultiThreadExec.setMolecule(il6);
    	MultiThreadExec.setMolecule(tnfa);
    	MultiThreadExec.setMolecule(il10);
    	MultiThreadExec.setMolecule(tgfb);
    	MultiThreadExec.setMolecule(mip2);
    	MultiThreadExec.setMolecule(mip1b);
    	
    	/*MultiThreadExec.setMolecule(il1);
    	MultiThreadExec.setMolecule(hb);
    	MultiThreadExec.setMolecule(hp);
    	MultiThreadExec.setMolecule(hpx);
    	MultiThreadExec.setMolecule(heme);*/
    	
    	
    	//System.out.println("setting molecules ...");
    	Voxel.setMolecule(Iron.NAME, iron, true);
    	Voxel.setMolecule(TAFC.NAME, tafc, true);
    	Voxel.setMolecule(Lactoferrin.NAME, lactoferrin);
    	for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) {
        			transferrin.values[0][x][y][z] = Constants.DEFAULT_APOTF_CONCENTRATION;
        			transferrin.values[1][x][y][z] = Constants.DEFAULT_TFFE_CONCENTRATION;
        			transferrin.values[2][x][y][z] = Constants.DEFAULT_TFFE2_CONCENTRATION;
        			hepcidin.values[0][x][y][z] = Constants.THRESHOLD_HEP * Constants.VOXEL_VOL;
        			/*hpx.values[0][x][y][z] = 1e-30;
        			hp.values[0][x][y][z] = 1e-30;
        			heme.values[0][x][y][z] = 3e-13;*/
        		}
    	Voxel.setMolecule(Transferrin.NAME, transferrin);
    	Voxel.setMolecule(Hepcidin.NAME, hepcidin);
    	Voxel.setMolecule(IL6.NAME, il6);
    	Voxel.setMolecule(TNFa.NAME, tnfa);
    	Voxel.setMolecule(IL10.NAME, il10);
    	Voxel.setMolecule(TGFb.NAME, tgfb);
    	Voxel.setMolecule(MIP2.NAME, mip2);
    	Voxel.setMolecule(MIP1B.NAME, mip1b);
    	
    	/*Voxel.setMolecule(IL1.NAME, il1);
    	Voxel.setMolecule(Hemoglobin.NAME, hb);
    	Voxel.setMolecule(Haptoglobin.NAME, hp);
    	Voxel.setMolecule(Hemopexin.NAME, hpx);
    	Voxel.setMolecule(Heme.NAME, heme, true);*/
    	/*
        for(int x = 0; x < xbin; x++) {
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) {
                    //""" Iron related """
                    grid[x][y][z].setMolecule(Iron.NAME, iron, true);
                    grid[x][y][z].setMolecule(TAFC.NAME, tafc, true);
                    grid[x][y][z].setMolecule(Lactoferrin.NAME, lactoferrin);
                    transferrin.values[0][x][y][z] = Constants.DEFAULT_APOTF_CONCENTRATION;
                    transferrin.values[1][x][y][z] = Constants.DEFAULT_TFFE_CONCENTRATION;
                    transferrin.values[2][x][y][z] = Constants.DEFAULT_TFFE2_CONCENTRATION;
                    grid[x][y][z].setMolecule(Transferrin.NAME, transferrin);
                    hepcidin.values[0][x][y][z] = Constants.THRESHOLD_HEP * Constants.VOXEL_VOL;
                    grid[x][y][z].setMolecule(Hepcidin.NAME, hepcidin);

                    //""" Cytokines (all ZERO) """
                    grid[x][y][z].setMolecule(IL6.NAME, il6);
                    grid[x][y][z].setMolecule(TNFa.NAME, tnfa);
                    grid[x][y][z].setMolecule(IL10.NAME, il10);
                    grid[x][y][z].setMolecule(TGFb.NAME, tgfb);

                    //""" Chemokines (all ZERO) """
                    grid[x][y][z].setMolecule(MIP2.NAME, mip2);
                    grid[x][y][z].setMolecule(MIP1B.NAME, mip1b);
        		}
        	System.out.println(x);
        }
        */
        //System.out.println("molecules settled ...");
    }

    public static void initializeLiver(Voxel[][][] grid, int xbin, int ybin, int zbin) {
    	for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
                    grid[x][y][z].setCell(Liver.getLiver()); //REVIEW
    }

    public static void initializePneumocytes(Voxel[][][] grid, int xbin, int ybin, int zbin, int numCells) {
        int k = 0;
        while (k < numCells) {
            int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            if (grid[x][y][z].getCells().isEmpty()) {
                grid[x][y][z].setCell(new Pneumocyte());
                k = k + 1;
            }
            if(k%100000==0)System.out.println(k +  " pneumocytes initialized ...");
        }
    }

    public static void initializeMacrophage(
    		Voxel[][][] grid, 
    		int  xmin, 
    		int xmax, 
    		int ymin, 
    		int ymax, 
    		int zmin, 
    		int zmax, 
    		int numMacrophages) {
        for (int i = 0; i < numMacrophages; i++) {
            int x = randint(xmin, xmax-1);
            int y = randint(ymin, ymax-1);
            int z = randint(zmin, zmax-1);
            Macrophage m = new Macrophage(Constants.MA_INTERNAL_IRON);
            grid[x][y][z].setCell(m);
        }
    }

    public static void  initializeNeutrophils(
    		Voxel[][][] grid, 
    		int  xmin, 
    		int xmax, 
    		int ymin, 
    		int ymax, 
    		int zmin, 
    		int zmax, 
    		int numMacrophages) {
    	for (int i = 0; i < numMacrophages; i++) {
            int x = randint(xmin, xmax-1);
            int y = randint(ymin, ymax-1);
            int z = randint(zmin, zmax-1);
            grid[x][y][z].setCell(new Neutrophil(0.0));
    	}
    }
    
    public static void  initializeErytrocytes(Voxel[][][] grid, int xbin, int ybin, int zbin) {
    	for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) {
        			Erythrocyte erythrocyte = new Erythrocyte(180);
                    grid[x][y][z].setCell(erythrocyte); //REVIEW
        		}
    }

    public static List<Afumigatus> infect(
    		int numAspergillus, 
    		Voxel[][][] grid, 
    		int xmin, 
    		int xmax, 
    		int ymin, 
    		int ymax, 
    		int zmin, 
    		int zmax) {
        List<Afumigatus> l = new ArrayList<>();
        for (int i = 0; i < numAspergillus; i++) {
        	int x = randint(xmin, xmax-1);
            int y = randint(ymin, ymax-1);
            int z = randint(zmin, zmax-1);
            Afumigatus a = new Afumigatus(
            		x, y, z, x, y, z, random(), random(), random(), 
            		0,Constants.CONIDIA_INIT_IRON,0, 0, true
            );
            //l.add(a);
            grid[x][y][z].setCell(a);
            //if(i%10000==0)System.out.println(i + " fungus initialized ...");
        }
        return l;
    }
    
    public static void setQuadrant(Voxel[][][] grid, int xbin, int ybin, int zbin) {
    	for(int i = 0; i < xbin; i++)
        	for(int j = 0; j < ybin; j++)
        		for(int k = 0; k < zbin; k++) 
        			if(grid[i][j][k].getQuadrant() != null)
        				grid[i][j][k].getQuadrant().updateChemokines(grid[i][j][k].getMolecules(), i, j, k);
    }

    static int active = 0;
    public static void run(
    		int iterations, 
    		int xbin, 
    		int ybin, 
    		int zbin, 
    		Voxel[][][] grid, 
    		List<Quadrant> quadrants, 
    		Recruiter[] recruiters, 
    		//list, 
    		//input, 
    		boolean printLattice) {
    	
    	long tic = 0;
    	long toc = 0;
    	double tnext = 0;
    	double trec = 0;
    	double tdiff = 0;
    	
        for (int k = 0; k < iterations; k++) {
            ModelTmp.iter = k;
            if (k != 0)
                Collections.shuffle(l);
            //#tic = timeit.default_timer()
            if (Afumigatus.getTotalCells() > 3e5)//1.1e7)
                return;
            //tic = timeit.default_timer()
            for (int ii : l) {

                if (ii == 0) {
                    tic = System.currentTimeMillis();
                    Exec.next(grid, xbin, ybin, zbin);
                    toc = System.currentTimeMillis();
                    tnext = toc - tic;
                    		
                    //#print("MultiThreadExec ", (toc - tic))
                }else if (ii == 1) {
                	tic = System.currentTimeMillis();
                	Exec.recruit(recruiters, grid, quadrants);
                	toc = System.currentTimeMillis();
                	trec = toc - tic;
                }else if (ii == 2) {
                	tic = System.currentTimeMillis();
                	Exec.diffusion();
                	toc = System.currentTimeMillis();
                	tdiff = toc - tic;
                }else if (ii == 3) {
                	//MultiThreadExec.resetCount();
                }
            }
            
            if(k>0) //pw.println(Afumigatus.getTotalCells() + "," + Macrophage.getTotalCells() + "," + Neutrophil.getTotalCells() + "," + (tnext + trec + tdiff));
            //System.out.println(Afumigatus.getTotalCells() + "," + Macrophage.getTotalCells() + "," + Neutrophil.getTotalCells() + "," + (tnext + trec + tdiff));
            
            
            if(false && k%1==0) {
            	int count = 0;
            	int resting = 0;
                active = 0;
                int inactive = 0;
                int anergic = 0;
                int other = 0;
                Pneumocyte m =null;
            	for(int x = 0; x < xbin; x++)
                	for(int y = 0; y < ybin; y++)
                		for(int z = 0; z < zbin; z++) 
                			for(Map.Entry<Integer, Cell> entry : grid[x][y][z].getCells().entrySet()) {
                				if(entry.getValue() instanceof Pneumocyte) {
                					m = (Pneumocyte) entry.getValue();
                					if(m.getStatus() == Macrophage.RESTING || m.getStatus() == Macrophage.ACTIVATING || m.getStatus() == Macrophage.INTERACTING)
                						resting++;
                					else if(m.getStatus() == Macrophage.ACTIVE)
                						active++;
                					else if(m.getStatus() == Macrophage.INACTIVE)
                						inactive++;
                					else if(m.getStatus() == Macrophage.ANERGIC)
                						anergic++;
                					else
                						other++;
                				}
                					//count++;
                			}
            	System.out.println(k + " " + resting + " " + active + " " + inactive + " " + anergic + " " + other + " " + Afumigatus.getTotalCells());
            }
            
            if(false && k%30==0) {
            	int count = 0;
            	int resting = 0;
                //int active = 0;
            	active = 0;
                int inactive = 0;
                int anergic = 0;
                int other = 0;
                Pneumocyte m =null;
            	for(int x = 0; x < xbin; x++)
                	for(int y = 0; y < ybin; y++)
                		for(int z = 0; z < zbin; z++) 
                			for(Map.Entry<Integer, Cell> entry : grid[x][y][z].getCells().entrySet()) {
                				if(entry.getValue() instanceof Pneumocyte) {
                					m = (Pneumocyte) entry.getValue();
                					if(m.getStatus() == Macrophage.RESTING || m.getStatus() == Macrophage.ACTIVATING || m.getStatus() == Macrophage.INTERACTING)
                						resting++;
                					else if(m.getStatus() == Macrophage.ACTIVE)
                						active++;
                					else if(m.getStatus() == Macrophage.INACTIVE)
                						inactive++;
                					else if(m.getStatus() == Macrophage.ANERGIC)
                						anergic++;
                					else
                						other++;
                				}
                					//count++;
                			}
            	System.out.println(resting + " " + active + " " + inactive + " " + anergic + " " + other + " " + Afumigatus.getTotalCells());
            }
            
            //if(k == 3)return;
            //toc = timeit.default_timer()
            //#print((toc0 - tic0), (toc1 - tic1), (toc2 - tic2))
            //print(Afumigatus.total_cells, (toc - tic))
            /*
            double tf = 0;
            for(int x = 0; x < xbin; x++)
            	for(int y = 0; y < ybin; y++)
            		for(int z = 0; z < zbin; z++) {
            			tf += grid[x][y][z].molecules.get(Transferrin.name).values[0];
            		}
            System.out.println(tf);*/
            
            
            if (!ModelTmp.SA) {
                ModelTmp.printStatistics(k);
            }
            MultiThreadExec.resetCount();
        }
    }
    
    public static void infect(int numAspergillus, Voxel[][][] grid, int xbin, int ybin, int zbin, double sigma) {
    	System.out.println("infecting ...");
    	
    	double[][][] w = new double[xbin][ybin][zbin];
    	double sumW = 0;
    	
    	double sigma2 = sigma*sigma;
    	double d = 0;
    	int[] p1 = new int[3];
    	int[] p2 = new int[3];
    	
    	while(d < sigma) {
    		p1[0] = Rand.getRand().randunif(0, xbin);
    		p1[1] = Rand.getRand().randunif(0, ybin);
    		p1[2] = Rand.getRand().randunif(0, zbin);
    		
    		p2[0] = Rand.getRand().randunif(0, xbin);
    		p2[1] = Rand.getRand().randunif(0, ybin);
    		p2[2] = Rand.getRand().randunif(0, zbin);
    		
    		d = LinAlg.euclidianDistance(p1, p2);
    	}
    	
    	System.out.println("distance between intection sites: " + d);
    	int[] v = new int[3];
    	double d1 = 0;
    	double d2 = 0;
    	
    	for(int x = 0; x < xbin; x++) {
        	for(int y = 0; y < ybin; y++) {
        		for(int z = 0; z < zbin; z++) {
        			v[0] = x;
        			v[1] = y;
        			v[2] = z;
        			
        			d1 = LinAlg.euclidianDistance(v, p1);
        			d2 = LinAlg.euclidianDistance(v, p2);
        			
        			if(d1 < d2) {
        				if(d1 < sigma) {  //threshold = sigma
        					w[x][y][z] = (1/(sigma*Math.sqrt(2*Math.PI)))*Math.exp(-(0.5*d1*d1)/sigma2);
        					sumW += w[x][y][z];
        				}
        			}else {
        				if(d2 < sigma) {  //threshold = sigma
        					w[x][y][z] = (1/(sigma*Math.sqrt(2*Math.PI)))*Math.exp(-(0.5*d2*d2)/sigma2);
        					sumW += w[x][y][z];
        				}
        			}
        		}
        	}
    	}
    	System.out.println("sumW = " + sumW);
    	
    	int count = 0;
    	int totalInfection = 0;
    	Afumigatus a = null;
    	for(int x = 0; x < xbin; x++) {
        	for(int y = 0; y < ybin; y++) {
        		for(int z = 0; z < zbin; z++) {
        			count = Rand.getRand().randpois(numAspergillus*w[x][y][z]/sumW);
        			if(count > 0) {
        				for(int i = 0; i < count; i++) {
        					a = new Afumigatus(
        		            		x, y, z, x, y, z, random(), random(), random(), 
        		            		0,Constants.CONIDIA_INIT_IRON,0, 0, true
        		            );
        					//a.setStatus(Afumigatus.SWELLING_CONIDIA);
        		            //l.add(a);
        		            grid[x][y][z].setCell(a);
        		            totalInfection++;
        							
        				}
        			}
        		}
        	}
    	}
    	
    	System.out.println("Whole lung infected with " + totalInfection + " conidia!");
    	
    }
    
    
    public void runMultiThread(
    		int iterations, 
    		int xbin, 
    		int ybin, 
    		int zbin, 
    		Voxel[][][] grid, 
    		List<Quadrant> quadrants, 
    		Recruiter[] recruiters, 
    		//list, 
    		//input, 
    		boolean printLattice,
    		int nthreads) throws InterruptedException {
    	
    	
    	System.out.println("Setting grid ...");
    	MultiThreadExec.setGrid(grid);
    	System.out.println("Setting Diffuse ...");
    	//MultiThreadExecDiffusion.setDiffuse(diffusion);
    	/*
    	TGFb.getMolecule().getNegativeInteractionList();
    	MIP2.getMolecule().getNegativeInteractionList();
    	MIP1B.getMolecule().getNegativeInteractionList();
    	TNFa.getMolecule().getNegativeInteractionList();
    	Transferrin.getMolecule().getNegativeInteractionList();
    	Lactoferrin.getMolecule().getNegativeInteractionList();
    	Iron.getMolecule().getNegativeInteractionList();
    	IL10.getMolecule().getNegativeInteractionList();
    	IL6.getMolecule().getNegativeInteractionList();
    	Hepcidin.getMolecule().getNegativeInteractionList();
    	//Liver.getMolecule().getNegativeInteractionList();*/
    	
    	
    	
    	//synchronized(this){
    	Thread[] threads = new Thread[nthreads];
    	
    	double ttimeAvg = 0;
    	double ntimeAvg = 0;
    	double rtimeAvg = 0;
    	double dtimeAvg = 0;
    	System.out.println("Starting ...");
    	PrintWriter pw = null;
		/*try {
			pw = new PrintWriter("/Users/henriquedeassis/Documents/Afumigatus/data/tmp.tsv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	List<Integer> l = new ArrayList<>();
    	l.add(0);
    	l.add(1);
    	l.add(2);
    	l.add(3);
    	
    	double tic, toc;
    	double ttime = 0;
    	double dtime = 0;
    	double ntime = 0;
    	double rtime = 0;
    	
        for (int k = 0; k < iterations; k++) {
        	Collections.shuffle(l);
        	
        	for(int ii : l) {
        		if(ii == 0) { /* interact, gc & degrade */
        			tic = System.currentTimeMillis();
        			for(int t = 0; t < nthreads; t++) {
        				threads[t] = new Thread(new MultiThreadExec("Thread-" + t));
        			}
        			for(int t = 0; t < nthreads; t++) {
        				threads[t].start();
        		
        			}
        			for(int t = 0; t < nthreads; t++) {
        				threads[t].join();
        			}
        	
        			toc = System.currentTimeMillis();
        			ttime =  k > 5 ? (toc - tic) : 0;
        			ttimeAvg += ttime;
        		}
        		if(ii == 1) { /* next, quadrant */
        			tic = System.currentTimeMillis();
        			MultiThreadExec.singleThreadNext();
        			toc = System.currentTimeMillis();
        			//System.out.println("single next: " + (toc - tic));
        			ntime = k > 5 ? (toc - tic) : 0;
        			ntimeAvg += ntime;
        		}
        		if(ii == 2) { /* Diffusion */
        			tic = System.currentTimeMillis();
        			for(int t = 0; t < nthreads; t++) {
        				threads[t] = new Thread(new MultiThreadExecDiffusion("diffusion-Thread-" + t));
        			}
        			for(int t = 0; t < nthreads; t++) {
        				threads[t].start();
        		
        			}
        			for(int t = 0; t < nthreads; t++) {
        				threads[t].join();
        			}
        			toc = System.currentTimeMillis();
        			//System.out.println("diffusion: " + (toc - tic));
        			dtime = k > 5 ? (toc - tic) : 0;
        			dtimeAvg += dtime;
        		}
        	//System.out.println("threads: " + (toc - tic));
        		if(ii==3) {/* Recruit */
        			tic = System.currentTimeMillis();
        			MultiThreadExec.recruit(recruiters, grid, quadrants);
        			toc = System.currentTimeMillis();
        			//System.out.println("recruit: " + (toc - tic));
        			rtime = k > 5 ? (toc - tic) : 0;
        			rtimeAvg += rtime;
        		}
        	}
        	//tic = System.currentTimeMillis();
        	//MultiThreadExec.diffusion(diffusion);
        	//toc = System.currentTimeMillis();
        	//System.out.println("diffusion: " + (toc - tic));
        	//double dtime = (toc - tic);
        	//dtimeAvg += dtime;
        	if(k%10 == 0)
        		System.out.println(k + " " + ttime + " " + ntime + " " + rtime + " " + dtime);
            if (!ModelTmp.SA) {
                ModelTmp.printStatistics(k);
            }
            MultiThreadExec.resetCount();
            //System.out.println("reseting ...");
            MultiThreadExec.reset();
            MultiThreadExecDiffusion.reset();
            
            //System.out.println("nofifying ...");
            //this.notifyAll();
            //List<Afumigatus> list = new ArrayList<>();
            /*double tf = 0;
            double tffe = 0;
            double tffe2 = 0;
            for(int x = 0; x < xbin; x++) {
            	for(int y = 0; y < ybin; y++) {
            		for(int z = 0; z < zbin; z++) {
            			tf    += Transferrin.getMolecule().values[0][x][y][z];
            			tffe  += Transferrin.getMolecule().values[1][x][y][z];
            			tffe2 += Transferrin.getMolecule().values[2][x][y][z];
            			/*Afumigatus af = null;
            			
            			for(Map.Entry<Integer, InfectiousAgent> entry : grid[x][y][z].getInfectiousAgents().entrySet()) {
            				af = (Afumigatus) entry.getValue();
            				for(Afumigatus af2 : list) {
            					if(af.getId() == af2.getId()) {
            						System.out.println("Equal objects " + af + " " + af2);
            					}
            				}
            				list.add(af);
            			}
            		}
            	}
            pw.println(tf + " " + tffe + " " + tffe2);
            }*/
        //pw.close();
        }
        ttimeAvg /= ((double) (iterations-5));
        rtimeAvg /= ((double) (iterations-5));
        ntimeAvg /= ((double) (iterations-5));
        dtimeAvg /= ((double) (iterations-5));
        System.out.println(ttimeAvg + " " + ntimeAvg + " " + rtimeAvg + " " + dtimeAvg);
        
        
        
        
        
        //System.out.println(Thread.currentThread().getName());
        /*for(int x = 0; x < xbin; x++) {
        	for(int y = 0; y < ybin; y++) {
        		for(int z = 0; z < zbin; z++) {
        			int count = 0;
        			String sttr = "";
        			for(String str : grid[x][y][z].threadNames) {
        				count++;
        				sttr += x + " " + y +  " " + z + " "  + str;
        			}
        			if(count>1) {
        				System.out.println(count + ": " + sttr);
        			}
        		}
        	}
        }*/
        
        
    	//}
    }
            

            /*
            if print_lattice:
                import random
                n = open("/Users/henriquedeassis/Documents/aspergillus_tmp_files/neutrophils" + str(k) + ".csv", "w")
                f = open("/Users/henriquedeassis/Documents/aspergillus_tmp_files/aspergillus" + str(k) + ".csv", "w")
                w = open("/Users/henriquedeassis/Documents/aspergillus_tmp_files/macrophage" + str(k) + ".csv", "w")
                for x in range(xbin):
                    for y in range(ybin):
                        for z in range(zbin):
                            for _, a in grid[x][y][z].cells.items():
                                if type(a) is Neutrophil:
                                    n.write(str(x + random.random()) + "," + str(y + random.random()) + "," + str(z + random.random()) + "\n")
                                if type(a) is Afumigatus:
                                    f.write(str(x) + "," + str(y) + "," + str(z) + "\n")
                                if type(a) is Macrophage:
                                    w.write(str(x + random.random()) + "," + str(y + random.random()) + "," + str(z + random.random()) + "\n")

                n.close()
                f.close()
                w.close()
                */
    
    static PrintWriter pw = null;
    static String fileName = "/Users/henriquedeassis/Documents/Projects/Afumigatus/data/WTLHS083121/";
    public static void printStatistics(int k) {
    	
    	try {
    		if(pw == null)
    			pw = new PrintWriter(fileName); //JDoubleKOfADIMulti.tsv PieceOfLungClosed.tsv
		
    		pw.println(k + "\t" + 
        	  //Afumigatus.getTotalIron() + "\t" + 
              Afumigatus.getTotalCells() + "\t" +
              Afumigatus.getTotalRestingConidia() + "\t" +
              Afumigatus.getTotalSwellingConidia() + "\t" +
              Afumigatus.getTotalGerminatingConidia() + "\t" +
              Afumigatus.getTotalHyphae() + "\t" +
        	  //active + "\t" +
              (TAFC.getMolecule().getTotalMolecule(0) + TAFC.getMolecule().getTotalMolecule(1)) + "\t" +
              TAFC.getMolecule().getTotalMolecule(0) + "\t" +
              TAFC.getMolecule().getTotalMolecule(1) + "\t" +
              Lactoferrin.getMolecule().getTotalMolecule(0) + "\t" +
              Lactoferrin.getMolecule().getTotalMolecule(1) + "\t" +
              Lactoferrin.getMolecule().getTotalMolecule(2) + "\t" +
              (Transferrin.getMolecule().getTotalMolecule(0) + Transferrin.getMolecule().getTotalMolecule(1) + Transferrin.getMolecule().getTotalMolecule(2)) + "\t" +
              Transferrin.getMolecule().getTotalMolecule(0) + "\t" +
              Transferrin.getMolecule().getTotalMolecule(1) + "\t" +
              Transferrin.getMolecule().getTotalMolecule(2) + "\t" +
              Hepcidin.getMolecule().getTotalMolecule(0) + "\t" +
              //Hemoglobin.getMolecule().getTotalMolecule(0) + "\t" + 
              /*Heme.getMolecule().getTotalMolecule(0) + "\t" + 
              Haptoglobin.getMolecule().getTotalMolecule(0) + "\t" + 
              Haptoglobin.getMolecule().getTotalMolecule(1) + "\t" + 
              Hemopexin.getMolecule().getTotalMolecule(0) + "\t" + 
              Hemopexin.getMolecule().getTotalMolecule(1) + "\t" +*/
              //TGFb.getMolecule().getTotalMolecule(0) + "\t" +
              //IL6.getMolecule().getTotalMolecule(0) + "\t" +
              //IL10.getMolecule().getTotalMolecule(0) + "\t" +
              TNFa.getMolecule().getTotalMolecule(0) + "\t" +
              //IL1.getMolecule().getTotalMolecule(0) + "\t" +
              //IL1.getMolecule().getTotalMolecule(0) + "\t" +
              MIP1B.getMolecule().getTotalMolecule(0) + "\t" +
              MIP2.getMolecule().getTotalMolecule(0) + "\t" +
              //Erythrocyte.getTotalCells() + "\t" + 
              Macrophage.getTotalCells() + "\t" +
              Neutrophil.getTotalCells());
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public static void main_legacy(String[] args) throws FileNotFoundException {
    	ModelTmp.readParameters(args);
    	
    	//int nAspergillus = Integer.parseInt(args[0]);
    	//System.out.println("BLA");
    	
        ModelTmp.SA = false;
        //#bla()
        //System.exit(1);

        //String[] input = new String[]{"0", "4000000", "100000", "4000000"};//#, input[40]]#, input[1], input[2], input[3]]
        //String[] input = new String[]{"0", "601526", "4817", "192689"};
        

        int xbin = 10;//Integer.parseInt(args[1]);//67;//282;
        int ybin = 10;//Integer.parseInt(args[2]);//67;//123;
        int zbin = 10;//Integer.parseInt(args[3]);//67;//180;
        int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        ///////////////////////

        double D = Constants.D/(4*(30/Constants.TIME_STEP_SIZE));
        //ADI diffusion = new ADI(D, 4);
        //diffusion.createPeriodicMatrixes(xbin, ybin, zbin);
        //Diffuse diffusion = new FADIClosed(f, pdeFactor, dt);
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        //ADI diffusion = new FADIPeriodic(D, 4);
        //diffusion.createPeriodicMatrixes(xbin, ybin, zbin);
        
        //System.out.println("Creating grid ...");
        Voxel[][][] grid = ModelTmp.createPeriodicGrid(xbin, ybin, zbin);
        //System.out.println("Creating quadrant ...");
        List<Quadrant> quadrants = ModelTmp.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        //System.out.println("Initializing molecules ...");
        ModelTmp.initializeMolecules(grid, xbin, ybin, zbin, diffusion);
        //System.out.println("Initializing pneumocytes ...");
        ModelTmp.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        //System.out.println("Initializing Liver ...");
        ModelTmp.initializeLiver(grid, xbin, ybin, zbin);
        
        //System.out.println("Initializing Erythrocytes ...");
        //Model.initializeErytrocytes(grid, xbin, ybin, zbin);

        //System.out.println("Initializing macrophages ...");
        ModelTmp.initializeMacrophage(grid, 0, xbin, 0, ybin, 0, zbin, Integer.parseInt(input[2]));
        //System.out.println("Infecting ...");
        ModelTmp.infect(Integer.parseInt(input[1]), grid, 0, xbin, 0, ybin, 0, zbin);
        //Model.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, 67); 


        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        
        //Constants.MAX_N = 0;
        //System.out.println(Constants.MAX_N);
        
        //Model.fileName += "outAtenuatedN_0.0.tsv";
        
        
        //System.out.println("setting quadrants ...");
        setQuadrant(grid, xbin, ybin, zbin);
        //double n = 8.0;
        //double m = 8.0;
        //Constants.TF_INTERCEPT = n*(Constants.TF_INTERCEPT) + (n-1) * Constants.TF_SLOPE * Constants.THRESHOLD_LOG_HEP;
        //Constants.TAFC_QTTY = m*Constants.TAFC_QTTY;
        //Constants.MIN_MA = 0;
        
        //fileName += "tf" + n + "_" + m + ".tsv";
        //VALUES FROM THE OLD JAVA PROJECT/////////
        
        //ADI diffusion_open = new ADI(D, 4);
        
        //Diffuse diffusion = new DiffuseOpen(f, pdeFactor, dt);
        
        //System.out.println(Constants.PR_P_INT);
        //diffusion_open.create_periodic_matrixes(xbin, ybin, zbin);
        long tic = System.currentTimeMillis();
        ModelTmp model = new ModelTmp();
        /*try {
			model.runMultiThread(
					720,//(int) 72*2*(30/Constants.TIME_STEP_SIZE), 
					xbin, 
					ybin, 
					zbin, 
					grid, 
					quadrants, 
					recruiters, 
					false,
					12);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        //pw = new PrintWriter("/Users/henriquedeassis/Documents/Afumigatus/data/out.csv");
        //System.out.println("run");
        
        
        System.out.println(
        		Constants.D + "\t" + 
    	Constants.ITER_TO_SWELLING + "\t" + 
    	Constants.PR_ASPERGILLUS_CHANGE + "\t" + 
    	Constants.ITER_TO_GERMINATE + "\t" + 
    	Constants.ITER_TO_CHANGE_STATE + "\t" + 
    	Constants.ITER_TO_REST + "\t" + 
    	Constants.ITER_TO_GROW + "\t" + 
    	Constants.PR_BRANCH + "\t" + 
    	Constants.TURNOVER_RATE + "\t" + 
    	Constants.LAC_QTTY + "\t" + 
    	Constants.TAFC_QTTY + "\t" + 
    	Constants.MA_IL6_QTTY + "\t" + 
    	Constants.MA_MIP1B_QTTY + "\t" + 
    	Constants.MA_MIP2_QTTY + "\t" + 
    	Constants.MA_IL10_QTTY + "\t" + 
    	Constants.MA_TNF_QTTY + "\t" + 
    	Constants.MA_TGF_QTTY + "\t" + 
    	Constants.IL6_HALF_LIFE + "\t" +  
    	Constants.MA_MOVE_RATE_REST + "\t" + 	
    	Constants.PR_MA_PHAG + "\t" + 
    	Constants.PR_N_PHAG + "\t" + 
    	Constants.PR_N_HYPHAE + "\t" + 
    	Constants.PR_MA_HYPHAE + "\t" + 
    	Constants.PR_P_INT + "\t" + 
    	Constants.MA_MAX_CONIDIA + "\t" + 
    	Constants.N_MAX_CONIDIA + "\t" + 
    	Constants.K_M_TF_TAFC + "\t" + 
    	Constants.K_M_TF_LAC + "\t" + 
    	Constants.NEUTROPHIL_HALF_LIFE + "\t" + 
    	Constants.MA_HALF_LIFE + "\t" + 
    	Constants.RECRUITMENT_RATE + "\t" + 
    	args[31]
    );
        
        
        
        
        ModelTmp.run(
        		720,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false
        		);
        long toc = System.currentTimeMillis();
        
        pw.close();
        //System.out.println((toc - tic));
    }
    
    
    
    private static void readParameters(String[] args) {
    	Constants.D = Double.parseDouble(args[0]);
    	Constants.ITER_TO_SWELLING =  Integer.parseInt(args[1]);
    	Constants.PR_ASPERGILLUS_CHANGE = Double.parseDouble(args[2]);
    	Constants.ITER_TO_GERMINATE = Integer.parseInt(args[3]);
    	Constants.ITER_TO_CHANGE_STATE = Integer.parseInt(args[4]);
    	Constants.ITER_TO_REST = Integer.parseInt(args[5]);
    	Constants.ITER_TO_GROW = Integer.parseInt(args[6]);
    	Constants.PR_BRANCH = Double.parseDouble(args[7]);
    	Constants.TURNOVER_RATE = Double.parseDouble(args[8]);
    	Constants.LAC_QTTY = Double.parseDouble(args[9]);
    	Constants.TAFC_QTTY = Double.parseDouble(args[10]);
    	Constants.MA_IL6_QTTY  = Double.parseDouble(args[11]);
    	Constants.MA_MIP1B_QTTY = Double.parseDouble(args[12]);
    	Constants.MA_MIP2_QTTY = Double.parseDouble(args[13]);
    	Constants.MA_IL10_QTTY = Double.parseDouble(args[14]);
    	Constants.MA_TNF_QTTY = Double.parseDouble(args[15]);
    	Constants.MA_TGF_QTTY = Double.parseDouble(args[16]);
    	
    	Constants.IL6_HALF_LIFE = Double.parseDouble(args[17]);
    	Constants.MIP1B_HALF_LIFE = Double.parseDouble(args[17]);
    	Constants.MIP2_HALF_LIFE = Double.parseDouble(args[17]);
    	Constants.IL10_HALF_LIFE = Double.parseDouble(args[17]);
    	Constants.TNF_HALF_LIFE = Double.parseDouble(args[17]);
    	Constants.TGF_HALF_LIFE = Double.parseDouble(args[17]);
    	
    	Constants.MA_MOVE_RATE_ACT = Double.parseDouble(args[18]);
    	Constants.MA_MOVE_RATE_REST = Double.parseDouble(args[18]);
    	
    	Constants.PR_MA_PHAG = Double.parseDouble(args[19]);
    	Constants.PR_N_PHAG = Double.parseDouble(args[20]);
    	Constants.PR_N_HYPHAE = Double.parseDouble(args[21]);
    	Constants.PR_MA_HYPHAE = Double.parseDouble(args[22]);
    	Constants.PR_P_INT = Double.parseDouble(args[23]);
    	Constants.MA_MAX_CONIDIA = Integer.parseInt(args[24]);
    	Constants.N_MAX_CONIDIA = Integer.parseInt(args[25]);
    	Constants.K_M_TF_TAFC = Double.parseDouble(args[26]);
    	Constants.K_M_TF_LAC = Double.parseDouble(args[27]);
    	Constants.NEUTROPHIL_HALF_LIFE = Double.parseDouble(args[28]);
    	Constants.MA_HALF_LIFE = Double.parseDouble(args[29]);
    	Constants.RECRUITMENT_RATE = Double.parseDouble(args[30]);
    	ModelTmp.fileName += ("outWT_" + args[31] + ".tsv");
    	
    }
    
    //1:  8.06  2.292 0.106 3.036 6766
    //2:  5.206 2.384 0.108 3.388 5575
    //4:  3.79  2.79  0.09  3.408 5076
    //6:  3.588 2.842 0.058 3.268 4919
    //8:  2.974 2.488 0.074 2.868 4229
    //12: 3.694 2.9   0.07  3.292 5007
    
    
    //1:  7.692 2.35  0.062 3.176 6671
    //2:  5.756 2.466 0.082 3.312 5844
    //4:  4.096 3.002 0.106 3.342 5299
    //6:  3.586 2.82  0.104 3.39  4991
    //8:  3.896 3.206 0.068 3.408 5322
    //12: 3.49  2.732 0.07  3.522 4936
    
    //1:  8.434 2.56  0.108 3.716 7434
    //2:  5.098 2.482 0.082 2.574 5147
    //4:  4.344 2.61  0.07  2.416 4759
    //6:  3.494 2.608 0.146 1.718 4002
    //8:  3.612 2.626 0.082 1.994 4182
    //12: 3.984 2.852 0.084 2.362 4674
    
    
    //1:  2527.1 263.1 0.1  4341.4 71318
    //2:  1327.2 311.7 0.0  2645.0 42841
    //4:  809.6  436.2 0.1  1546.1 27922
    //6:  799.9  256.4 0.0  1230.8 22874
    //8:  778.7  263.2 0.0  1260.7 23028
    //12: 583.5  441.1 0.0  1317.3 23420
    
    //s:  1240.0 0.0          346.0  17549
    //1:  1191.3 234.7 0.0    406.6  18339
    //2:  876.2  234.9 0.1    390.8  15034
    //4:  546.3  244.8 0.0    266.0  10589
    //6:  472.5  249.1 0.0    285.8  10089
    
    
    //1:  1123.4 197.0 0.0    309.9  27552
    //2:  669.5  345.5 0.0    178.5  21228
    //4:  378.4  206.5 0.0    125.4  14803
    //6:  281.3  206.2 0.0    103.1  12501
    //8:  257.2  198.1 0.0    104.4  12111
    //12: 215.7  201.5 0.0    106.3  11706
    
    
    //HL12: 5180.4 6349.1 0.0 9000.7 344236

}
