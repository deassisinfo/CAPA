package edu.uf.interactable;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import edu.uf.geometry.Voxel;
import edu.uf.utils.Constants;
import edu.uf.utils.LinAlg;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

import java.util.HashSet;

public class Afumigatus extends PositionalInfectiousAgent{

    public static final String NAME = "Afumigatus";
    public static final int[] INIT_AFUMIGATUS_BOOLEAN_STATE = new int[] {1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static final int RESTING_CONIDIA = 0;
    public static final int SWELLING_CONIDIA = 1;
    public static final int GERM_TUBE = 2;
    public static final int HYPHAE = 3;
    public static final int DYING = 4;
    public static final int DEAD = 5;
    public static final int STERILE_CONIDIA = 6;
    
    public static final int FREE = 0;
    public static final int INTERNALIZING = 1;
    public static final int RELEASING = 2;
    public static final int ENGAGED = 3;

    public static final int hapX = 0;
    public static final int sreA = 1;
    public static final int HapX = 2;
    public static final int SreA = 3;
    public static final int RIA = 4;
    public static final int EstB = 5;
    public static final int MirB = 6;
    public static final int SidA = 7;
    public static final int Tafc = 8;
    public static final int ICP = 9;
    public static final int LIP = 10;
    public static final int CccA = 11;
    public static final int FC0fe = 12;
    public static final int FC1fe = 13;
    public static final int VAC = 14;
    public static final int ROS = 15;
    public static final int Yap1 = 16;
    public static final int SOD2_3 = 17;
    public static final int Cat1_2 = 18;
    public static final int ThP = 19;
    public static final int Fe = 20;
    public static final int O = 21;
    public static final int SPECIES_NUM = 22;

    private static double totalIron = 0;
    //private static int totalCells = 0;
    private static int[] totalCells = new int[5];
    /*private static int totalRestingConidia = 0;
    private static int totalSwellingConidia = 0;
    private static int totalGerminatingConidia = 0;
    private static int totalHyphae = 0;*/
    private static int  totalSterileConidia = 0;

    private static Set<Afumigatus> treeSepta = null;
    
    private Afumigatus nextSepta;
    private Afumigatus nextBranch;
    private boolean isRoot;
    private boolean growable;
    private boolean branchable;
    private int activationIteration;
    private int growthIteration;
    
    private double xTip;
    private double yTip;
    private double zTip;
    private double dx;
    private double dy;
    private double dz;
    
    public Afumigatus() {
    	this(0,0,0, 0,0,0, Rand.getRand().randunif(), Rand.getRand().randunif(), Rand.getRand().randunif(),
    			0, 0, 0, 0, true);
    }


    public Afumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, 
    		int state, boolean isRoot) {
        super(xRoot, yRoot, zRoot);
        this.setIronPool(ironPool);
        this.setState(state);
        super.setStatus(status);
        this.isRoot = isRoot;
        this.xTip = xTip;
        this.yTip = yTip;
        this.zTip = zTip;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.setEngulfed(false);

        //this.cfu = None

        this.growable = true;
        this.branchable = false;
        this.activationIteration = 0;
        this.growthIteration = growthIteration;
        this.setBooleanNetwork(Afumigatus.INIT_AFUMIGATUS_BOOLEAN_STATE.clone());

        this.nextSepta = null;
        this.nextBranch = null;
        //this.Fe = false;
        this.setBnIteration(0);
        
        Afumigatus.totalIron = Afumigatus.totalIron + ironPool;
        Afumigatus.totalCells[0] = Afumigatus.totalCells[0] + 1;
        
        
        if(status <=  Afumigatus.HYPHAE)Afumigatus.totalCells[status + 1]++;
    }
    
    public static int getTotalCells() {
    	return Afumigatus.totalCells[0];
    }
    
    public static int getTotalRestingConidia() {
    	return Afumigatus.totalCells[1];
    }
    
    public static int getTotalSwellingConidia() {
    	return Afumigatus.totalCells[2];
    }
    
    public static int getTotalGerminatingConidia() {
    	return Afumigatus.totalCells[3];
    }
    
    public static int getTotalHyphae() {
    	return Afumigatus.totalCells[4];
    }
    
