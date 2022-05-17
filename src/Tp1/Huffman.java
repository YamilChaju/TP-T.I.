package Tp1;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class Huffman 
{
	private HashMap <Integer, Double> ocurrencias; 
	private HashMap<Integer, String> codigos;
	private Nodo raizNodo;
	
	// Constructor
	public Huffman () 
	{
		this.ocurrencias = new HashMap <Integer, Double> (); 
		this.codigos = new HashMap<Integer, String>();
		this.raizNodo = null; 
    }  
    // Obtenemos la distribucion del archivo
	public TreeSet<Nodo> getDistribuciones(senial s){
        TreeSet<Nodo> distribucion = new TreeSet<Nodo>();
		HashMap<Integer,Double> map = new HashMap<Integer,Double>();
		ArrayList<Integer> array = s.getArray();
		for (int i=0; i<array.size(); i++) {
			if (map.containsKey(array.get(i))) {
				map.replace(array.get(i), map.get(array.get(i)) + (double)  1);
			}else {
				map.put(array.get(i), (double) 1);
			}		
	    }
        int size = s.getSizeArray();
		Set<Integer> keys = map.keySet();
        // Recorremos el vector de ocurrencias para generar las probabilidades de la distribucion
        for(Integer key:keys){
            distribucion.add(new Nodo(key, (double) map.get(key)/size, null, null));
        }
        this.ocurrencias = map;
        return distribucion;
	
    }

	
	// Generamos el arbol de huffman
	public void generarArbol (TreeSet<Nodo> distribucion) 
	{	
		String s = ""; 
		/* Mientras que haya mas de un nodo en la distribucion, seguimos. El algoritmo termina
		   cuando se haya llegado a conectar todos los nodos. */
		while (distribucion.size() > 1) 
		{
			/* Como esta ordenada la estructura de menor a mayor, agarramos los dos primeros nodos, siendo estos
			   los dos menores actuales */
			Nodo menorNodo1 = distribucion.pollFirst(); 
			Nodo menorNodo2 = distribucion.pollFirst(); 
			// Generamos el padre, que es la suma de las probabilidades de los nodos hijos
			Nodo padre = new Nodo(menorNodo1.getProbabilidad()+menorNodo2.getProbabilidad(), menorNodo1, menorNodo2);
			// Hacemos que el padre generado sea la raiz
			this.raizNodo = padre; 
			// Y agregamos el padre a la distribucion
			distribucion.add(padre); 
		}
		// Y por ultimo se generan los codigos huffman, con el arbol ya construido
		this.raizNodo.generarCodigos(this.raizNodo, s, this.codigos); 
	}

	// Obtenemos una copia de las ocurrencias
	public HashMap <Integer, Double> getOcurrencias () 
	{
		HashMap <Integer, Double> aux=new HashMap <Integer, Double>(); 
		for (Map.Entry<Integer, Double> iterador: this.ocurrencias.entrySet()) 
		{
			aux.put(iterador.getKey(), iterador.getValue()); 
		}
		
		return aux; 
	}
	
	// Obtenemos una copia de los codigos
	public HashMap <Integer, String> getCodigos() 
	{
		HashMap <Integer, String> aux=new HashMap <Integer, String>(); 
	    for (Map.Entry<Integer, String> iterador: this.codigos.entrySet()) 
	    {
	    	aux.put (iterador.getKey(), iterador.getValue()); 
	    }
	    
	    return aux; 
	}
  
	// Codificamos el archivo, es decir generamos el binario a partir de los codigos asociados a los valores
  	public ArrayList<Character> codificarArchivo(senial s) 
  	{
  		ArrayList<Character> arcComp = new ArrayList<Character>(); 
  		ArrayList<Integer> array = s.getArray();
		for (int i=0; i<array.size(); i++) 
  			{
  				String c = new String();
  				// Obtenemos el codigo del color almacenado en "codigos", en un string
  				c = this.codigos.get(array.get(i));
  				// Desglosamos el string, separamos bit a bit y vamos agregandolo a nuestro arreglo de caracteres
  				for (int j = 0; j < c.length(); j++) 
  				{
  					arcComp.add(c.charAt(j)); 
  				}
  			}
  	
  		
		return arcComp; 
  	}

  	public double getRendimiento(senial s){

        double L = 0;
        double H = 0;

        TreeSet<Nodo> distribuciones = this.getDistribuciones(s);
        while(distribuciones.size() > 0){
            // calculamos la entropia de las probabilidades y sumamos las longitudes multiplicadas sus respectivas probabilidades.
            Nodo actual = distribuciones.pollFirst();
            H += (Math.log(actual.getProbabilidad())/Math.log(2.0))*actual.getProbabilidad();
            L += actual.getProbabilidad()*this.codigos.get(actual.getValor()).length();
        }
        H = H*-1;
        return H/L;
    }
  	
  	// Descomprimimos el archivo comprimido generado
  	public void descomprimir (String rutaArchivoComprimido, String rutaArchivoDescomprimido) 
  	{
  		Decodificador decodificador= new Decodificador(rutaArchivoComprimido);
  		int cantidad = decodificador.getCantidad(); 
  	 
  		HashMap<Integer, Integer> ocurrenciaValores = new HashMap<Integer, Integer>();
  		// Obtenemos la ocurrencia de los valores del archivo comprimido
  		ocurrenciaValores = decodificador.getOcurrenciaDeValores();
  		ArrayList<Character> arcHuffman = new ArrayList<Character>(); 
  		// Obtenemos la lista de caracteres generados por la compresion
  		arcHuffman = decodificador.getArcHuffman(); 
  		int pasadas = cantidad; 
  	
  		TreeSet<Nodo> distribucion = new TreeSet<Nodo>(); 
  		// Generamos el estado inicial del algoritmo de huffman, agregando a la distribucion los valores con sus probabilidades
  		for (Map.Entry<Integer, Integer> iterador : ocurrenciaValores.entrySet()) 
  		{
  			distribucion.add(new Nodo (iterador.getKey(), (double) iterador.getValue()/pasadas, null, null)); 
  		}
  		
  		// Generamos el arbol de huffman
  		generarArbol(distribucion);
  		Integer[] valores = new Integer[cantidad];
  		// Recorremos la imagen pixel a pixel
  		for(int i = 0; i < valores.length; i++)
  		{
  			valores[i] = (int) raizNodo.obtenerValor(arcHuffman, raizNodo);
  		}
  		try 
  		{
  		// Y creamos el archivo
  		FileWriter fw = new FileWriter(rutaArchivoDescomprimido);
			for(int i = 0; i < valores.length; i++)
  			{
				fw.write(valores[i].toString()+'\n');
  			}
			fw.close();
  		} 
  		catch (IOException e) 
  		{
  			e.printStackTrace();
  		}
  	}

}