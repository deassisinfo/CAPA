package edu.uf.utils;

public class Constants {
    public static double D = 16;

    public static int TIME_STEP_SIZE = 2;// // minutes
    public static int HOUR = 60;// // minutes
    public static double UNIT_T = TIME_STEP_SIZE/30.0;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int ITER_TO_SWELLING = (4 * (HOUR/TIME_STEP_SIZE) - 2);// //6 // 8???
    public static double PR_ASPERGILLUS_CHANGE = -Math.log(0.5)/(6*(HOUR/TIME_STEP_SIZE));
    public static int ITER_TO_GERMINATE = 2 * (HOUR/TIME_STEP_SIZE) - 2;// //2 // $4???
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int ITER_TO_CHANGE_STATE = 2 * (HOUR/TIME_STEP_SIZE);// //4
    public static int ITER_TO_REST = 30*6;
    public static int ITER_TO_GROW = (HOUR/TIME_STEP_SIZE) - 1;// //1
    public static int ITER_TO_NEUTROPHIL_DIE = 12*HOUR/TIME_STEP_SIZE;


    public static double VOXEL_VOL = 6.4e-11;// // 0.26  // L
    public static double HYPHAE_VOL = 1.056832e-12;//  // L
    public static double CONIDIA_VOL = 4.844e-14;// //L //5.324E-015 //4.844e-14
    public static double MA_VOL = 4.849048e-12;//  // 4.849048e-15 //  --- WRONG!!! (4.849048e-12)
    public static double SPACE_VOL = 6.4e-8;//1.92488e-05;// 0.0003995827;//6.4e-8;//  //// FOR NOW!!!!

    ////ITER_TO_HEP_CHANGE = 10
    public static double PR_BRANCH = 0.25; 


    public static double TURNOVER_RATE = 1-Math.log(1.2)/(30/TIME_STEP_SIZE);////0.2 // 10.1124/jpet.118.250134 (approx) 0.2/h CHANGE!!!!
    ////ERYTHROCYTE_TURNOVER_RATE = 1-math.log(1.2)/int(30/TIME_STEP_SIZE)

    public static double LAC_QTTY = 4.3680464554587733e-17 * UNIT_T/2;// // REF 62  HALF-HOUR????

    public static double TAFC_UP = (1e-12/VOXEL_VOL) * UNIT_T/2;// // (cell^-1.h^-1) Ref 218, 224 (1e-12/VOXEL_VOL) -- NOT UNIT_T

    public static double TAFC_QTTY =  1e-15 * UNIT_T; //2e-15 * UNIT_T // REF 15  (Ref 2.7e-13 219)   1.74e-15//1.74e-12 //TAFC secretion rate  3.0659e-15
    public static double HEMOLYSIN_QTTY = 2.08e-10 * UNIT_T;//5e-9 * UNIT_T // Arbitrary Unities
    public static double MA_IL6_QTTY = 2.192281e-20 * UNIT_T / 3.0; // THIS IS CORRECT IT IS 3!!!!!!!
    public static double MA_IL8_QTTY = 7.609164e-19 * UNIT_T / 3.0;
    public static double MA_MCP1_QTTY = 2.635868e-20 * UNIT_T / 3.0;
    public static double MA_MIP1B_QTTY = 2.684384e-20 * UNIT_T / 3.0;
    public static double MA_MIP2_QTTY = 1.659086e-19 * UNIT_T / 3.0;
    public static double MA_IL10_QTTY = 1.046032e-21 * UNIT_T / 3.0;
    public static double MA_TNF_QTTY = 4.826850e-20 * UNIT_T / 3.0;
    public static double MA_TGF_QTTY = 1.517909e-21 * UNIT_T / 3.0;
    
    public static double MA_IL1_QTTY = 8.468887e-21 * UNIT_T / 3.0;

    public static double N_IL6_QTTY = 0.005875191*MA_IL6_QTTY;
    public static double N_IL8_QTTY = 0.005875191*MA_IL8_QTTY;
    public static double N_MIP2_QTTY = 0.005875191*MA_MIP2_QTTY;
    public static double N_TNF_QTTY = 0.005875191*MA_TNF_QTTY;
    
    public static double N_IL1_QTTY = 0.005875191*MA_IL1_QTTY;
    

    public static double P_IL6_QTTY = 1*MA_IL6_QTTY;
    public static double P_IL8_QTTY = 1*MA_IL8_QTTY;
    public static double P_MCP1_QTTY = 1*MA_MCP1_QTTY;
    public static double P_MIP1B_QTTY = 1*MA_MIP1B_QTTY;
    public static double P_MIP2_QTTY = 1*MA_MIP2_QTTY;
    public static double P_TNF_QTTY = 1*MA_TNF_QTTY;
    
    public static double P_IL1_QTTY = 1*MA_IL1_QTTY; 
    
    public static double L_HEP_QTTY = 6.9e-8;//3e-7;//1.945e-10*2.5;//2.3e-8;

    //public static double REST_HPX_QTTY = 1.948634e-05; //DOI:10.1016/0192-0561(84)90022-5
    //public static double REST_HP_QTTY = 2.190855e-07; //DOI:10.17221/8770-VETMED
    

