package Tp1;

import java.util.ArrayList;

class Canal {
	private senial entrada;
	private senial salida;
	
	public double[][] getMatrizConjunta() {
		double [][] matriz = new double [3][3];
		ArrayList<Integer> arrEntrada = entrada.getArray();
		ArrayList<Integer> arrSalida = salida.getArray();
		int valorEntrada = 0;
		int valorSalida = 0;
		int valAntEntrada = arrEntrada.get(0);
		int valAntSalida = arrSalida.get(0);
		for(int i=0; i<matriz.length; i++) {					//inicializamos la matriz
			for(int j=0; j<matriz[0].length; j++) {
				matriz[i][j] = (double) 0;
			}
		}
		for (int k=1; k < arrEntrada.size(); k++) {
			valorEntrada = arrEntrada.get(k);
			valorSalida = arrSalida.get(k);
			if (valorEntrada > valAntEntrada) 
				if(valorSalida > valAntSalida) 
					matriz[1][1]++;
				else if (valorSalida < valAntSalida) 
						matriz[2][1]++;
					else
						matriz[0][1]++;
			else if (valorEntrada < valAntEntrada)
				if(valorSalida > valAntSalida) 
					matriz[1][2]++;
				else if (valorSalida < valAntSalida) 
						matriz[2][2]++;
					else
						matriz[0][2]++;
			else
				if(valorSalida > valAntSalida) 
					matriz[1][0]++;
				else if (valorSalida < valAntSalida) 
						matriz[2][0]++;
					else
						matriz[0][0]++;
			valAntEntrada = valorEntrada;
			valAntSalida = valorSalida;
		}
		
		for(int m=0; m<matriz.length; m++) {					
			for(int l=0; l<matriz[0].length; l++) {
				matriz[m][l] /= (arrEntrada.size() - 1);
			}
		}
		return matriz;
	}
	
	public double [] getProbabilidadMarginalX() {
		double [][] mat = this.getMatrizConjunta();
		double [] arr = new double [3];
		double cont;
		
		for (int i=0; i<arr.length; i++) {
			cont = 0;
			for (int j=0; j<arr.length; j++) {
				cont += mat[j][i];
			}
			arr[i] = cont;
		}
		return arr;
	}

	public double [] getProbabilidadMarginalY() {
		double [][] mat = this.getMatrizConjunta();
		double [] arr = new double [3];
		double cont;
		
		for (int i=0; i<arr.length; i++) {
			cont = 0;
			for (int j=0; j<arr.length; j++) {
				cont += mat[i][j];
			}
			arr[i] = cont;
		}
		return arr;
	}
	
	public double [][] getMatrizCondicionalXdadoY() {
		double [][] mat = this.getMatrizConjunta();
		double [] arr = this.getProbabilidadMarginalY();
		
		for (int i=0; i<mat.length; i++) {
			for (int j=0; j<mat[0].length; j++) {
					mat[i][j] /= arr[i];
			}
		}
		return mat;
	}
	
	public double [][] getMatrizCondicionalYdadoX() {
		double [][] mat = this.getMatrizConjunta();
		double [] arr = this.getProbabilidadMarginalX();
		
		for (int i=0; i<mat.length; i++) {
			for (int j=0; j<mat[0].length; j++) {
					mat[j][i] /= arr[i];
			}
		}
		return mat;
	}
	
	public double getRuido() {
		double [] arr = this.getProbabilidadMarginalX();
		double [][] mat = this.getMatrizCondicionalYdadoX();
		double sumaY = 0;
		double sumaTotal = 0;
		
		for (int i=0; i<mat.length; i++) {
			for (int j=0; j<mat[0].length; j++) {
				sumaY -= (mat[j][i] * (Math.log10(mat[j][i]) / Math.log10(2)));
			}
			sumaTotal += (arr[i] * sumaY);
			sumaY = 0;
		}
		
		return sumaTotal;
	}
	
	public double getPerdida() {
		double [] arr = this.getProbabilidadMarginalY();
		double [][] mat = this.getMatrizCondicionalXdadoY();
		double sumaX = 0;
		double sumaTotal = 0;
		
		for (int i=0; i<mat.length; i++) {
			for (int j=0; j<mat[0].length; j++) {
				sumaX -= (mat[i][j] * (Math.log10(mat[i][j]) / Math.log10(2)));
			}
			sumaTotal += (arr[i] * sumaX);
			sumaX = 0;
		}
		
		return sumaTotal;
	}
	
	public Canal (senial entrada, senial salida) {
		this.entrada = entrada;
		this.salida = salida;
	}
}
