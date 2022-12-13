package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;

public abstract class Molecule extends Interactable{

	private int id;
	
	public double[][][][] values;
	private double[] totalMolecules;
	protected double[] totalMoleculesAux;
	private Diffuse diffuse;
	
	protected Molecule(double[][][][] qttys, Diffuse diffuse) {
        this.id = Id.getId();
        this.values = qttys ;
        this.totalMoleculesAux = new double[qttys.length];
        this.totalMolecules = new double[qttys.length];
        this.diffuse = diffuse;
        for(int i = 0; i < qttys.length; i++) {
        	for(double[][] matrix : qttys[i])
        		for(double[] array : matrix)
        			for(double d : array) 
        				this.incTotalMolecule(i, d);
        }
	}
	
	private void incTotalMolecule(int index, double inc) {
    	this.totalMolecules[index] = this.totalMolecules[index] + inc;
    }

	public double inc(double qtty, int x, int y, int z) {
		return this.inc(qtty, 0, x, y, z); 
	}
	
	public double inc(double qtty, String index, int x, int y, int z) {
		return inc(qtty, this.getIndex(index), x, y, z);
	}
	
    public double inc(double qtty, int index, int x, int y, int z) {
        this.values[index][x][y][z] = this.values[index][x][y][z] + qtty;
        return this.values[index][x][y][z];
    }
    
    public double dec(double qtty, int x, int y, int z) {
		return this.dec(qtty, 0, x, y, z);
	}
	
	public double dec(double qtty, String index, int x, int y, int z) {
		return dec(qtty, this.getIndex(index), x, y, z);
	}

    public double dec(double qtty, int index, int x, int y, int z) {
    	qtty = this.values[index][x][y][z] - qtty >= 0 ? qtty : 0;
        this.values[index][x][y][z] = this.values[index][x][y][z] - qtty;
        return this.values[index][x][y][z];
    }
    
    public double pdec(double p, int x, int y, int z) {
		return this.pdec(p, 0, x, y, z);
	}
	
	public double pdec(double p, String index, int x, int y, int z) {
		return pdec(p, this.getIndex(index), x, y, z);
	}

    public double pdec(double p, int index, int x, int y, int z) {
        double dec = this.values[index][x][y][z] * p;
        this.values[index][x][y][z] = this.values[index][x][y][z] - dec;
        return this.values[index][x][y][z];
    }
    
    public double pinc(double p, int x, int y, int z) {
		return this.pinc(p, 0, x, y, z);
	}
	
	public double pinc(double p, String index, int x, int y, int z) {
		return pinc(p, this.getIndex(index), x, y, z);
	}

    public double pinc(double p, int index, int x, int y, int z) {
        double inc = this.values[index][x][y][z] * p;
        this.values[index][x][y][z] = this.values[index][x][y][z] + inc;
        return this.values[index][x][y][z];
    }
    
    public void set(double qtty, int x, int y, int z) {
		this.set(qtty, 0, x, y, z);
	}
	
	public void set(double qtty, String index, int x, int y, int z) {
		set(qtty, this.getIndex(index), x, y, z);
	}

    public void set(double qtty, int index, int x, int y, int z) {
        this.incTotalMolecule(index, - this.values[index][x][y][z]);
        this.incTotalMolecule(index, qtty);
        this.values[index][x][y][z] = qtty;
    }
    
    public double get(int x, int y, int z) {
		return this.get(0, x, y, z);
	}
	
	public double get(String index, int x, int y, int z) {
		return get(this.getIndex(index), x, y, z);
	}

    public double get(int index, int x, int y, int z) {
        return this.values[index][x][y][z];
    }
    
    public abstract void degrade(int x, int y, int z);

    public void degrade(double p, int x, int y, int z) {
        if(p < 0) {
            return;
        }
        for(int i = 0; i < this.values.length; i++) { 
            this.pdec(1-p, i, x, y, z);
        }
    }
    
    public int getId() {
    	return id;
    }
    
    public double getTotalMolecule(int index) {
    	return this.totalMolecules[index];
    }
    
    public void diffuse() {
    	if(diffuse == null) return;
    	for(int i = 0; i < values.length; i++) {
    		diffuse.solver(values, i); 
    	}
    }
            
    public abstract void computeTotalMolecule(int x, int y, int z);

    public abstract int getIndex(String str);
    
    public abstract double getThreshold();
    
    public abstract int getNumState();
    
    public void resetCount() {
    	for(int i = 0; i < this.totalMolecules.length; i++) {
    		this.totalMolecules[i] =  this.totalMoleculesAux[i];
    		this.totalMoleculesAux[i] = 0;
    	}
    }
	
}