    public static  double getTotalIron() {
    	return Afumigatus.totalIron;
    }
    
    public Afumigatus getNextSepta() { 
		return nextSepta;
	}

    
	public Afumigatus getNextBranch() {
		return nextBranch;
	}


	public boolean isRoot() {
		return isRoot;
	}


	public double getxTip() {
		return xTip;
	}


	public void setxTip(double xTip) {
		this.xTip = xTip;
	}


	public double getyTip() {
		return yTip;
	}


	public void setyTip(double yTip) {
		this.yTip = yTip;
	}


	public double getzTip() {
		return zTip;
	}


	public void setzTip(double zTip) {
		this.zTip = zTip;
	}

	public double getDx() {
		return dx;
	}


	public double getDy() {
		return dy;
	}


	public double getDz() {
		return dz;
	}
	
	public boolean isInternalizing() {
        return this.getState() == Afumigatus.INTERNALIZING;
	}
	
	public void setStatus(int status) {
		if(this.getStatus() <=  Afumigatus.HYPHAE)Afumigatus.totalCells[this.getStatus() + 1]--;
		if(status <=  Afumigatus.HYPHAE)Afumigatus.totalCells[status + 1]++;
		super.setStatus(status);
	}
	
	public synchronized void grow(int xbin, int ybin, int zbin, Voxel[][][] grid, Phagocyte phagocyte) {
        if(phagocyte instanceof Phagocyte) { // If instead of being in a voxel it is inside a phagosome
        	ArrayList<InfectiousAgent> tmpPhagossome = (ArrayList<InfectiousAgent>) ((ArrayList)phagocyte.getPhagosome()).clone();
        	Afumigatus phagent = null;
            for(InfectiousAgent agent : tmpPhagossome) {
                if(agent instanceof Afumigatus) {
                	phagent = (Afumigatus) agent;
                	if (phagent.getStatus() == Afumigatus.GERM_TUBE && phagent.getBooleanNetwork(Afumigatus.LIP) == 1) {
                		//pass
                        //#voxel.status = Phagocyte.NECROTIC if voxel.status != Phagocyte.DEAD else Phagocyte.DEAD
                	}
                }
            }
        }           
        if (this.getState() == Afumigatus.FREE) {
            Voxel voxel = Util.findVoxel(this.xTip, this.yTip, this.zTip, xbin, ybin, zbin, grid);
            if (voxel != null && voxel.getTissueType() != voxel.AIR) {
                Afumigatus nextSepta = this.elongate();
                if (nextSepta != null)
                    voxel.setCell(nextSepta);
                nextSepta = this.branch();
                if (nextSepta != null)
                    voxel.setCell(nextSepta);
            }
        }
	}
        

    private Afumigatus elongate() {

    	Afumigatus septa = null;
        //if(this.growable && this.getBooleanNetwork(Afumigatus.LIP) == 1) {
    	if(this.growable && canGrow()) {
            if(this.getStatus() == Afumigatus.HYPHAE) {
                if(this.growthIteration >= Constants.ITER_TO_GROW) {
                    this.growthIteration = 0;
                    this.growable = false;
                    this.branchable = true;
                    this.setIronPool(this.getIronPool() / 2.0);
                    this.nextSepta = createAfumigatus(this.xTip, this.yTip, this.zTip, 
                                                 this.xTip + this.dx, this.yTip + this.dy, this.zTip + this.dz,
                                                 this.dx, this.dy, this.dz, 0, 
                                                 0, Afumigatus.HYPHAE, this.getState(), false);
                    this.nextSepta.setIronPool(this.getIronPool());
                    septa = this.nextSepta;
                } else 
                    this.growthIteration = this.growthIteration + 1;
            }else if(this.getStatus() == Afumigatus.GERM_TUBE) {
                if(this.growthIteration >= Constants.ITER_TO_GROW){
                    this.setStatus(Afumigatus.HYPHAE);
                    this.xTip = this.getX() + this.dx;
                    this.yTip = this.getY() + this.dy;
                    this.zTip = this.getZ() + this.dz;
                }else
                    this.growthIteration = this.growthIteration + 1;
            }
        }
        return septa;
    }

    public Afumigatus branch() {
    	return this.branch(null, Constants.PR_BRANCH);
    }
    