    public static double IL6_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//1
    public static double IL8_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//4
    public static double MCP1_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//2
    public static double MIP1B_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//2
    public static double MIP2_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//2
    public static double IL10_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//3
    public static double TNF_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//0.5
    public static double TGF_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//0.1
    public static double IL1_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));// ?
    public static double HEP_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));// ?
    public static double ANTI_TNF_HALF_LIFE = 1+Math.log(0.5)/(5 * 24 * HOUR/((double) TIME_STEP_SIZE));// five days -- 10.1002/eji.1830180221
    public static double HEMOPEXIN_HALF_LIFE = 1+Math.log(0.5)/(1.2 * 24 * HOUR/((double) TIME_STEP_SIZE)); // 1.2 days -- 10.1006/abbi.1993.1014

    public static double Kd_IL6 = 3.3e-10;
    public static double Kd_IL8 = 1.045e-9;
    public static double Kd_MCP1 = 5.686549e-10;
    public static double Kd_MIP1B = 1.8e-10;
    public static double Kd_MIP2 = 9.166739837e-11;
    public static double Kd_IL10 = 1.4e-10;
    public static double Kd_TNF = 3.26e-10;
    public static double Kd_TGF = 2.65e-11;
    public static double Kd_KC = 1.0e-9; //doi/10.1074/jbc.M112.443762
    public static double Kd_Hep = 8.55e-07; // REF 223
    public static double Kd_LIP = 7.90456448805514E-05; //2.762975e-05
    public static double Kd_HEMO = 19.987634208144584;//185.18518518518516/2 // Arbitrary Unitis x h^-1 10.1016/j.ijmm.2011.04.016
    public static double Kd_MA_IRON = 0.0020799959084752307;
    
    public static double Kd_IL1 = 3.9e-9; //
    public static double Kd_TfR2 = 2.7e-8;

    public static double MA_IRON_IMPORT_RATE = 5.3333e-12/VOXEL_VOL; // L*cell^-1*h^-1. REF 27 
    public static double MA_IRON_EXPORT_RATE = 1367.3051298168639/VOXEL_VOL; //calculations made based on an MA iron concentration of 2.08 mM and 6.5 uM of apo-Tf (see Pedro's paper)
    public static double MA_HEME_IMPORT_RATE = 0.0833328125;

    //TIME_STEP = 30
    public static int CYT_BIND_T = 35; // SEE MANUAL (REF 199)
    public static int IRON_AFNT_T = 60;
    public static int IRON_IMP_EXP_T = 60;
    public static int PHAG_AFNT_T = 1;
    public static int HYPHAE_INT_T = 60;
    public static double REL_CYT_BIND_UNIT_T = TIME_STEP_SIZE/((double) CYT_BIND_T);
    public static double REL_IRON_AFNT_UNIT_T = TIME_STEP_SIZE/((double) IRON_AFNT_T);
    public static double REL_PHAG_AFNT_UNIT_T = TIME_STEP_SIZE/((double) PHAG_AFNT_T);
    public static double REL_IRON_IMP_EXP_UNIT_T = TIME_STEP_SIZE/((double) IRON_IMP_EXP_T);
    public static double REL_HYPHAEL_INT_UNIT_T = TIME_STEP_SIZE/((double) PHAG_AFNT_T);
    public static double REL_N_HYPHAE_INT_UNIT_T = TIME_STEP_SIZE/((double) HOUR);
    public static double STD_UNIT_T = TIME_STEP_SIZE/((double) HOUR);

    public static double DRIFT_BIAS = 1e-100; //0.9 // REF 156 (Est.)
    public static double PR_MOVE_ASP = 0.75; // DUMMY
    public static double MA_MOVE_RATE_REST = 1.44*TIME_STEP_SIZE/40.0; // 1.44--doi:10.1371/journal.pone.0004693 -NOT
    public static double MA_MOVE_RATE_ACT = 1.44*TIME_STEP_SIZE/40.0;
    public static double REC_BIAS = 0.9995; // DUMMY VALUE CREATED TO AVOID INFINTY LOOP!

    public static double MAX_N = 522.5;//157162;//3262500;//522.5;//15 // for an alveoli sac with 6 alveoli (Review)
    public static double MAX_MA = 209;//62925;//1306250;//209;//
    public static double MIN_MA = 15;//4817;//100000;//15;//
    //MA_MAX = 360 // for an alveoli sac with 6 alveoli (Review) using MIP-1b

    //N_REC = 1.0884e-11
    //MA_REC = 5*N_REC // the bigger the "worst"
    public static double NUM_ALVEOLI = 3.26e+06; //REF 242, 184

    //IRON_EXP_RATE = 7.035e4 // mol*cell^-1*h^-1 (It is based on mol not molar). Total Iron inside a macrophage: 8.713E-21 mols.
    //IRON_IMP_RATE = 0.322   // mol*cell^-1*h^-1 (It is based on mol not molar). Total Iron in a voxel (40x40x40): 1.904E-15 mols.

    public static double PR_MA_PHAG =  1 - Math.exp(-(1/VOXEL_VOL)*REL_PHAG_AFNT_UNIT_T/1.32489230813214E+10); // 30 min --> 1 - exp(-cells*t/Kd) --> Kd = 1.32489230813214E+10
    public static double PR_N_PHAG =   1 - Math.exp(-(1/VOXEL_VOL)*REL_PHAG_AFNT_UNIT_T/1.96177129709014E+11); // 30 min --> Kd = 1.96177129709014E+11
    public static double PR_E_PHAG =   1 - Math.exp(-(1/VOXEL_VOL)*REL_PHAG_AFNT_UNIT_T/6.80269538729756E+11); // 30 min --> Kd = 6.80269538729756E+11
    public static double PR_N_HYPHAE = 1 - Math.exp(-(1/VOXEL_VOL)*REL_N_HYPHAE_INT_UNIT_T/2.02201143330207E+09); // 0.5 h --> Kd = 2.02201143330207E+09
    public static double PR_MA_HYPHAE = 1 - Math.exp(-(1/VOXEL_VOL)*REL_N_HYPHAE_INT_UNIT_T/5.02201143330207E+9);  //Kd ~10x Neut. (REF 71)
    //PR_MA_HYPHAE = 1 - math.exp(-(1 / VOXEL_VOL) * REL_N_HYPHAE_INT_UNIT_T / 3.3333e+10)  // 0.5 h --> Kd = 2.02201143330207E+09
    public static double PR_MA_PHAG_ERYT = 1 - Math.exp(-(1/VOXEL_VOL)*REL_N_HYPHAE_INT_UNIT_T/73873893586.4061); //https://doi.org/10.1002/(SICI)1097-0320(19971015)30:5<269::AID-CYTO8>3.0.CO;2-C
    public static double PR_P_INT = PR_E_PHAG; //DUMMY VALUE???


    public static int MA_MAX_CONIDIA = 18;
    public static int N_MAX_CONIDIA = 3;
    public static int E_MAX_CONIDIA = 18;

    public static double PR_KILL = -Math.log(0.01)/(12*HOUR/((double) TIME_STEP_SIZE)); //10.1080/13693780400029247

    public static double K_M_TF_TAFC = 2.514985e-3;
    public static double K_M_TF_LAC = 2.5052031141601793e-3; ////// REF 62 (2.5-10% uptake in 1-4h)


    public static double MA_INTERNAL_IRON = 1.0086e-14; // 2.08e-3*4.849048e-12 (CORRECT VALUE)
    public static double CONIDIA_INIT_IRON = Kd_LIP * CONIDIA_VOL;

    public static double NEUTROPHIL_HALF_LIFE = - Math.log(0.5) / (6 * (HOUR/((double) TIME_STEP_SIZE))); // 12h (half-life infection) doi.org/10.1189/jlb.1112571
    public static double MA_HALF_LIFE = -Math.log(0.5) / ( 1 * 24 * (HOUR/((double) TIME_STEP_SIZE)));

    //MA_OCCUPANCY = 6 // ~6 times bigger then neutrophils

    //SECRETING_HALF_LIFE = 0.9

    //PR_INT_RESTING = 0.15
    //PR_INT_ACTIVE = 0.3
    //MAX_INTERNALIZED_CONIDIA = 50

    //TF_ENHANCE = 2

    //PHAGOSOME_UP_RATE = 0.025

    public static double P1 =  0.2734;
    public static double P2 = -1.1292;
    public static double P3 =  0.8552;

    public static double IL6_THRESHOLD = 1.372243e-10;//1e-11 // 5e-14 //Molar (threshold to activate liver)
    public static double HEP_INTERCEPT = -0.3141; // [IL6] > 1e-11
    public static double HEP_SLOPE = 0.7793; // [IL6] > 1e-11

    public static double TF_INTERCEPT = -1.194e-05;
    public static double TF_SLOPE = -5.523e-06;
    public static double THRESHOLD_LOG_HEP = -8;
    public static double THRESHOLD_HEP = Math.pow(10, THRESHOLD_LOG_HEP); 

    public static double DEFAULT_APOTF_REL_CONCENTRATION = 0.4;
    public static double DEFAULT_TFFE_REL_CONCENTRATION = 0.1657;
    public static double DEFAULT_TFFE2_REL_CONCENTRATION = 0.4343;

    public static double DEFAULT_TF_CONCENTRATION = (TF_INTERCEPT + TF_SLOPE * THRESHOLD_LOG_HEP) * VOXEL_VOL; //1.3490016161826808e-16
    public static double DEFAULT_APOTF_CONCENTRATION = DEFAULT_APOTF_REL_CONCENTRATION * DEFAULT_TF_CONCENTRATION;
    public static double DEFAULT_TFFE_CONCENTRATION = DEFAULT_TFFE_REL_CONCENTRATION * DEFAULT_TF_CONCENTRATION;
    public static double DEFAULT_TFFE2_CONCENTRATION = DEFAULT_TFFE2_REL_CONCENTRATION * DEFAULT_TF_CONCENTRATION;

    //MA_DEFAULT_IRON_CONCENTRATION = 2.08e-3

    public static double RECRUITMENT_RATE = 4.882812e+14;//4.882812e+14;//2.0;//2.9333567478400004//0.5e18//3.0e18 * STD_UNIT_T //1.5e+19  // 0.04 //0.04 ATTENTION HALVED!

    public static double N_REC_MUL = 1.0;
    public static double MA_REC_MUL = 1.0;

    public static double N_FRAC = 1.0;
    //N_REC_RATE = 1.2054830189634535e+19  // 0.2


    public static int ANTI_TNFA_REACT_TIME_UNIT = 120; //sec
    public static double K_M_ANTI_TNFA = 6.9737e-07;  // (M^-1.sec^-1)  http://www.jimmunol.org/content/162/10/6040.full//ref-list-1
    public static double ANTI_TNFA_SYSTEM_CONCENTRATION_REF = 2e-8*VOXEL_VOL;
    public static double ANTI_TNFA_SYSTEM_CONCENTRATION = ANTI_TNFA_SYSTEM_CONCENTRATION_REF; // https://doi.org/10.1016/0022-1759(95)00278-2


    public static double HEMOGLOBIN_UPTAKE_RATE = (1e-13/VOXEL_VOL) * UNIT_T; // TAFC_UP
    public static double ERYTROCYTE_HEMOGLOBIN_CONCENTRATION = 4.6875e-16; //mol
    public static int MAX_ERYTHROCYTE_VOXEL =  239;//180; 
    public static int ERYTHROCYTE_TURNOVER_RATE = 239;// RBC/time-step //ON THE AMOUNT OF BLOOD IN THE LUNGS. By YAS KUNO, 1917
    

    public static double ESTB_KM = 4e-4; //10.1128/EC.00066-07 
    public static double ESTB_HALF_LIFE = 1+Math.log(0.5)/(24.0 * HOUR/((double) TIME_STEP_SIZE));//2.9 - Advances in Enzymology and related Areas of Molecular Biology, Alton Meister, v39, 1973, p227
    public static double ESTB_SYSTEM_CONCENTRATION = 0;
    public static double ESTB_KCAT = 3096; // 10.1128/AEM.65.8.3470-3472.1999 (GENERIC FUNGI ESTERASE)

    public static double HEMOPEXIN_SYSTEM_CONCENTRATION = 0; // NOT CORRECT
    public static double HEMOPEXIN_SYSTEM_CONCENTRATION_REF = 3.1e-7 * VOXEL_VOL; // (Luis SV and 10.3181/00379727-75-18083)
    public static double HEMOPEXIN_KM = 1e-9; // Kd < 1pM -- 10.1006/abbi.1993.1014 (Kd is not Km!!!)
    public static double HEMOPEXIN_KCAT = 100;

    //HEMOLYSIS_RATE = 0.0005
    public static boolean HEMOLYSIN = false;

    public static int ITERATION_TO_KILL = 15;

    public static int HEMORRHAGE_DELAY = 0;
    
    
    /* ********* NEW PARAMETERS ********** */
    
    public static double HEME_SYSTEM_CONCENTRATION = 9.934e-5 * VOXEL_VOL; //from Borna's data
    public static double HEME_TURNOVER_RATE = 3e-2; //~fit to Borna's data
    
    public static double HEMOLYSIS_RATE = 0.1674/30*2.64; //first approx. -- Use Borna's data to fit
    public static double Kd_HP = 1;//3.657392737e-6; //10.1111/j.1365-2567.2004.02071.x
    public static double Kd_HPX = 9.74317e-7; // https://doi.org/10.1189/jlb.1208742
    
    public static double Kd_Heme = 3e-5;//9.74317e-5; //DOI:10.1074/jbc.M610737200
    
    public static double KM_HP = 2e-4; // same as PHX (DUMMY)
    public static double KM_HPX = 2e-4; //??? (it was abs) https://doi.org/10.1101/2020.04.16.044321;
    public static double KCAT_HPX = 120;
    
    public static double K_HB = 2*0.085976/30.0; //step^-1 Borna's data Lung Heme/hemoglobin Figure
    
    public static double HEME_UP = 0.02*2*8.153e-15/VOXEL_VOL; //10.1099/mic.0.26108-0 (Haemin C. albicans - Calculate based on 40 min delta) per min therefore timse 2
    
    public static double DEFAULT_HPX_CONCENTRATION = 6.24e-16; //BORNA UNPUBLISHED RESULTS HPX/BAL (ALREADY MUL BY VOXEL VOL)
    public static double DEFAULT_HP_CONCENTRATION = 0.015*6.24e-16;  //(ALREADY MUL BY VOXEL VOL)
    
    
    public static double L_HP_QTTY = 10*4e-8;//1.2e-5;//3.443492e-05; //ROUGH APPROX based on V.max. DOI:10.1016/j.endinu.2018.07.008
    public static double L_HPX_QTTY = 10*9.74e-6*3;//1.8706e-14;//1.948634e-05; //ROUGH APPROX based on normal levels of hpx in mice: DOI:10.1016/0192-0561(84)90022-5
    public static double L_REST_HPX_QTTY = 9.74e-6*3; //BORNA UNPUBLISHED RESULTS HPX/BAL
    public static double L_REST_HP_QTTY = 4e-8; //DOI:10.17221/8770-VETMED corrected by BORNA UNPUBLISH. 
    
    
    
    
    public static double REDUCTIVE_IRON_ASSIMILATION_RATE = 5.208333e-08; //TAFC/10000 //DUMMY
    public static double KM_IRON = 1.428571e-05; //10.1128/jb.169.8.3664-3668.1987
    public static double KCAT_IRON = 1; //10.1128/jb.169.8.3664-3668.1987
    public static double MIN_FREE_IRON = 1e-9*VOXEL_VOL; //1e-17???
    public static double GLU_UPTAKE_RATE = 5.208333e-07; //TAFC/1000 //DUMMY
    public static double Kd_GLU = 5e-3*VOXEL_VOL; //DUMMY
    
    
    public static double INIT_GLU = 1e-5*VOXEL_VOL; //DUMMY
    public static double INIT_TIN_PROTOPORPHYRIN = 1e-5*VOXEL_VOL; //DUMMY
    public static double INIT_HEME = 0;
    public static double INIT_IRON = MIN_FREE_IRON;
    
    public static double Kd_GROW = Kd_LIP / 10.0;
    
    
    
    
    /** COVID-19 PARAMETERS**/
    
    public static double PR_INT_NK_P = 0.05; //DUMMY
    public static double IFN1_QTTY = 4.95e-19; //DUMMY: BASED ON Kd
    public static double IFN1_INHIBITION = 0.8;//DUMMY
    public static double Kd_IFN1 = 3.34e-9; //https://doi.org/10.1074/jbc.M116.773788 //CHECK BETTER
    public static double DEFAULT_VIRAL_REPLICATION_RATE = 1.01;//DUMMY
    public static double IFN1_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//1
    public static double MAX_VIRAL_LOAD = 1e-10;//DUMMY
    public static double VIRAL_UPTAKE_RATE = 0.01;//DUMMY
    public static double Kd_Covid = 1e-20;//DUMMY
    public static double MAX_NK = 21; //DUMMY;
    
    
    public static double GRANULE_QTTY = 1;
    public static double Kd_Granule = 1;
    public static double GRANULE_HALF_LIFE = 1+Math.log(0.5);
    
    
}



