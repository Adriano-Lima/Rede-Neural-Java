run <- function(parametros, entrada, saida) {
  
  c = paste("javac Bmu.java Euclidiana.java Grafo.java Padrao.java Parametros.java Principal.java Rede.java Vizinho.java Sammon.java Calculo.java -Xlint");
  system(c);
  s = paste(saida,"dot",sep=".");
  p = paste("echo"," // ", entrada, parametros," >", s);
  system(p);
  cmd = paste("java Principal ", parametros, "< ", entrada," >>",s);
  system(cmd);
  f = paste(saida,"png",sep=".");
  fig = paste("dot -T png ",s," > ",f);
  system(fig);
}

# java Principal 0 0.35 6 3 0.7 0.1 15 0.0000001 1000 10 12 idh.cores idh.paises 1 < idh_2000.dat
# java Principal 0 0.35 6 6 0.7 0.1 15 0.0000001 1000 10 12 idh2010.cores idh2010.paises 1 < idh_2010.dat
# java Principal 0 0.35 4 3 0.7 0.1 15 0.0000001 1000 5 5 vinho2.cores teste 1 < vinho_normalizado.dat
# java Principal 0 0.35 4 3 0.7 0.1 15 0.0000001 1000 5 5 iris2.cores teste 1 < iris-normalizado2.dat
# java Principal 0 0.35 4 4 0.7 0.1 15 0.0000001 1000 5 5 enem2.cores teste 1 < enem_naoNormalizado2.dat
# java Principal 0 0.35 4 4 0.7 0.1 15 0.0000001 1000 5 5 enem2.cores teste 1 < enem_normalizado2.dat
# java Principal 0 0.35 4 5 0.7 0.1 15 0.0000001 1000 5 5 enem_privadas.cores teste 1 < enem_privadas.dat
# java Principal 0 0.35 4 4 0.7 0.1 15 0.0000001 1000 5 5 enem2.cores teste 2 < enem_matematica.dat

#     usarSammon MF Viz QuantTipos AlphaInitial AlphaFinal LambdaInitial LambdaFinal QuantIteracoesnoTreino LinhasRede ColunasRede ArquivoComCores ArquivoPaises
metodo=c("1 0.3 6 4 0.7 0.1 15 0.0000001 1000 6 6 enem2.cores teste 1");

file_in=c("enem_normalizado2.dat");
file_out=c("enem_cssom2");
#file_imagens = c("enem3.png","enem4.png","idh1.png");


executar <-function(){
	n  = length(file_in);
	for (i in 1: n){
		cat("Executando " ,file_in[i],"\n");
		run(metodo[i],file_in[i],file_out[i]);
	}
}