    public Afumigatus branch(double prBranch) {
    	return this.branch(null, prBranch);
    }
    
    private Afumigatus branch(Double phi, double prBranch) {
    	Afumigatus branch = null;
    	if(this.branchable && this.getStatus() == Afumigatus.HYPHAE && canGrow()) {
        //if(this.branchable && this.getStatus() == Afumigatus.HYPHAE && this.getBooleanNetwork(Afumigatus.LIP) == 1) {
            if(Rand.getRand().randunif() < prBranch) {
                if(phi == null) {
                    phi = 2*Rand.getRand().randunif()*Math.PI;
                }

                this.setIronPool(this.getIronPool() / 2.0);
                double[] growthVector = new double[] {dx, dy, dz};
				double[][] base = LinAlg.gramSchimidt(this);
				double[][] baseInv = LinAlg.transpose(base);
				double[][] R = LinAlg.rotation(2*Rand.getRand().randunif()*Math.PI);
				R = LinAlg.dotProduct(base, LinAlg.dotProduct(R, baseInv));
				growthVector = LinAlg.dotProduct(R, growthVector);
                

                this.nextBranch = createAfumigatus(this.xTip, this.yTip, this.zTip,
                                             this.xTip + growthVector[0], this.yTip + growthVector[1], 
                                             this.zTip + growthVector[2],
                                             growthVector[0], growthVector[1], growthVector[2], -1,
                                              0, Afumigatus.HYPHAE, this.getState(), false);

                this.nextBranch.setIronPool(this.getIronPool());
                branch = this.nextBranch;
            }
            this.branchable = false;
        }
        return branch;
    }
    
    protected boolean canGrow() {
    	return this.getBooleanNetwork(Afumigatus.LIP) == 1;//Rand.getRand().randunif() < Util.activationFunction(this.getIronPool(), Constants.Kd_GROW, 1.0, Constants.HYPHAE_VOL, 1.0);
    }
    