/*
 * package edu.uf.utils;

public interface Constants {
    public static final double D = 16;

    public static final int TIME_STEP_SIZE = 2;// // minutes
    public static final int HOUR = 60;// // minutes
    public static final double UNIT_T = TIME_STEP_SIZE/30.0;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final int ITER_TO_SWELLING = 1;//(4 * (HOUR/TIME_STEP_SIZE) - 2);// //6 // 8???
    public static final double PR_ASPERGILLUS_CHANGE = -Math.log(0.5)/(6*(HOUR/TIME_STEP_SIZE));
    public static final int ITER_TO_GERMINATE = 2 * (HOUR/TIME_STEP_SIZE) - 2;// //2 // $4???
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final int ITER_TO_CHANGE_STATE = 2 * (HOUR/TIME_STEP_SIZE);// //4
    public static final int ITER_TO_REST = 30*6;
    public static final int ITER_TO_GROW = (HOUR/TIME_STEP_SIZE) - 1;// //1
    public static final int ITER_TO_NEUTROPHIL_DIE = 12*HOUR/TIME_STEP_SIZE;


    public static final double VOXEL_VOL = 6.4e-11;// // 0.26  // L
    public static final double HYPHAE_VOL = 1.056832e-12;//  // L
    public static final double CONIDIA_VOL = 4.844e-14;// //L //5.324E-015 //4.844e-14
    public static final double MA_VOL = 4.849048e-12;//  // 4.849048e-15 //  --- WRONG!!! (4.849048e-12)
    public static final double SPACE_VOL = 6.4e-8;//1.92488e-05;// 0.0003995827;//6.4e-8;//  //// FOR NOW!!!!

    ////ITER_TO_HEP_CHANGE = 10
    public static final double PR_BRANCH = 0.25; 


    public static final double TURNOVER_RATE = 1-Math.log(1.2)/(30/TIME_STEP_SIZE);////0.2 // 10.1124/jpet.118.250134 (approx) 0.2/h CHANGE!!!!
    ////ERYTHROCYTE_TURNOVER_RATE = 1-math.log(1.2)/int(30/TIME_STEP_SIZE)

    public static final double LAC_QTTY = 4.3680464554587733e-17 * UNIT_T/2;// // REF 62  HALF-HOUR????

    public static final double TAFC_UP = (1e-12/VOXEL_VOL) * UNIT_T/2;// // (cell^-1.h^-1) Ref 218, 224 (1e-12/VOXEL_VOL) -- NOT UNIT_T

    public static final double TAFC_QTTY =  1e-15 * UNIT_T; //2e-15 * UNIT_T // REF 15  (Ref 2.7e-13 219)   1.74e-15//1.74e-12 //TAFC secretion rate  3.0659e-15
    public static final double HEMOLYSIN_QTTY = 2.08e-10 * UNIT_T;//5e-9 * UNIT_T // Arbitrary Unities
    public static final double MA_IL6_QTTY = 2.192281e-20 * UNIT_T / 3.0; // THIS IS CORRECT IT IS 3!!!!!!!
    public static final double MA_IL8_QTTY = 7.609164e-19 * UNIT_T / 3.0;
    public static final double MA_MCP1_QTTY = 2.635868e-20 * UNIT_T / 3.0;
    public static final double MA_MIP1B_QTTY = 2.684384e-20 * UNIT_T / 3.0;
    public static final double MA_MIP2_QTTY = 1.659086e-19 * UNIT_T / 3.0;
    public static final double MA_IL10_QTTY = 1.046032e-21 * UNIT_T / 3.0;
    public static final double MA_TNF_QTTY = 4.826850e-20 * UNIT_T / 3.0;
    public static final double MA_TGF_QTTY = 1.517909e-21 * UNIT_T / 3.0;
    
    public static final double MA_IL1_QTTY = 8.468887e-21 * UNIT_T / 3.0;

    public static final double N_IL6_QTTY = 0.005875191*MA_IL6_QTTY;
    public static final double N_IL8_QTTY = 0.005875191*MA_IL8_QTTY;
    public static final double N_MIP2_QTTY = 0.005875191*MA_MIP2_QTTY;
    public static final double N_TNF_QTTY = 0.005875191*MA_TNF_QTTY;
    
    public static final double N_IL1_QTTY = 0.005875191*MA_IL1_QTTY;
    

    public static final double P_IL6_QTTY = 1*MA_IL6_QTTY;
    public static final double P_IL8_QTTY = 1*MA_IL8_QTTY;
    public static final double P_MCP1_QTTY = 1*MA_MCP1_QTTY;
    public static final double P_MIP1B_QTTY = 1*MA_MIP1B_QTTY;
    public static final double P_MIP2_QTTY = 1*MA_MIP2_QTTY;
    public static final double P_TNF_QTTY = 1*MA_TNF_QTTY;
    
    public static final double P_IL1_QTTY = 1*MA_IL1_QTTY;
    
    public static final double L_HEP_QTTY = 6.9e-8;//3e-7;//1.945e-10*2.5;//2.3e-8;
    public static final double L_HP_QTTY = 3.443492e-05; //ROUGH APPROX based on V.max. DOI:10.1016/j.endinu.2018.07.008
    public static final double L_HPX_QTTY = 1.948634e-05; //ROUGH APPROX based on normal levels of hpx in mice: DOI:10.1016/0192-0561(84)90022-5
    //public static final double REST_HPX_QTTY = 1.948634e-05; //DOI:10.1016/0192-0561(84)90022-5
    public static final double REST_HP_QTTY = 2.190855e-07; //DOI:10.17221/8770-VETMED
    

    public static final double IL6_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//1
    public static final double IL8_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//4
    public static final double MCP1_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//2
    public static final double MIP1B_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//2
    public static final double MIP2_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//2
    public static final double IL10_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//3
    public static final double TNF_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//0.5
    public static final double TGF_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//0.1
    public static final double IL1_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));// ?
    public static final double HEP_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));// ?
    public static final double ANTI_TNF_HALF_LIFE = 1+Math.log(0.5)/(5 * 24 * HOUR/((double) TIME_STEP_SIZE));// five days -- 10.1002/eji.1830180221
    public static final double HEMOPEXIN_HALF_LIFE = 1+Math.log(0.5)/(1.2 * 24 * HOUR/((double) TIME_STEP_SIZE)); // 1.2 days -- 10.1006/abbi.1993.1014

    public static final double Kd_IL6 = 3.3e-10;
    public static final double Kd_IL8 = 1.045e-9;
    public static final double Kd_MCP1 = 5.686549e-10;
    public static final double Kd_MIP1B = 1.8e-10;
    public static final double Kd_MIP2 = 9.166739837e-11;
    public static final double Kd_IL10 = 1.4e-10;
    public static final double Kd_TNF = 3.26e-10;
    public static final double Kd_TGF = 2.65e-11;
    public static final double Kd_KC = 1.0e-9; //doi/10.1074/jbc.M112.443762
    public static final double Kd_Hep = 8.55e-07; // REF 223
    public static final double Kd_LIP = 7.90456448805514E-05; //2.762975e-05
    public static final double Kd_HEMO = 19.987634208144584;//185.18518518518516/2 // Arbitrary Unitis x h^-1 10.1016/j.ijmm.2011.04.016
    public static final double Kd_MA_IRON = 0.0020799959084752307;
    
    public static final double Kd_IL1 = 3.9e-9; //
    public static final double Kd_TfR2 = 2.7e-8;

    public static final double MA_IRON_IMPORT_RATE = 5.3333e-12/VOXEL_VOL; // L*cell^-1*h^-1. REF 27
    public static final double MA_IRON_EXPORT_RATE = 1367.3051298168639/VOXEL_VOL; //calculations made based on an MA iron concentration of 2.08 mM and 6.5 uM of apo-Tf (see Pedro's paper)
    public static final double MA_HEME_IMPORT_RATE = 0.0833328125;

    //TIME_STEP = 30
    public static final int CYT_BIND_T = 35; // SEE MANUAL (REF 199)
    public static final int IRON_AFNT_T = 60;
    public static final int IRON_IMP_EXP_T = 60;
    public static final int PHAG_AFNT_T = 1;
    public static final int HYPHAE_INT_T = 60;
    public static final double REL_CYT_BIND_UNIT_T = TIME_STEP_SIZE/((double) CYT_BIND_T);
    public static final double REL_IRON_AFNT_UNIT_T = TIME_STEP_SIZE/((double) IRON_AFNT_T);
    public static final double REL_PHAG_AFNT_UNIT_T = TIME_STEP_SIZE/((double) PHAG_AFNT_T);
    public static final double REL_IRON_IMP_EXP_UNIT_T = TIME_STEP_SIZE/((double) IRON_IMP_EXP_T);
    public static final double REL_HYPHAEL_INT_UNIT_T = TIME_STEP_SIZE/((double) PHAG_AFNT_T);
    public static final double REL_N_HYPHAE_INT_UNIT_T = TIME_STEP_SIZE/((double) HOUR);
    public static final double STD_UNIT_T = TIME_STEP_SIZE/((double) HOUR);

    public static final double DRIFT_BIAS = 1e-100; //0.9 // REF 156 (Est.)
    public static final double PR_MOVE_ASP = 0.75; // DUMMY
    public static final double MA_MOVE_RATE_REST = 1.44*TIME_STEP_SIZE/40.0; // 1.44--doi:10.1371/journal.pone.0004693 -NOT
    public static final double MA_MOVE_RATE_ACT = 1.44*TIME_STEP_SIZE/40.0;
    public static final double REC_BIAS = 0.9995; // DUMMY VALUE CREATED TO AVOID INFINTY LOOP!

    public static final double MAX_N = 522.5;//157162;//3262500;//522.5;//15 // for an alveoli sac with 6 alveoli (Review)
    public static final double MAX_MA = 209;//62925;//1306250;//209;//
    public static final double MIN_MA = 15;//4817;//100000;//15;//
    //MA_MAX = 360 // for an alveoli sac with 6 alveoli (Review) using MIP-1b

    //N_REC = 1.0884e-11
    //MA_REC = 5*N_REC // the bigger the "worst"
    public static final double NUM_ALVEOLI = 3.26e+06; //REF 242, 184

    //IRON_EXP_RATE = 7.035e4 // mol*cell^-1*h^-1 (It is based on mol not molar). Total Iron inside a macrophage: 8.713E-21 mols.
    //IRON_IMP_RATE = 0.322   // mol*cell^-1*h^-1 (It is based on mol not molar). Total Iron in a voxel (40x40x40): 1.904E-15 mols.

    public static final double PR_MA_PHAG =  1 - Math.exp(-(1/VOXEL_VOL)*REL_PHAG_AFNT_UNIT_T/1.32489230813214E+10); // 30 min --> 1 - exp(-cells*t/Kd) --> Kd = 1.32489230813214E+10
    public static final double PR_N_PHAG =   1 - Math.exp(-(1/VOXEL_VOL)*REL_PHAG_AFNT_UNIT_T/1.96177129709014E+11); // 30 min --> Kd = 1.96177129709014E+11
    public static final double PR_E_PHAG =   1 - Math.exp(-(1/VOXEL_VOL)*REL_PHAG_AFNT_UNIT_T/6.80269538729756E+11); // 30 min --> Kd = 6.80269538729756E+11
    public static final double PR_N_HYPHAE = 1 - Math.exp(-(1/VOXEL_VOL)*REL_N_HYPHAE_INT_UNIT_T/2.02201143330207E+09); // 0.5 h --> Kd = 2.02201143330207E+09
    public static final double PR_MA_HYPHAE = 1 - Math.exp(-(1/VOXEL_VOL)*REL_N_HYPHAE_INT_UNIT_T/5.02201143330207E+9);  //Kd ~10x Neut. (REF 71)
    //PR_MA_HYPHAE = 1 - math.exp(-(1 / VOXEL_VOL) * REL_N_HYPHAE_INT_UNIT_T / 3.3333e+10)  // 0.5 h --> Kd = 2.02201143330207E+09
    public static final double PR_MA_PHAG_ERYT = 1 - Math.exp(-(1/VOXEL_VOL)*REL_N_HYPHAE_INT_UNIT_T/73873893586.4061); //https://doi.org/10.1002/(SICI)1097-0320(19971015)30:5<269::AID-CYTO8>3.0.CO;2-C
    public static final double PR_P_INT = PR_E_PHAG; //DUMMY VALUE???


    public static final int MA_MAX_CONIDIA = 18;
    public static final int N_MAX_CONIDIA = 3;
    public static final int E_MAX_CONIDIA = 18;

    public static final double PR_KILL = -Math.log(0.01)/(12*HOUR/((double) TIME_STEP_SIZE)); //10.1080/13693780400029247

    public static final double K_M_TF_TAFC = 2.514985e-3;
    public static final double K_M_TF_LAC = 2.5052031141601793e-3; ////// REF 62 (2.5-10% uptake in 1-4h)


    public static final double MA_INTERNAL_IRON = 1.0086e-14; // 2.08e-3*4.849048e-12 (CORRECT VALUE)
    public static final double CONIDIA_INIT_IRON = Kd_LIP * CONIDIA_VOL;

    public static final double NEUTROPHIL_HALF_LIFE = - Math.log(0.5) / (6 * (HOUR/((double) TIME_STEP_SIZE))); // 12h (half-life infection) doi.org/10.1189/jlb.1112571
    public static final double MA_HALF_LIFE = -Math.log(0.5) / ( 1 * 24 * (HOUR/((double) TIME_STEP_SIZE)));

    //MA_OCCUPANCY = 6 // ~6 times bigger then neutrophils

    //SECRETING_HALF_LIFE = 0.9

    //PR_INT_RESTING = 0.15
    //PR_INT_ACTIVE = 0.3
    //MAX_INTERNALIZED_CONIDIA = 50

    //TF_ENHANCE = 2

    //PHAGOSOME_UP_RATE = 0.025

    public static final double P1 =  0.2734;
    public static final double P2 = -1.1292;
    public static final double P3 =  0.8552;

    public static final double IL6_THRESHOLD = 1.372243e-10;//1e-11 // 5e-14 //Molar (threshold to activate liver)
    public static final double HEP_INTERCEPT = -0.3141; // [IL6] > 1e-11
    public static final double HEP_SLOPE = 0.7793; // [IL6] > 1e-11

    public static final double TF_INTERCEPT = -1.194e-05;
    public static final double TF_SLOPE = -5.523e-06;
    public static final double THRESHOLD_LOG_HEP = -8;
    public static final double THRESHOLD_HEP = Math.pow(10, THRESHOLD_LOG_HEP); 

    public static final double DEFAULT_APOTF_REL_CONCENTRATION = 0.4;
    public static final double DEFAULT_TFFE_REL_CONCENTRATION = 0.1657;
    public static final double DEFAULT_TFFE2_REL_CONCENTRATION = 0.4343;

    public static final double DEFAULT_TF_CONCENTRATION = (TF_INTERCEPT + TF_SLOPE * THRESHOLD_LOG_HEP) * VOXEL_VOL; //1.3490016161826808e-16
    public static final double DEFAULT_APOTF_CONCENTRATION = DEFAULT_APOTF_REL_CONCENTRATION * DEFAULT_TF_CONCENTRATION;
    public static final double DEFAULT_TFFE_CONCENTRATION = DEFAULT_TFFE_REL_CONCENTRATION * DEFAULT_TF_CONCENTRATION;
    public static final double DEFAULT_TFFE2_CONCENTRATION = DEFAULT_TFFE2_REL_CONCENTRATION * DEFAULT_TF_CONCENTRATION;

    //MA_DEFAULT_IRON_CONCENTRATION = 2.08e-3

    public static final double RECRUITMENT_RATE = 4.882812e+14;//4.882812e+14;//2.0;//2.9333567478400004//0.5e18//3.0e18 * STD_UNIT_T //1.5e+19  // 0.04 //0.04 ATTENTION HALVED!

    public static final double N_REC_MUL = 1.0;
    public static final double MA_REC_MUL = 1.0;

    public static final double N_FRAC = 1.0;
    //N_REC_RATE = 1.2054830189634535e+19  // 0.2


    public static final int ANTI_TNFA_REACT_TIME_UNIT = 120; //sec
    public static final double K_M_ANTI_TNFA = 6.9737e-07;  // (M^-1.sec^-1)  http://www.jimmunol.org/content/162/10/6040.full//ref-list-1
    public static final double ANTI_TNFA_SYSTEM_CONCENTRATION_REF = 2e-8*VOXEL_VOL;
    public static final double ANTI_TNFA_SYSTEM_CONCENTRATION = ANTI_TNFA_SYSTEM_CONCENTRATION_REF; // https://doi.org/10.1016/0022-1759(95)00278-2


    public static final double HEMOGLOBIN_UPTAKE_RATE = (1e-13/VOXEL_VOL) * UNIT_T; // TAFC_UP
    public static final double ERYTROCYTE_HEMOGLOBIN_CONCENTRATION = 4.6875e-16; //mol
    public static final double MAX_ERYTHROCYTE_VOXEL = 180;


    public static final double ESTB_KM = 4e-4; //10.1128/EC.00066-07 
    public static final double ESTB_HALF_LIFE = 1+Math.log(0.5)/(24.0 * HOUR/((double) TIME_STEP_SIZE));//2.9 - Advances in Enzymology and related Areas of Molecular Biology, Alton Meister, v39, 1973, p227
    public static final double ESTB_SYSTEM_CONCENTRATION = 0;
    public static final double ESTB_KCAT = 3096; // 10.1128/AEM.65.8.3470-3472.1999 (GENERIC FUNGI ESTERASE)

    public static final double HEMOPEXIN_SYSTEM_CONCENTRATION = 0; // NOT CORRECT
    public static final double HEMOPEXIN_SYSTEM_CONCENTRATION_REF = 3.1e-7 * VOXEL_VOL; // (Luis SV and 10.3181/00379727-75-18083)
    public static final double HEMOPEXIN_KM = 1e-9; // Kd < 1pM -- 10.1006/abbi.1993.1014 (Kd is not Km!!!)
    public static final double HEMOPEXIN_KCAT = 100;

    //HEMOLYSIS_RATE = 0.0005
    public static final boolean HEMOLYSIN = false;

    public static final int ITERATION_TO_KILL = 15;

    public static final int HEMORRHAGE_DELAY = 0;
    
    

    
    public static final double HEMOLYSIS_RATE = 0.00025; //DUMMY
    public static final double Kd_HP = 1e-13; //DUMMY
    public static final double Kd_HPX = 1e-13; //DUMMY
    
    public static final double KM_HP = 1e-3; //DUMMY
    public static final double KM_HPX = 1e-3; //DUMMY
    
    public static final double K_HB = 0.05; //DUMMY
    
    public static final double HEME_UP = 2*(1e-13/VOXEL_VOL) * UNIT_T; //DUMMY -- TAFC
    
    
}

 */
