#!/bin/bash
javac Bmu.java  Euclidiana.java  Grafo.java  Padrao.java  Parametros.java  Principal.java  Rede.java Vizinho.java Sammon.java Calculo.java -Xlint &&
#     usarSammon MF Viz QuantTipos AlphaInitial AlphaFinal LambdaInitial LambdaFinal QuantIteracoesnoTreino LinhasRede ColunasRede ArquivoComCores 
java Principal 0 0.35 4 3 0.7 0.1 15 0.0000001 1000 5 5 iris2.cores teste 1 < iris-normalizado2.dat


#utilizar o arquivo experimentos.R para compilar e executar 
