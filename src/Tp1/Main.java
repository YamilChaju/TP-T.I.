package Tp1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

class Main {
	
	
	public static String ejercicio1A(senial s){
		double[][] matDist = new double[3][3];
		matDist = s.getDistributionsMatrix();
		String resultado = ("Matriz de distribuciones:"+ '\n');
		for (int i = 0; i < matDist.length; i++) {
			for (int j = 0; j < matDist[0].length; j++) {
				resultado += (String.format("%.4f",matDist[i][j])+' ');
			}
			resultado += '\n';
		}
		return resultado;
	}
	
	public static String ejercicio1B(senial s){
		float largestAutocorrelation = -1;
		int Tlargest = 0;
		float smallestAutocorrelation = 1;
		int Tsmallest = 0;
		float aux;
		for (int i=1; i<=50; i++) {
			aux = s.getAutocorrelation(i);
			if (largestAutocorrelation < aux) {
				largestAutocorrelation = aux;
				Tlargest = i;
			}else if (smallestAutocorrelation > aux) {
				smallestAutocorrelation = aux;
				Tsmallest = i;
			}
		}
		String resultado = ("La autocorrelacion mayor es: "+largestAutocorrelation + '\n');
		resultado += (" con Tau igual a: " + Tlargest + '\n' + '\n');
		resultado += ("La autocorrelacion menor es: " + smallestAutocorrelation + '\n');
		resultado += (" con Tau igual a: " + Tsmallest+ '\n');

		return resultado;
	}
	
	public static String ejercicio1C(senial sBTC, senial sETH){
		float corr = 0;
		int t=0;
		String resultado= "";
		while (t<=200) {
			corr = sBTC.getCrossCorrelation(t, sETH);			
			resultado += ("Correlacion cruzada con t = " + t + " --> " + corr + '\n');
			t=t+50;
		}
		return resultado;
	}
		
	
	public static String ejercicio2A(senial s){
		Codificador c = new Codificador();
		String resultado = "";
		HashMap <Integer,Double> hash = c.getDistribuciones(s);
		hash.forEach((k,v) -> resultado.concat("key: "+k+" value:"+v + '\n'));
		return resultado;
	}

	public static String ejercicio2B(senial entrada){
		Huffman huffmanCompresor=new Huffman();
		huffmanCompresor.getDistribuciones(entrada);
		huffmanCompresor.generarArbol(huffmanCompresor.getDistribuciones(entrada));
		String NombreArchivoComp = "archivos\\Huffman"+ entrada.getName();
		String NombreArchivoDescomp = "archivos\\DescompHuffman"+ entrada.getName();
		Codificador cod = new Codificador(entrada.getSizeArray(), huffmanCompresor.getOcurrencias(), huffmanCompresor.codificarArchivo(entrada), NombreArchivoComp);
		File comp = new File(NombreArchivoComp);
		huffmanCompresor.descomprimir(NombreArchivoComp,NombreArchivoDescomp);
		File descomp = new File(NombreArchivoDescomp);
		String resultado = ("Peso " + entrada.getName() + " Huffman comprimido: " + comp.length() +'\n' +"Peso " + entrada.getName() + " Huffman descomprimido: " + descomp.length() +'\n');
		resultado += ("El rendimiento de Huffman es: " + huffmanCompresor.getRendimiento(entrada) + '\n');
		return resultado;
	}
	
	public static String ejercicio2C(senial s) {
		Codificador codificador = new Codificador();
		String comprimido = codificador.RLC(s);
		String resultado = "";
		FileWriter fichero = null;
        //PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("archivos\\RLC-"+s.getName());
            //pw = new PrintWriter(fichero);

            fichero.write(comprimido + '\n');
           

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           	  File comp = new File("archivos\\RLC-"+s.getName());
           	  resultado += ("Peso " + s.getName() + " RLC comprimido: " + comp.length() +'\n');
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
        
        DecodificadorRLC d = new DecodificadorRLC();
        try
        {
            fichero = new FileWriter("archivos\\RLC-Desc-"+s.getName());

            d.decodificar("archivos\\RLC-"+s.getName(), fichero);
       
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           	  File descomp = new File("archivos\\RLC-Desc-"+s.getName());
           	  resultado += ("Peso " + s.getName() + " RLC descomprimido: " + descomp.length() +'\n');
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
		return resultado;
	}
        
        
	
	
	
	public static String ejercicio3A(senial entrada, senial salida) {
		Canal c = new Canal(entrada, salida);
		double [][] matriz = c.getMatrizConjunta();
		String resultado = ("Matriz conjunta del canal: " + '\n');
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				resultado += (String.format("%.4f",matriz[i][j]) + ' ');
			}
			resultado += '\n';
		}
		return resultado;
		
	}
	
	public static String ejercicio3B(senial entrada, senial salida) {
		Canal c = new Canal(entrada, salida);
		String resultado = ("El ruido del canal es: "+c.getRuido() + '\n' + "La perdida del canal es: "+c.getPerdida() + '\n');
		return resultado;
	}
	
	
	
	
	public static void main(String[] args) throws IOException {
		File BTC = new File("BTC");
		senial sBTC = new senial(BTC);
		File ETH = new File("ETH");
		senial sETH = new senial(ETH);
		FileWriter fichero = new FileWriter("archivos\\Resultados");
		PrintWriter pw = new PrintWriter(fichero);

	//EJERCICIO 1
		pw.write(ejercicio1A(sBTC) + '\n' + ejercicio1A(sETH) + '\n' + ejercicio1B(sBTC) + '\n' + ejercicio1B(sETH) + '\n' + ejercicio1C(sBTC, sETH) + '\n' + ejercicio1C(sETH, sBTC) + '\n');
	//EJERCICIO 2
		pw.write(ejercicio2A(sBTC) + '\n'  + ejercicio2A(sETH) + '\n' + ejercicio2B(sBTC) + '\n'  + ejercicio2B(sETH) + '\n' + ejercicio2C(sBTC) + '\n'  + ejercicio2C(sETH) + '\n');
	//EJERCICIO 3
		pw.write(ejercicio3A(sBTC, sETH) + '\n'  + ejercicio3B(sBTC, sETH) + '\n');
		
		pw.close();
	}
	
	public Main() {
		
	}

}