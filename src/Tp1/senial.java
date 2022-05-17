package Tp1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class senial {
	
	private ArrayList<Integer> values = new ArrayList<Integer>();
	private String nombre;
	
	public ArrayList<Integer> getArray() {
		return this.values;
	}
	public int getSizeArray() {
		return this.values.size();
	}
	public String getName() {
		return this.nombre;
	}
	
	public double [][] getDistributionsMatrix() {	//Matriz de distribuciones
		double [][] matrix = new double[3][3];
		int previousElement = values.get(0);
		int element;
		char previousState = 'E';
		int Econt , Ucont , Dcont;
		Econt = Ucont = Dcont = 0;
		for(int i=0; i<3; i++) {					//inicializamos la matriz
			for(int j=0; j<3; j++) {
				matrix[i][j] = (double) 0;
			}
		}
		for (int k=1; k<values.size();k++){	
			element = values.get(k);
			if (previousState == 'E') {				//si el estado anterior es Equal
				Econt++;
				if (element > previousElement) {
					matrix[1][0]++;
					previousState = 'U';
				}else if(element < previousElement) {
						matrix[2][0]++;
						previousState = 'D';
					}else {
						matrix[0][0]++;
					}
				
			}else if (previousState == 'U') {			//si el estado anterior es Up
					Ucont++;
					if (element > previousElement) {
						matrix[1][1]++;
					}else if(element < previousElement) {
							matrix[2][1]++;
							previousState = 'D';
						}else {
							matrix[0][1]++;
							previousState = 'E';
						}
					
					
				}else {									//si el estado anterior es Down
					Dcont++;
					if (element > previousElement) { 
						matrix[1][2]++;
						previousState = 'U';
					}else if(element < previousElement) {
							matrix[2][2]++;
						}else {
							matrix[0][2]++;
							previousState = 'E';
						}
				}	
			previousElement = element;
		}
		for(int l=0; l<matrix.length; l++) {			//Se divide cada valor por la cantidad total de valores
			for(int m=0; m<matrix[0].length; m++) {		//para obtener la probabilidad
				if(m==0) 
					matrix[l][m] /= Econt;
				else if (m==1)
					matrix[l][m] /= Ucont;
				else 
					matrix[l][m] /= Dcont;		
			}
		}
		return matrix;
	}
	
	public float getValuesAverage(int t) {
		int sum = 0;
		if (t < 0) {									//Si t es negativo quiere decir que se pide el promedio
			for (int i=0; i<(values.size()+t); i++){	//desde el inicio del arreglo hasta el fin - t
				sum += values.get(i);
			}
		}else {											//Si t es positivo (o cero) se pide el promedio
			for (int i=0+t; i<values.size(); i++){		//desde el inicio del arreglo + t hasta el fin
				sum += values.get(i);
			}
		}
		return (float) sum/(values.size() - Math.abs(t));
	}
	
	public float getAutocorrelation(int t) {
		float sumDif_XY = 0, sumDifSquare_X = 0, sumDifSquare_Y = 0;
		float X_average = this.getValuesAverage(-t);				//X senial original
		float Y_average = this.getValuesAverage(t);					//Y misma senial pero desplazada t veces
        for (int i = 0; i < (values.size()-t); i++)
        {   
            sumDifSquare_X += (float) Math.pow((values.get(i)-X_average),2);		// sumatoria: (x - <x>)^2
            sumDifSquare_Y += (float) Math.pow((values.get(i+t)-Y_average),2);		// sumatoria: (y - <y>)^2
            
            sumDif_XY += ((values.get(i)-X_average)*(values.get(i+t)-Y_average));		// sumatoria: (x - <x>)*(y - <y>)
        }
      
        float corr = (float)(sumDif_XY)/ (float)(Math.sqrt(sumDifSquare_X * sumDifSquare_Y));
      
        return corr;
	}
	
	public float getCrossCorrelation(int t, senial senial) {
		float sumDif_XY = 0, sumDifSquare_X = 0, sumDifSquare_Y = 0;
		float X_average = this.getValuesAverage(-t);				//X: senial actual
		float Y_average = senial.getValuesAverage(t);				//Y: senial enviada por parametro
        for (int i = 0; i < (this.values.size()-t); i++)
        {   
            sumDifSquare_X += (float) Math.pow((this.values.get(i)-X_average),2);		// sumatoria: (x - <x>)^2
            sumDifSquare_Y += (float) Math.pow((senial.values.get(i+t)-Y_average),2);	// sumatoria: (y - <y>)^2
            
            sumDif_XY += ((this.values.get(i)-X_average)*(senial.values.get(i+t)-Y_average));// sumatoria: (x - <x>)*(y - <y>)
        }
      
        
        float corr = (float)(sumDif_XY)/ (float)(Math.sqrt(sumDifSquare_X * sumDifSquare_Y));
      
        return corr;
	}
	
	
	public senial(File file) throws FileNotFoundException {		//pasamos los valores del archivo
		Scanner scanner = new Scanner(file);					//al arreglo values
		while (scanner.hasNext()) {
			this.values.add(scanner.nextInt());
		}
		scanner.close();
		this.nombre = file.getName();
	}
}
	


