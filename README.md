# CAPA

CAPA1.jar runs scenario1

java -jar CAPA1.jar ITER_TO_GROW PR_N_KILL PR_MA_KILL NUM_MA NUM_N

ITER_TO_GROW is an integer number indicating the number of interactions it takes for a new septae to be generated.

PR_N_KILL a number between 0-1 is the probability of neutrophils killing hyphae

PR_MA_KILL a number between 0-1 is the probability of macrophages killing hyphae

NUM_MA is an integer indicating the number of macrophages

NUM_N is an integer indicating the number of neutrophils

example:

$ java -jar CAPA1.jar 118 0.0691232398232442 0.722728420708909 864 706

CAPA2.jar runs scenario2

java -jar CAPA2.jar ITER_TO_GROW PR_MA_KILL GRANULE_Kd GRANULE_HALF_LIFE NUM_MA NUM_N

ITER_TO_GROW is an integer number indicating the number of interactions it takes for a new septae to be generated.

PR_MA_KILL a number between 0-1 is the probability of macrophages killing hyphae

GRANULE_Kd a real number indicating the Kd of granule reaction

GRANULE_HALF_LIFE a real number indicating the half-life of granules in iterations. One iteration is 2 minutes.

NUM_MA is an integer indicating the number of macrophages

NUM_N is an integer indicating the number of neutrophils

example:

$ java -jar CAPA2.jar 170 0.0759564702152328 20019760755 22.093283994252 509 864
