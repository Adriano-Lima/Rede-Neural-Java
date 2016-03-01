run <- function(arquivo, saida) {
  
  s = paste("echo ",arquivo);
  system(s);
  cmd = paste("dot -T png ", arquivo," -o ",saida);
  system(cmd);
}


file_in=c("enem1.dot","enem2.dot","enem3.dot","enem4.dot",
		  "enem5.dot","enem6.dot","enem7.dot","enem8.dot",
		   "idh1.dot","idh2.dot","idh3.dot","idh4.dot","idh5.dot",
		   "iris1.dot","iris2.dot","vinho1.dot");

file_out=c("enem1.png","enem2.png","enem3.png","enem4.png",
		  "enem5.png","enem6.png","enem7.png","enem8.png",
		   "idh1.png","idh2.png","idh3.png","idh4.png","idh5.png",
		   "iris1.png","iris2.png","vinho1.png");


executar <-function(){
	for (i in 1:16) {
		run(file_in[i],file_out[i]);
	}
}