package Tp1;

import java.util.ArrayList;
import java.util.HashMap;

public class Nodo implements Comparable<Nodo>
{
	
	private int valor;
	private double probabilidad; 
	private Nodo nodoIzq; 
	private Nodo nodoDer; 
	private int posicion; 

	/* Constructor con el parametro de valor, utilizado para inicializar el arbol de huffmann con 
	   los codigos de valor correspondientes. */
	public Nodo(int valor, double probabilidad, Nodo nodoIzq, Nodo nodoDer)  
	{
		this.valor = valor;
		this.probabilidad = probabilidad;
		this.nodoDer = nodoDer; 
		this.nodoIzq = nodoIzq; 
	}
	
	
	/* Constructor sin el parametro de valor, utilizado para agregar al arbol de huffman los nodos 
	   intermedios del algoritmo, los cuales solo tienen la suma de las probabilidades de sus hijos. */
	public Nodo(double probabilidad, Nodo nodoIzq, Nodo nodoDer) 
	{
		this.probabilidad = probabilidad; 
		this.nodoDer = nodoDer; 
		this.nodoIzq = nodoIzq;
	}
	
	// Obtenemos el valor del nodo
	public int getValor() 
	{
		return valor;
	}

	// Seteamos el valor del nodo
	public void setValor(int valor) 
	{
		this.valor = valor;
	}
	
	// Obtenemos la probabilidad asociada al nodo
	public double getProbabilidad()
	{
		return probabilidad;
	}
	
	// Seteamos la probabilidad asociada al nodo
	public void setProbabilidad(double probabilidad) 
	{
		this.probabilidad = probabilidad;
	}

	/* Genera los codigos de huffman asociados al arbol generado. Se va generando un string de manera
	   recursiva en donde se toma como convencion:
	   	->"0" si va por la rama izquierda
	   	->"1" si va por la rama derecha
	   cuando llega a una hoja del arbol, se llega a un valor y este mismo se agrega al HashMap  y su string (codigo) asociado. */
	public void generarCodigos (Nodo raiz, String s, HashMap<Integer , String> codigos) 
	{
		// Si es Hoja, encontramos un valor 
		if (raiz.nodoIzq == null && raiz.nodoDer == null)
		{
			codigos.put(raiz.getValor(), s); 
		}
		else 
		{
			// Sino, recorremos recursivamente por izquierda o por derecha. 
			generarCodigos(raiz.nodoIzq, s + "0", codigos);
			generarCodigos(raiz.nodoDer, s + "1", codigos);
		}
	}
	
	/* Metodo utilizado para la descompresion, lo que hace es recorrer el arbol ya generado 
	   segun la convencion tomada para la compresion. Lo hace tomando como entrada el mensaje
	   codificado,los bits de este se van recorriendo y cuando se encuentra una hoja 
	   en el arbol la retorna, dado que se encontro un valor del archivo codificado. */
	public double obtenerValor (ArrayList <Character> arcHuffman, Nodo nodo) 
	{
		Character bit = '2'; 
		// Mientras que no sea hoja y que la posicion no supere el rango del arreglo, seguimos
		while (!esHoja(nodo) && this.posicion < arcHuffman.size()) 
		{
			// Tomamos el bit del arreglo
			bit = arcHuffman.get(this.posicion); 
			// Si es 0, nos vamos para la rama izquierda
			if (bit == '0')
				nodo = nodo.nodoIzq; 
			// Sino, nos vamos para la rama derecha
			else if (bit == '1')
				nodo = nodo.nodoDer; 
			this.posicion++; 
		}
		
		// Cuando llego a una hoja, llego a un valor. Lo retornamos 
		return nodo.getValor(); 
	}
	
	// True si es una hoja del arbol, false de lo contrario. 
	public boolean esHoja (Nodo nodo) 
	{
		if (nodo.nodoIzq == null && nodo.nodoDer == null) 
			return true; 
		else 
			return false; 
	}
	
	/* Implementacion de la interfaz Comparable, se utiliza para insertar ordenado
	   de manera ascendente en la estructura utilizada para generar el arbol de huffman, un TreeSet. */
	@Override
	public int compareTo (Nodo otroNodo) 
	{
		if (this.probabilidad <= otroNodo.getProbabilidad())
			return -1; 
		
		return 1; 
	}	
}
