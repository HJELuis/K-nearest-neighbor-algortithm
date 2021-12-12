
package proyecto;

import java.util.Random;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.Evaluation;

import java.io.*;


public class Proyecto {

   public static void tam_documento(String documento,int[] num_insta, int[] num_atrib){
        
            
        try{
            FileReader fe = new FileReader(documento);
            BufferedReader br = new BufferedReader(fe);
            while(br.readLine()!= null){
                num_insta[0]++;// para conocer el número de instancias en el archivo
            }
            fe.close();
            }catch(IOException e){
                System.out.println("Error: " + e.toString());
            }
        
        int num_atributos[] = new int[num_insta[0]] ;	 
        for(int i = 0; i < num_insta[0]; i++)
            num_atributos[i] = 0;
            
	
        int num_caracteres[] = new int[num_insta[0]];
            for(int i = 0; i < num_insta[0]; i ++)
                num_caracteres[i] = 0;
            
        try{  
               
            for(int i = 0; i < num_insta[0]; i++)
                num_atributos[i] = 0;
        
            for(int i = 0; i < num_insta[0]; i ++)
                num_caracteres[i] = 0;
           
            FileReader fe2 = new FileReader(documento);
            BufferedReader br2  = new BufferedReader(fe2);
            BufferedReader br3  = new BufferedReader(fe2);
           
            int aux = 0;
            int a = 0;        
            aux = br2.read();
            char caracter;
            while(aux != -1){// para saber el número de atributos que hay en cada instancia
              caracter = (char)aux;
		num_caracteres[a]++;
		if(String.valueOf(caracter).matches(","))
			num_atributos[a]++;
		if(String.valueOf(caracter).matches("\n"))
			a++;
                aux = br2.read();
            }
            for(int i = 0; i < num_insta[0]; i++){//por no estar tomando en cuenta la clase
		num_atributos[i]++;
            }
            fe2.close();
           
        }catch(IOException e){
            System.out.println("Error: " + e.toString());
        }
        
        int  n_elementos = 0, aux2 = 0;
        boolean s = true;
        n_elementos = num_insta[0];
        while(s && (--n_elementos > 0)){//ordenar para saber la cantidad  mas grande de atributos en una instancia
		s = false;
		for(int i = 1; i <= n_elementos; i++){
                    if(num_atributos[i-1] > num_atributos[i]){
			aux2 = num_atributos[i-1];
			num_atributos[i-1] = num_atributos[i];
			num_atributos[i] = aux2;
			s = true;
                    }
		}
            }

            num_atrib[0] = num_atributos[num_insta[0] - 1];
            
    }
   public static void datos_documento(String documento,double d[][]){
       
        try{
            FileReader fe = new FileReader(documento);
            BufferedReader br = new BufferedReader(fe);
            String linea;String aux = "";
            int i = 0,a = 0;
            while((linea = br.readLine()) != null){//para llenar las matrices con los valores numericos de los archivos
                for (int j = 0; j < linea.length(); j++) {
                    if(!(String.valueOf(linea.charAt(j)).matches(","))){
                        aux += linea.charAt(j);
                    }else{
                        d[i][a++] = Double.parseDouble(aux);
                        aux = "";                       
                    }
                }
                d[i][a] = Double.parseDouble(aux);//se trata de la clase
                aux = "";
                i++; // se trata de una ueva instancia
                a = 0;
            }
            fe.close();
            }catch(IOException e){
                System.out.println("Error: " + e.toString());
            }
        
    }
   public static void KNN(int k_vecinos,double entrenamiento[][],double prueba[][],int instanciase, int instanciasp, int atributosp, int atributose){
       
        double[][] distancia = new double[instanciasp][instanciase];
       
        for(int ip = 0; ip < instanciasp; ip++){
		for(int ie = 0; ie < instanciase; ie++){
			distancia[ip][ie] = 0;
		}
	}
        
        double suma = 0;
        int num_atributos = atributosp - 1;// para evitar sumar la clase
 
        for(int ip = 0; ip < instanciasp; ip++){//se calcula la distancia de todos los elementos del conjunto de prueba a todos los elementos del conjunto de entrenamiento
		for(int ie = 0; ie < instanciase; ie++){
			for(int j = 0; j < num_atributos; j++){
				suma += Math.pow(entrenamiento[ie][j] - prueba[ip][j],2);
                        }
			distancia[ip][ie]= Math.sqrt(suma);
			suma = 0;
		}
	}
        
        double[][] aux = new double[instanciasp][instanciase];
        for(int ip = 0; ip < instanciasp; ip++){// las distancias se pasan a una matriz auxiliar
		for(int ie = 0; ie < instanciase; ie++){
				aux[ip][ie] = distancia[ip][ie];
		}
	}
        
        int n_elementosp = instanciasp, n_elementose = instanciase;boolean s = true;double aux2 = 0.0;
        
        for(int j = 0; j < n_elementosp; j++){ //la siguientes líneas son para ordenar las distancias de menor a mayor
	
		while(s && (--n_elementose > 0)){
			s = false;
			for(int i = 1; i <= n_elementose; i++){
				if(aux[j][i-1] > aux[j][i]){
					aux2 = aux[j][i-1];
					aux[j][i-1] = aux[j][i];
					aux[j][i] = aux2;
					s = true;
				}
			}
		}
		aux2 = 0;
		s = true;
		n_elementose = instanciase;
	}
        
        int[][] pos = new int[instanciasp][k_vecinos];boolean mismainstancia = false;
	for(int ip = 0; ip < instanciasp; ip++){
		for(int v = 0; v < k_vecinos; v++){
			pos[ip][v] = 0;
		}
	}
        for(int ip = 0; ip < instanciasp; ip++){ //las siguientes líneas son para encontrar mis k vecinos mas cercanos en la matriz de distancias que me pueda regresar el indice adecuado
		for(int v = 0; v < k_vecinos; v++){
			for(int ie = 0; ie < instanciase; ie++){
				if(aux[ip][v] == distancia[ip][ie]){
					for(int m = 0; m < k_vecinos; m++){
						if(pos[ip][m] == ie)
							mismainstancia = true;
					}
					if(!mismainstancia)
						pos[ip][v] = ie;
					mismainstancia = false;
				}	
			}
		}
	}
        
        double[] clas = new double[instanciase];
	for(int ie = 0; ie < instanciase; ie++){
		clas[ie] = 0;
	}
      
	clas[0] = entrenamiento[0][atributose-1];
	int c = 1,cuenta = 0;
	for(int ie = 0; ie < instanciase; ie++){// las siguientes líneas son para obtener las clases
		for(int cla = 0; cla < c; cla++){
			if(clas[cla] != entrenamiento[ie][atributose-1]){
				cuenta++;
				if(cuenta == c){
					clas[c++] = entrenamiento[ie][atributose-1];
					cuenta = 0;
				}
			}
		}
		cuenta = 0;
	}
        
        int[][] num_clase = new int[instanciasp][c];
	for(int ip = 0; ip < instanciasp; ip++){
		for(int cla = 0; cla < c; cla++){
			num_clase[ip][cla] = 0;
		}
	}
	for(int ip = 0; ip < instanciasp; ip++){//las siguientes líneas son para contar cuantas veces se repite una clase para determinar cual va a ser la clase de mi instancia a clasificar
		for(int v = 0; v < k_vecinos; v++){
			for(int cla = 0; cla < c; cla++){
				if(entrenamiento[pos[ip][v]][atributose-1] == clas[cla])
					num_clase[ip][cla]++;
			}
		}
	}
        
        int mayor = 0,indice = 0;
	double[] clase = new double[instanciasp]; // determinar para cada instancia del conjunto de prueba cual es la clase que tienen mas vecinos para asignarla como su clase
	for(int ip = 0; ip < instanciasp; ip++){
		for(int cla = 0; cla < c; cla++){
			if(mayor < num_clase[ip][cla]){
				mayor = num_clase[ip][cla];
				indice = cla;
			}
		}	
		clase[ip] = clas[indice];
		mayor = 0;
	}
        
        double num_aciertos = 0;
	for(int ip = 0; ip < instanciasp; ip++){//determinar que clase asignada es igual a la esperada de parte del conjunto de prueba 
		if(clase[ip] == prueba[ip][atributose-1])
			num_aciertos++;
	}
        
	double exactitud = 0;
	exactitud = ((num_aciertos/instanciasp)) * 100;
	      System.out.println("La exactitud es de: " + exactitud);
            
        int[][] matriz = new int[c][c];    
        for(int ip = 0; ip < instanciasp; ip ++){// para obtener la matriz de confusiobn
            matriz[(int)prueba[ip][19]][(int)clase[ip]]++;
        }      
        
        char lateral = 97;
        char encabezado = 97;
        for(int j = 0; j < c; j++){
                System.out.print(encabezado++ + "  " );
        }
        System.out.println("<-- clasificado como");
        for(int i = 0; i < c; i++){
            for(int j = 0; j < c; j++){
                if(matriz[i][j] > 9){
                    System.out.print(matriz[i][j] + " ");
                }else{
                    System.out.print(matriz[i][j] + "  ");
                }
            }
            System.out.print("|" + " " + lateral++ + " = " + i);
            System.out.println();
        }
        System.out.println();
   }
   public static void KNN(int k_fold,int k_vecinos,double entrenamiento[][],int instanciase,int atributose){
        double[] exactitud = new double[k_fold];
        double aux3 = 0;
        int exac = 0;
        int nueva_matriz = 0;
        double may = 0;
        int sel = 0;
        int numero = 0;
        int k_cross = instanciase/k_fold;//dividir el número de instancias en el conjunto de entrenamiento en k partes.
	
	
        double[][] conjunto_entrenamiento = new double[instanciase-k_cross][atributose];
	double[][] conjunto_prueba = new double[k_cross][atributose];
	int tam_e = 0,tam_p = 0;
	
	tam_e = instanciase - k_cross;
	tam_p = k_cross;
	
	int[][] indice = new int[k_fold][k_cross];
	int contador_indices = 0,aleatorio = 0,ce = 0;boolean esigual = false;
	for(int f = 0; f < k_fold; f++){ //inicializar la matriz indice con un valor distinto de 0
		for(int c = 0; c < k_cross; c++){
			indice[f][c] = instanciase;
		}
	}
        
        for(int f = 0; f < k_fold; f++){
            while(contador_indices < k_cross){
		aleatorio =(int)(Math.random() * (instanciase-1));
		esigual = false;
		for(int h = 0; h <= f; h++){
                    for(int i = 0; i < k_cross; i++){
			if(aleatorio == indice[h][i])
                            esigual = true;
                    }
		}
		if(!esigual){
                    indice[f][contador_indices] = aleatorio;
                    contador_indices++;
		}
            }
            contador_indices = 0;
	}
        
        
        
        
        double[] cl = new double[tam_e];
        for(int i = 0; i < tam_e; i++){
            cl[i] = 0;
        }
        cl[0] = entrenamiento[0][atributose-1];
        int cuenta1 = 0;numero = 1;
        for(int i = 0; i < tam_e; i++){// las siguientes líneas son para obtener las clases
            for(int j = 0; j < numero; j++){		 
                if(cl[j] != entrenamiento[i][atributose-1]){
                    cuenta1++;
                    if(cuenta1 == numero){
                        cl[numero++] = entrenamiento[i][atributose-1];
                        cuenta1 = 0;
                    }
                }	 
            }
	cuenta1 = 0;
        }
            
        int[][][] matriz = new int[k_fold][numero][numero]; 
        
        
        
        for(int fp = 0; fp < k_fold; fp++){//aqui comienza la division de conjuntos
            
            for(int c = 0; c < k_cross; c++){
                for(int a = 0; a < atributose; a++){
                    conjunto_prueba[c][a] = entrenamiento[indice[fp][c]][a];
                }
            }
            
            for(int f = 0; f < k_fold; f++){
		if(f == fp)
                    continue;
		for(int j = 0; j < k_cross; j++){
                    for(int a = 0; a < atributose; a++){
			
			conjunto_entrenamiento[ce][a] = entrenamiento[indice[f][j]][a];
                    }
                    ce++;
		}
            }
            
            int[] indice_faltante = new int[tam_e-ce]; //para obtener los indices faltantes puesto que al dividir el conjunto de entrenamiento no siempre se obtienen valores pares, por lo que hay que agregar las instancias faltantes.
            for(int i = 0; i < tam_e-ce; i++){
		indice_faltante[i] = 0;
            }
            
            contador_indices = 1;
            while(contador_indices < tam_e-ce){
		aleatorio = (int)(Math.random() * (instanciase - 1));
		esigual = false;
		for(int h = 0; h < k_fold; h++){
                    for(int i = 0; i < k_cross; i++){
			if(aleatorio == indice[h][i])
                            esigual = true;
                    }
                }	
		for(int f = 0; f < tam_e-ce; f++){
                    if(indice_faltante[f] == aleatorio)
			esigual = true;
		}	
		if(!esigual){	
                    indice_faltante[contador_indices++] = aleatorio;
                    
		}
            }
            
            
          
            int auxi = tam_e - ce; 
            for(int i = 0; i < auxi; i++){
		for(int a = 0; a < atributose; a++){
                    conjunto_entrenamiento[ce][a] = entrenamiento[indice_faltante[i]][a];
		}
		ce++;
            }
            ce = 0 ; 
            
            int t_atributos = atributose - 1; //para que no tome en cuenta la clase 
            double[][] distancia = new double [tam_p][tam_e];
            double[][] aux = new double[tam_p][tam_e];
            double suma = 0;
            for(int ip = 0; ip < tam_p; ip++){
		for(int ie = 0; ie < tam_e; ie++){
                    for(int a = 0; a < t_atributos; a++){
			suma += Math.pow(conjunto_entrenamiento[ie][a] - conjunto_prueba[ip][a],2); 
                    }
                    distancia[ip][ie]=Math.sqrt(suma);
                    suma = 0;
		}
            }
            
            for(int ip = 0; ip < tam_p; ip++){
			 for(int ie = 0; ie < tam_e; ie++){
				 aux[ip][ie] = distancia[ip][ie];
			 }
		 }
            
            int n_elementosp = tam_p, n_elementose = tam_e; boolean s = true;
            double aux2 = 0.0;
            for(int j = 0; j < n_elementosp; j++){ //la siguientes líneas son para ordenar las distancias de menor a mayor
		while(s && (--n_elementose > 0)){
                    s = false;
                    for(int i = 1; i <= n_elementose; i++){
			if(aux[j][i-1] > aux[j][i]){
                            aux2 = aux[j][i-1];
                            aux[j][i-1] = aux[j][i];
                            aux[j][i] = aux2;
                            s = true;
			}
                    }
		}
		aux2 = 0;
		s = true;
		n_elementose = tam_e;
            }
                
            int[][] pos = new int[tam_p][k_vecinos];
            for(int ip = 0; ip < tam_p; ip++){
		for(int v = 0; v < k_vecinos; v++){
                    pos[ip][v] = 0;
		}
            }
            boolean mismainstancia = false;
            
            for(int ip = 0; ip < tam_p; ip++){ //las siguientes líneas son para encontrar mis k valores mas pequeños en la matriz de distancias que me pueda regresar el indice adecuado
		for(int v = 0; v < k_vecinos; v++){
                    for(int ie = 0; ie < tam_e; ie++){
			if(aux[ip][v] == distancia[ip][ie]){
                            for(int vv = 0; vv < k_vecinos; vv++){
                                if(pos[ip][vv] == ie)
                                    mismainstancia = true;
                            }
                            if(!mismainstancia)
                                pos[ip][v] = ie;
                            mismainstancia = false;
                        }
                    }
                }
            }
		 
            double[] clas = new double[tam_e];
            for(int i = 0; i < tam_e; i++){
		clas[i] = 0;
            }
            clas[0] = conjunto_entrenamiento[0][atributose-1];
            int c = 1,cuenta = 0;
            for(int i = 0; i < tam_e; i++){// las siguientes líneas son para obtener las clases
		for(int j = 0; j < c; j++){		 
                    if(clas[j] != conjunto_entrenamiento[i][atributose-1]){
			cuenta++;
                        if(cuenta == c){
                            clas[c++] = conjunto_entrenamiento[i][atributose-1];
                            cuenta = 0;
                        }
                    }	 
		}
		cuenta = 0;
            }
            
           
            int[][] num_clase = new int[tam_p][c];
            for(int ip = 0; ip < tam_p; ip++){
		for(int j = 0; j < c; j++){
                    num_clase[ip][j] = 0;
		}
            }
            for(int j = 0; j < tam_p; j ++){//las siguientes líneas son para contar cunatas veces se repite una clase para determinar cual va a ser la clase de mi instancia a clasificar
		for(int i  = 0; i < k_vecinos; i++){	 
                    for(int k = 0; k < c; k++){
			if(conjunto_entrenamiento[pos[j][i]][atributose-1] == clas[k])
                            num_clase[j][k]++;
                    }
				 
                }
            }
            
            int mayor = 0,ind = 0;
            double[] clase = new double[tam_p]; // determinar para cada instancia del conjunto de prueba cual es la clase que tienen mas vecinos para asignarla como su clase
            for(int j = 0; j < tam_p; j++){
                for(int i = 0; i < c; i++){
                    if(mayor < num_clase[j][i]){
			mayor = num_clase[j][i];
			ind = i;
                    }
		}	
		clase[j] = clas[ind];
		mayor = 0;
            }
            
            double num_aciertos = 0;
            for(int j = 0; j < tam_p; j++){//determinar que clase asignada es igual a la esperada de parte del conjunto de prueba 
                if(clase[j] == conjunto_prueba[j][atributose-1])
                    num_aciertos++;
            }
            
            
            exactitud[exac++] = ((num_aciertos/tam_p)) * 100;
            aux3 += exactitud[exac-1]; 
            
            
            if(may < exactitud[exac-1] ){
                may = exactitud[exac-1];
                sel = fp;
            }
            
            
            for(int ip = 0; ip < tam_p; ip++ ){
                matriz[nueva_matriz][(int)conjunto_prueba[ip][atributose-1]][(int)clase[ip]]++;
            }
            
            nueva_matriz++;
            
            if(fp == k_fold - 1){
                aux3 /= k_fold;
                System.out.println("La exactitud es: " + aux3);
                char lateral = 97;
                char encabezado = 97;
                for(int j = 0; j < c; j++){
                    System.out.print(encabezado++ + "  " );
                }
                System.out.println("<-- clasificado como");
                for(int i = 0; i < c; i++){
                    for(int j = 0; j < c; j++){
                        if(matriz[sel][i][j] > 9){
                            System.out.print(matriz[sel][i][j] + " ");
                        }else{
                            System.out.print(matriz[sel][i][j] + "  ");
                        }
                    }
                    System.out.print("|" + " " + lateral++ + " = " + i);
                    System.out.println();
                }
            }
            
        }
        System.out.println();
   }
   public static void main(String[] args) throws Exception{
        
        
        int[] num_instae = {0};int[] num_atribe = {0}; 
        int[] num_instap = {0};int[] num_atribp = {0};
        
        tam_documento("Datos-S-Entrena.txt",num_instae,num_atribe);
        tam_documento("S-Prueba.txt",num_instap,num_atribp);
        
        double[][] entrenamiento = new double[num_instae[0]][num_atribe[0]];
        double[][] prueba = new double[num_instap[0]][num_atribp[0]];
        
        datos_documento("Datos-S-Entrena.txt",entrenamiento);
        datos_documento("S-Prueba.txt",prueba);
        
        int k_vecinos = 5;
	String capas = "6,3";
        double tasa_aprendizaje = 0.9;
        double momentum = 0.5;
        int epocas = 1000;
        int k_fold = 10;
       
        if(k_fold == 0){
            System.out.println("============Algoritmo KNN============");
            KNN(k_vecinos,entrenamiento,prueba,num_instae[0],num_instap[0],num_atribp[0],num_atribe[0]);
            System.out.println("============Algoritmo RNA============");
            DataSource e_fuente = new DataSource("C:\\Users\\luise\\Documents\\entrenamiento.arff");
            Instances train = e_fuente.getDataSet();
                
            train.setClassIndex(train.numAttributes()-1);
                
            int num_clases = train.numClasses();
            for(int i = 0; i < num_clases; i++){
                String classValue = train.classAttribute().value(i);
            }
            MultilayerPerceptron mp = new MultilayerPerceptron();
            mp.setHiddenLayers(capas);
            mp.setLearningRate(tasa_aprendizaje);
            mp.setMomentum(momentum);
            mp.setTrainingTime(epocas);
                
            mp.buildClassifier(train);
            Evaluation eval = new Evaluation(train );
                
            DataSource p_fuente = new DataSource("C:\\Users\\luise\\Documents\\prueba.arff");
            Instances test = p_fuente.getDataSet();
                
            test.setClassIndex(test.numAttributes()-1);
            eval.evaluateModel(mp, test);
                   
            double bien_clas = 0;
            for(int i = 0; i < test.numInstances(); i++){
                double c_real = test.instance(i).classValue();
                String real = test.classAttribute().value((int)c_real);
                Instance newInst = test.instance(i);
                double predMP = mp.classifyInstance(newInst);
                String clasificada = test.classAttribute().value((int) predMP);
                if(real == clasificada)
                    bien_clas++;
            }
                
            double exactitud = 0;
            exactitud = (bien_clas / test.numInstances()) * 100;
            System.out.println("La exactitud es : " + exactitud);
            System.out.println(eval.toMatrixString());        
                   
        }
        else{
            System.out.println("============Algoritmo KNN============");
            KNN(k_fold,k_vecinos,entrenamiento,num_instae[0],num_atribe[0]);
            System.out.println("============Algoritmo RNA============");
            DataSource f_entrena = new DataSource("C:\\Users\\luise\\Documents\\entrenamiento.arff");
            Instances train = f_entrena.getDataSet();
                   
            train.setClassIndex(train.numAttributes()-1);
            MultilayerPerceptron mp = new MultilayerPerceptron();
            mp.setHiddenLayers(capas);
            mp.setLearningRate(tasa_aprendizaje);
            mp.setMomentum(momentum);
            mp.setTrainingTime(epocas);
            int seed = 0;
                    
            Random rand = new Random(seed);
                    
            Instances datosrand = new Instances(train);
            datosrand.randomize(rand);
            double exactitud = 0,mayor = 0;
            int indice = 0;
            String aux[] = new String[k_fold];
            for(int i = 0; i < k_fold; i++){
                Evaluation eval = new Evaluation(datosrand);
                        
                Instances entrena = datosrand.trainCV(k_fold, i);
                Instances test = datosrand.testCV(k_fold,i);
                        
                mp.buildClassifier(entrena);
                eval.evaluateModel(mp, test);
                        
                aux[i] = eval.toMatrixString();
                        
                if(mayor < exactitud){
                    mayor = exactitud;
                            indice = i;
                }
                exactitud += eval.pctCorrect();
            }
                    
            exactitud /= k_fold;
            System.out.println("La exactiud es igual a:" + exactitud);
            System.out.println(aux[indice]);
                   
        }
    }
}