    protected Afumigatus createAfumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, 
    		int state, boolean isRoot) {
    	return new Afumigatus(xRoot, yRoot, zRoot,
    			xTip, yTip, zTip,
                dx, dy, dy, growthIteration,
                ironPool, status, state, isRoot);
    }
    
    public Set<String> getNegativeInteractionList(){
    	if(this.negativeInteractList == null) {
    		synchronized(this) {
    			this.negativeInteractList = new HashSet<>();
    			this.negativeInteractList.add(Afumigatus.NAME);
    			this.negativeInteractList.add(Transferrin.NAME);
    		}
    	}
    	return this.negativeInteractList;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if(interactable instanceof Iron) { //UNCOMENT WHEN CREATE IRON
            Molecule mol = (Molecule) interactable;
        	if(this.getStatus() == Afumigatus.DYING || this.getStatus() == Afumigatus.DEAD) {
        		mol.inc(this.getIronPool(), x, y, z);
                this.incIronPool(-this.getIronPool());
            }
            return true;
        }else if(interactable instanceof Macrophage) {
        	Macrophage m = (Macrophage) interactable;
            if(m.isEngaged())
                return true;
            if(m.getStatus() != Macrophage.APOPTOTIC && m.getStatus() != Macrophage.NECROTIC && m.getStatus() != Macrophage.DEAD) {
                if(this.getStatus() != Afumigatus.RESTING_CONIDIA) {
                    double prInteract = this.getStatus() == Afumigatus.HYPHAE ? Constants.PR_MA_HYPHAE : Constants.PR_MA_PHAG;
                    if(Rand.getRand().randunif() < prInteract) {
                        Phagocyte.intAspergillus(m, this, this.getStatus() != Afumigatus.HYPHAE);
                        if(this.getStatus() == Afumigatus.HYPHAE && m.getStatus() == Macrophage.ACTIVE) {
                            this.setStatus(Afumigatus.DYING);
                            if(this.nextSepta != null) {
                                this.nextSepta.isRoot = true;
                            if(this.nextBranch != null)
                                this.nextBranch.isRoot = true;
                            }
                        }else {
                            if(this.getStatus() == Afumigatus.HYPHAE && m.getStatus() == Macrophage.ACTIVE) {
                            	m.setEngaged(true);
                            }
                        }
                    }
                }
            }
            return true;
        }
            
        return interactable.interact(this, x, y, z);
    }

    public void processBooleanNetwork() {
        if(this.getBnIteration() == 15) {
        	int[] temp = new int[Afumigatus.SPECIES_NUM];
        	//for(int i = 0; i < Afumigatus.SPECIES_NUM; i++)
        	//	temp[i] = 0;

            temp[Afumigatus.hapX] = -this.booleanNetwork[Afumigatus.SreA] + 1;
            temp[Afumigatus.sreA] = -this.booleanNetwork[Afumigatus.HapX] + 1;
            temp[Afumigatus.HapX] = this.booleanNetwork[Afumigatus.hapX] & (-this.booleanNetwork[Afumigatus.LIP] + 1);
            temp[Afumigatus.SreA] = this.booleanNetwork[Afumigatus.sreA] & this.booleanNetwork[Afumigatus.LIP];
            temp[Afumigatus.RIA] = -this.booleanNetwork[Afumigatus.SreA] + 1;
            temp[Afumigatus.EstB] = -this.booleanNetwork[Afumigatus.SreA] + 1;
            temp[Afumigatus.MirB] = this.booleanNetwork[Afumigatus.HapX] & (-this.booleanNetwork[Afumigatus.SreA] + 1);
            temp[Afumigatus.SidA] = this.booleanNetwork[Afumigatus.HapX] & (-this.booleanNetwork[Afumigatus.SreA] + 1);
            temp[Afumigatus.Tafc] = this.booleanNetwork[Afumigatus.SidA];
            temp[Afumigatus.ICP] = (-this.booleanNetwork[Afumigatus.HapX] + 1) & (this.booleanNetwork[Afumigatus.VAC] | this.booleanNetwork[Afumigatus.FC1fe]);
            temp[Afumigatus.LIP] = (this.booleanNetwork[Afumigatus.Fe] & this.booleanNetwork[Afumigatus.RIA]) | this.lipActivation();
            temp[Afumigatus.CccA] = -this.booleanNetwork[Afumigatus.HapX] + 1;
            temp[Afumigatus.FC0fe] = this.booleanNetwork[Afumigatus.SidA];
            temp[Afumigatus.FC1fe] = this.booleanNetwork[Afumigatus.LIP] & this.booleanNetwork[Afumigatus.FC0fe];
            temp[Afumigatus.VAC] = this.booleanNetwork[Afumigatus.LIP] & this.booleanNetwork[Afumigatus.CccA];
            // temp[Afumigatus.ROS] = this.boolean_network[Afumigatus.LIP] | 
            //                        (this.boolean_network[Afumigatus.O] & (- (this.boolean_network[Afumigatus.SOD2_3] & this.boolean_network[Afumigatus.ThP] 
            //                         & this.boolean_network[Afumigatus.Cat1_2]) + 1)) 
            //                        | (this.boolean_network[Afumigatus.ROS] & (- (this.boolean_network[Afumigatus.SOD2_3] 
            //                         & (this.boolean_network[Afumigatus.ThP] | this.boolean_network[Afumigatus.Cat1_2])) + 1));
            temp[Afumigatus.ROS] = (this.booleanNetwork[Afumigatus.O] & (- (this.booleanNetwork[Afumigatus.SOD2_3] & this.booleanNetwork[Afumigatus.ThP] 
                                   & this.booleanNetwork[Afumigatus.Cat1_2]) + 1)) 
                                  | (this.booleanNetwork[Afumigatus.ROS] & (- (this.booleanNetwork[Afumigatus.SOD2_3] 
                                   & (this.booleanNetwork[Afumigatus.ThP] | this.booleanNetwork[Afumigatus.Cat1_2])) + 1));
            temp[Afumigatus.Yap1] = this.booleanNetwork[Afumigatus.ROS];
            temp[Afumigatus.SOD2_3] = this.booleanNetwork[Afumigatus.Yap1];
            temp[Afumigatus.Cat1_2] = this.booleanNetwork[Afumigatus.Yap1] & (-this.booleanNetwork[Afumigatus.HapX] + 1);
            temp[Afumigatus.ThP] = this.booleanNetwork[Afumigatus.Yap1];

            temp[Afumigatus.Fe] = 0; // might change according to iron environment?
            temp[Afumigatus.O] = 0;
            //temp[Afumigatus.TAFCBI] = 0;

            //print(this.boolean_network)
            for(int i = 0; i < Afumigatus.SPECIES_NUM; i++)
                this.booleanNetwork[i] = temp[i];
            this.setBnIteration(0);
        }else
            this.setBnIteration(this.getBnIteration() + 1);
    }

    public void updateStatus() {
        this.activationIteration++;
        if(this.getStatus() == Afumigatus.RESTING_CONIDIA && 
                this.activationIteration >= Constants.ITER_TO_SWELLING && 
                Rand.getRand().randunif() < Constants.PR_ASPERGILLUS_CHANGE) {
            this.setStatus(Afumigatus.SWELLING_CONIDIA);
            this.activationIteration = 0;
        }else if(this.getStatus() == Afumigatus.SWELLING_CONIDIA && this.activationIteration >= Constants.ITER_TO_GERMINATE){// and \
                //random() < Constants.PR_ASPERGILLUS_CHANGE:
            this.setStatus(Afumigatus.GERM_TUBE);
            this.activationIteration = 0;
        }else if(this.getStatus() == Afumigatus.DYING) {
            this.die();
        }

        if(this.nextSepta == null) 
            this.growable = true;

        if(this.getState() == Afumigatus.INTERNALIZING || this.getState() == Afumigatus.RELEASING)
            this.setState(Afumigatus.FREE); 

        this.diffuseIron();
        if(this.nextBranch == null)
            this.growable = true;
    }

    public boolean isDead() {
        return this.getStatus() == Afumigatus.DEAD;
    }
    
    public void diffuseIron() {
    	this.diffuseIron(null);
    }

    public void diffuseIron(Afumigatus afumigatus) {
        if(afumigatus == null) {
            if(this.isRoot) {
                Afumigatus.treeSepta = new HashSet<>();
                Afumigatus.treeSepta.add(this);
                this.diffuseIron(this);
                double totalIron = 0;
              //for a in Afumigatus.tree_septa:
                for(Afumigatus a : Afumigatus.treeSepta) 
                    totalIron = totalIron + a.getIronPool();
                totalIron = totalIron/Afumigatus.treeSepta.size();
                //for a in Afumigatus.tree_septa:
                for(Afumigatus a : Afumigatus.treeSepta)
                    a.setIronPool(totalIron);
            }
        }else {
            if(afumigatus.nextSepta != null && afumigatus.nextBranch == null){
                Afumigatus.treeSepta.add(afumigatus.nextSepta);
                this.diffuseIron(afumigatus.nextSepta);
            }else if(afumigatus.nextSepta != null && afumigatus.nextBranch != null) {
                Afumigatus.treeSepta.add(afumigatus.nextSepta);
                Afumigatus.treeSepta.add(afumigatus.nextBranch);
                this.diffuseIron(afumigatus.nextBranch);
                this.diffuseIron(afumigatus.nextSepta);
            }
        }
    }

    public void incIronPool(double qtty) {
        this.setIronPool(this.getIronPool() + qtty);
        Afumigatus.totalIron = Afumigatus.totalIron + qtty;
    }

    public void die() {
        if(this.getStatus() != Afumigatus.DEAD) {
            if(this.getStatus() == Afumigatus.STERILE_CONIDIA)
                Afumigatus.totalSterileConidia = Afumigatus.totalSterileConidia - 1;
            this.setStatus(Afumigatus.DEAD);
            Afumigatus.totalCells[0] = Afumigatus.totalCells[0] - 1;
        }
    }

    protected int lipActivation() {
        return Rand.getRand().randunif() < Util.activationFunction(this.getIronPool(), Constants.Kd_LIP, 1.0, Constants.HYPHAE_VOL, 1.0) ? 1 : 0;
    }
    
    public String getName() {
    	return NAME;
    }


	@Override
	public void move(Voxel oldVoxel, int steps) {
	}
	
	public double getTAFCQTTY() {
		return Constants.TAFC_QTTY;
	}


	@Override
	public int getMaxMoveSteps() {
		// TODO Auto-generated method stub
		return -1;
	}

}
