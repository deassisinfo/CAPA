package edu.uf.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.Diffusion.FADIPeriodic;
import edu.uf.geometry.MacrophageRecruiter;
import edu.uf.geometry.MacrophageReplenisher;
import edu.uf.geometry.NKRecruiter;
import edu.uf.geometry.NeutrophilRecruiter;
import edu.uf.geometry.NeutrophilReplenisher;
import edu.uf.geometry.Quadrant;
import edu.uf.geometry.Recruiter;
import edu.uf.geometry.Voxel;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Macrophage;
import edu.uf.main.initialize.Initialize;
import edu.uf.main.initialize.InitializeBaseInvitroModel;
import edu.uf.main.initialize.InitializeBaseModel;
import edu.uf.main.initialize.InitializeCoinfection;
import edu.uf.main.initialize.InitializeDummyCovidModel;
import edu.uf.main.initialize.InitializeHemeInVitroModel;
import edu.uf.main.initialize.InitializeHemorrhageModel;
import edu.uf.main.print.PrintBaseInVitro;
import edu.uf.main.print.PrintBaseModel;
import edu.uf.main.print.PrintCoinfection;
import edu.uf.main.print.PrintDummyCovid;
import edu.uf.main.print.PrintHemeInVitroModel;
import edu.uf.main.print.PrintHemorrhageModel;
import edu.uf.main.print.PrintStat;
import edu.uf.main.run.Run;
import edu.uf.main.run.RunSingleThread;
import edu.uf.utils.Constants;

public class Main {
	
	private static void baseModel(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeBaseModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintBaseModel();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);


        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        
        run.run(
        		1440,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,
        		-1,
        		stat
        );
        
