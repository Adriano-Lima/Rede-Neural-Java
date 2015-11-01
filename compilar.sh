#!/bin/bash
javac Bmu.java  Euclidiana.java  Grafo.java  Padrao.java  Parametros.java  Principal.java  Rede.java Vizinho.java Sammon.java Calculo.java &&
java Principal 0 0.05 0.01 0 8 3 10 1 1000 < iris-normalizado2.dat > saida.txt