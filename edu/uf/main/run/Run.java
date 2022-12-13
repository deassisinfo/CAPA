package edu.uf.main.run;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uf.geometry.Quadrant;
import edu.uf.geometry.Recruiter;
import edu.uf.geometry.Voxel;
import edu.uf.main.print.PrintStat;

public interface Run {
	
	public static final List<Integer> L = Arrays.asList(0,1,2,3);
	
	public void run(
			int iterations, 
			int xbin, 
			int ybin, 
			int zbin, 
			Voxel[][][] grid, 
			List<Quadrant> quadrants, 
			Recruiter[] recruiters, 
			boolean printLattice, 
			File outputFile,
			int nthreads,
			PrintStat printStat
	) throws InterruptedException;
}