        stat.close();
        //System.out.println((toc - tic));
	}
	
	
	private static void baseInvitroModel(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeBaseInvitroModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintBaseInVitro();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{args[0], args[1], args[2], args[3]};
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        List<Macrophage> macrophage = initialize.initializeMacrophage(grid, xbin, ybin, zbin, (int) (Integer.parseInt(input[0]) * Double.parseDouble(input[1])));
        List<Afumigatus> afumigatus = initialize.infect(Integer.parseInt(input[0]), grid, xbin, ybin, zbin, Afumigatus.SWELLING_CONIDIA, 0,  -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        Constants.MIN_MA = Integer.parseInt(input[0]) * Double.parseDouble(input[1]);
        Constants.MA_MOVE_RATE_ACT = Constants.MA_MOVE_RATE_ACT * Double.parseDouble(input[2]);
        Constants.MA_MOVE_RATE_REST = Constants.MA_MOVE_RATE_ACT;
        Constants.PR_MA_PHAG = Constants.PR_MA_PHAG * Double.parseDouble(input[3]);

        Recruiter[] recruiters = new Recruiter[0];
        
        System.out.println(input[0] + " " + input[1] + " " + input[2] + " " + input[3]);
        
        File file = new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/phagocytosis_1_" + args[1] + "_" + args[2] + "_" + args[3] + ".tsv");
        
        run.run(
        		60,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		file,
        		-1,
        		stat
        );
        
        /*for(Macrophage m : macrophage)
        	m.die();
        for(Afumigatus m : afumigatus)
        	m.die();*/
        
        
        stat.close();
        //System.out.println((toc - tic));
	}
	
	private static void hemorrhateModel(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeHemorrhageModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintHemorrhageModel();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        /*Constants.CONIDIA_INIT_IRON *= Double.parseDouble(args[0]);
        Constants.L_HPX_QTTY = 0;
        Constants.L_REST_HPX_QTTY = 0;
        Constants.DEFAULT_HPX_CONCENTRATION = 0;
        Constants.HEME_TURNOVER_RATE = 0;*/
        //Constants.HEME_UP = 0;//Constants.HEME_UP*0.8;
        //Constants.Kd_HPX=1; //7.120429e-13
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        //initialize.initializeErytrocytes(grid, xbin, ybin, zbin);
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);


        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        
        
        
        File file = new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/tmp.tsv");
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,
        		-1,
        		stat
        );
        
        stat.close();
        //System.out.println((toc - tic));
	}
	
	private static void runExperiment081321() throws Exception {
		FileReader fr = new FileReader("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/mat081321.csv");
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		String[] sp = null;
	
		while((line = br.readLine())!=null) {
			sp = line.split(",");
			//System.out.println("64 " + sp[0] + " " + sp[1] + " " + sp[2]);
			Main.baseInvitroModel(new String[]{"64", sp[0], sp[1], sp[2]});
		}
		
		br.close();
	}
	
	
	private static void runSA092221(String[] args)  throws Exception{
		Initialize initialize = new InitializeHemorrhageModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintHemorrhageModel();
		
		
		Constants.HEME_SYSTEM_CONCENTRATION = Double.parseDouble(args[0]);
		Constants.HEME_TURNOVER_RATE = Double.parseDouble(args[1]);
		Constants.Kd_HPX = Double.parseDouble(args[2]);
		Constants.Kd_Heme = Double.parseDouble(args[3]);
		Constants.KM_HPX = Double.parseDouble(args[4]);
		Constants.KCAT_HPX = Double.parseDouble(args[5]);
		Constants.HEME_UP = Double.parseDouble(args[6]); 
		Constants.DEFAULT_HPX_CONCENTRATION = Double.parseDouble(args[7]);
		Constants.L_HPX_QTTY = Double.parseDouble(args[8]);
		Constants.L_REST_HPX_QTTY = Double.parseDouble(args[9]);
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        //initialize.initializeErytrocytes(grid, xbin, ybin, zbin);
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);


        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        
        
        File file = new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/hemorrhage/outWT_" + args[10] + ".tsv");
        
        run.run(
        		1080,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		file,
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	private static void runHemeInVitro101221()  throws Exception{
		Initialize initialize = new InitializeHemeInVitroModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintHemeInVitroModel();
		
		
		Constants.TURNOVER_RATE = 0;
		
		Constants.Kd_GROW = Constants.Kd_LIP / 2.0;
		
		Constants.KM_IRON = 1e-10;
		Constants.KCAT_IRON = 30;
		Constants.MIN_FREE_IRON = Constants.KM_IRON*Constants.VOXEL_VOL*10;//L*0.0001;
		Constants.INIT_GLU  = 0.02*Constants.VOXEL_VOL;
		Constants.INIT_IRON = Constants.MIN_FREE_IRON;//1e-5*Constants.VOXEL_VOL;
		//Constants.MIN_FREE_IRON = 0;
		
		Constants.Kd_GLU = 1;//5e1*Constants.VOXEL_VOL;
		
		Constants.INIT_HEME = 0;
		Constants.INIT_TIN_PROTOPORPHYRIN = 0;
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "100"};//, "15", "640"};
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);


        Recruiter[] recruiters = new Recruiter[0];
        
        
        //ile file = new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/hemorrhage/outWT_" + args[10] + ".tsv");
        
        run.run(
        		1440,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	
	
	private static void runExperiment101221()  throws Exception{
		Initialize initialize = new InitializeBaseModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintBaseModel();
		
		
		/*Initialize initialize = new InitializeHemorrhageModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintHemorrhageModel();*/
		
		Constants.Kd_GROW = Constants.Kd_LIP / 10.0;
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, Integer.parseInt(input[0]));
        
        


        Recruiter[] recruiters = new Recruiter[1];
        recruiters[0] = new MacrophageRecruiter();
        //recruiters[1] = new NeutrophilRecruiter();
        
        
        //ile file = new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/hemorrhage/outWT_" + args[10] + ".tsv");
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	
	private static void runDummyCovid(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeDummyCovidModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintDummyCovid();
		
		
		int xbin = 30;
		int ybin = 30;
		int zbin = 30;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        int nMacrophage = 15;
        int nPnemocytes = 1000;
        double infectionLevel = Double.parseDouble(args[0]);
        Constants.MAX_MA *= 9;
        Constants.MAX_N *= 9;
        Constants.MAX_NK *= 9;
        Constants.DEFAULT_VIRAL_REPLICATION_RATE = Double.parseDouble(args[1]);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, nPnemocytes);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, nMacrophage);
        ((InitializeDummyCovidModel) initialize).infect(infectionLevel, grid, 5, 5, 5);
        initialize.setQuadrant(grid, xbin, ybin, zbin);


        Recruiter[] recruiters = new Recruiter[3];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        recruiters[2] = new NKRecruiter();
        
        run.run(
        		1440,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,
        		-1,
        		stat
        );
        
        stat.close();
        //System.out.println((toc - tic));
	}
	
	
	private static void baseCoinfection(String[] args) throws InterruptedException {
		InitializeCoinfection initialize = new InitializeCoinfection();
		Run run = new RunSingleThread();
		PrintCoinfection stat = new PrintCoinfection();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "20", args[3], "640", "0"};
        //Constants.MAX_N = Constants.MAX_N;
        //Constants.MAX_MA = Constants.MAX_MA;
        
        //Constants.Kd_Granule = 2e12;
        
        //Constants.ITER_TO_GROW_FAST = Integer.parseInt(args[0]);
        Constants.ITER_TO_GROW = Integer.parseInt(args[0]);
        
        //Constants.Kd_Granule = Double.parseDouble(args[1]) * 2e12;
        
        Constants.PR_N_HYPHAE = Double.parseDouble(args[1])*Constants.PR_N_HYPHAE;
        Constants.PR_MA_HYPHAE = Double.parseDouble(args[2])*Constants.PR_MA_HYPHAE;
        //Constants.MA_MOVE_RATE_ACT = Constants.MA_MOVE_RATE_ACT*10;
        //Constants.MA_MOVE_RATE_REST = Constants.MA_MOVE_RATE_ACT*10;
        
        //Constants.NEUTROPHIL_HALF_LIFE =  - Math.log(0.5) / (Double.parseDouble(args[3]) * (Constants.HOUR/((double) Constants.TIME_STEP_SIZE)));
        
        //Constants.Granule_HALF_LIFE = - Math.log(0.5) / (Double.parseDouble(args[4]) * (Constants.HOUR/((double) Constants.TIME_STEP_SIZE)));
        
        Constants.MAX_MA = Integer.parseInt(args[3]);
        Constants.MAX_N = Integer.parseInt(args[4]);
        
        
        
        
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        //initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, Integer.parseInt(input[4]));
        initialize.coInjuryInfect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.HYPHAE, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        //stat.setGrid(grid);


        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageReplenisher();
        recruiters[1] = new NeutrophilReplenisher();
        
        
        
        run.run( 
        		720,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		new File("/Users/henriquedeassis/eclipse-workspace/jISS_legacy/sample/out_" + args[5]), //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/baseClock.tsv"),
        		-1,
        		stat
        );
        
        stat.close();

        //System.out.println((toc - tic));
	}
	
	
	private static void baseCoinfectionScenario1(String[] args) throws InterruptedException {
		InitializeCoinfection initialize = new InitializeCoinfection();
		Run run = new RunSingleThread();
		PrintCoinfection stat = new PrintCoinfection();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "20", args[3], "640", "0"};
        //Constants.MAX_N = Constants.MAX_N;
        //Constants.MAX_MA = Constants.MAX_MA;
        
        //Constants.Kd_Granule = 2e12;
        
        //Constants.ITER_TO_GROW_FAST = Integer.parseInt(args[0]);
        Constants.ITER_TO_GROW = Integer.parseInt(args[0]);
        
        //Constants.Kd_Granule = Double.parseDouble(args[1]) * 2e12;
        
        Constants.PR_N_HYPHAE = Double.parseDouble(args[1])*Constants.PR_N_HYPHAE;
        Constants.PR_MA_HYPHAE = Double.parseDouble(args[2])*Constants.PR_MA_HYPHAE;
        //Constants.MA_MOVE_RATE_ACT = Constants.MA_MOVE_RATE_ACT*10;
        //Constants.MA_MOVE_RATE_REST = Constants.MA_MOVE_RATE_ACT*10;
        
        //Constants.NEUTROPHIL_HALF_LIFE =  - Math.log(0.5) / (Double.parseDouble(args[3]) * (Constants.HOUR/((double) Constants.TIME_STEP_SIZE)));
        
        //Constants.Granule_HALF_LIFE = - Math.log(0.5) / (Double.parseDouble(args[4]) * (Constants.HOUR/((double) Constants.TIME_STEP_SIZE)));
        
        Constants.MAX_MA = Integer.parseInt(args[3]);
        Constants.MAX_N = Integer.parseInt(args[4]);
        
        
        
        
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        //initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, Integer.parseInt(input[4]));
        initialize.coInjuryInfect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.HYPHAE, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        //stat.setGrid(grid);


        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageReplenisher();
        recruiters[1] = new NeutrophilReplenisher();
        
        
        
        run.run( 
        		720,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null, //new File("/Users/henriquedeassis/eclipse-workspace/jISS_legacy/sample/out_" + args[5]), //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/baseClock.tsv"),
        		-1,
        		stat
        );
        
        stat.close();

        //System.out.println((toc - tic));
	}
	
	private static void baseCoinfectionScenario2(String[] args) throws InterruptedException {
		InitializeCoinfection initialize = new InitializeCoinfection();
		Run run = new RunSingleThread();
		PrintCoinfection stat = new PrintCoinfection();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "20", args[4], "640", "0"};
        //Constants.MAX_N = Constants.MAX_N;
        //Constants.MAX_MA = Constants.MAX_MA;
        
        //Constants.Kd_Granule = 2e12;
        
        //Constants.ITER_TO_GROW_FAST = Integer.parseInt(args[0]);
        Constants.ITER_TO_GROW = Integer.parseInt(args[0]);
        
        Constants.Kd_Granule = Double.parseDouble(args[2]) * 1e10;
        
        Constants.PR_N_HYPHAE = 0.0;//Double.parseDouble(args[1])*Constants.PR_N_HYPHAE;
        Constants.PR_MA_HYPHAE = Double.parseDouble(args[1])*Constants.PR_MA_HYPHAE;
        //Constants.MA_MOVE_RATE_ACT = Constants.MA_MOVE_RATE_ACT*10;
        //Constants.MA_MOVE_RATE_REST = Constants.MA_MOVE_RATE_ACT*10;
        
        //Constants.NEUTROPHIL_HALF_LIFE =  - Math.log(0.5) / (Double.parseDouble(args[3]) * (Constants.HOUR/((double) Constants.TIME_STEP_SIZE)));
        
        Constants.GRANULE_HALF_LIFE = 1+Math.log(0.5) / (Double.parseDouble(args[3]));// * (Constants.HOUR/((double) Constants.TIME_STEP_SIZE)));
        
        Constants.MAX_MA = Integer.parseInt(args[4]);
        Constants.MAX_N = Integer.parseInt(args[5]);
        
        
        
        
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        //initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, Integer.parseInt(input[4]));
        initialize.coInjuryInfect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.HYPHAE, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        //stat.setGrid(grid);


        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageReplenisher();
        recruiters[1] = new NeutrophilReplenisher();
        
        
        
        run.run( 
        		720,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null, //new File("/Users/henriquedeassis/eclipse-workspace/jISS_legacy/sample/pgout1_" + args[6]), //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/baseClock.tsv"),
        		-1,
        		stat
        );
        
        stat.close();

        //System.out.println((toc - tic));
	}
	
	
	private static void fitBaseModel(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeBaseModel();
		Run run = new RunSingleThread();
		PrintCoinfection stat = new PrintCoinfection();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640", "0"};
        //Constants.MAX_N = Constants.MAX_N;
        //Constants.MAX_MA = Constants.MAX_MA;
        
        //Constants.Kd_Granule = 2e12;
        
        //Constants.ITER_TO_GROW_FAST = Integer.parseInt(args[0]);
        //Constants.ITER_TO_GROW = Integer.parseInt(args[0]);
        
        Constants.Kd_Granule = Double.parseDouble(args[1]) * 1e10;
        
        Constants.PR_N_HYPHAE = 0.0;//Double.parseDouble(args[1])*Constants.PR_N_HYPHAE;
        Constants.PR_MA_HYPHAE = Double.parseDouble(args[2])*Constants.PR_MA_HYPHAE;
        //Constants.MA_MOVE_RATE_ACT = Constants.MA_MOVE_RATE_ACT*10;
        //Constants.MA_MOVE_RATE_REST = Constants.MA_MOVE_RATE_ACT*10;
        
        //Constants.NEUTROPHIL_HALF_LIFE =  - Math.log(0.5) / (Double.parseDouble(args[3]) * (Constants.HOUR/((double) Constants.TIME_STEP_SIZE)));
        
        Constants.GRANULE_HALF_LIFE = - Math.log(0.5) / (Double.parseDouble(args[3]));// * (Constants.HOUR/((double) Constants.TIME_STEP_SIZE)));
        
        Constants.MAX_MA = Integer.parseInt(args[4]);
        Constants.MAX_N = Integer.parseInt(args[5]);
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);


        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        
        run.run(
        		1440,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,
        		-1,
        		stat
        );
        
        stat.close();
        //System.out.println((toc - tic));
	}
	
	
	
	

	public static void main(String[] args) throws Exception {
		//Main.baseInvitroModel(new String[]{"64", args[0], args[1], args[2]});
		//Main.runHemeInVitro101221();
		///Main.runExperiment101221();
		//Main.runSA092221(args);
		//System.out.println("hello word!");
		Main.baseCoinfectionScenario1(args);
	}

}
