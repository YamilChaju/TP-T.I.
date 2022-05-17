package Tp1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Clase que contiene lo necesario para descomprimir un archivo que esta comprimido
public class Decodificador
{
	// Contiene la cantidad de elementos del archivo 
	private int cantidad;

	// Contiene las ocurrencias de cada valor
	private HashMap<Integer, Integer> ocurrenciaValores;
	// Contiene el archivo codificado en huffman
	private ArrayList<Character> arcHuffman;
	// Guarda la posicion en la que se va leyendo el archivo comprimido
	int posicion;
	
	// Descomprime el archivo guardando los datos en los atributos de la clase para que luego puedan ser accedidos
	public Decodificador(String ruta)
	{
		posicion = 0;
		// Descomprimo el archivo comprimido en una secuencia de chars
		char[] secuencia = ByteEncodingHelper.DecodeSequence(ruta);

    
		// Encuentro el tamaño del archivo
		cantidad = encontrarSemiSecuencias(secuencia);
		

		// Encuentro los valores con sus ocurrencias
		ocurrenciaValores = new HashMap<Integer, Integer>();
		// Hasta que llegue al final del encabezado
		while(!finalEncabezado(secuencia))
		{
			int valor = encontrarSemiSecuencias(secuencia);
			int ocurrencia = encontrarSemiSecuencias(secuencia);
			ocurrenciaValores.put(valor, ocurrencia);
		}
		// Cuando llego al final del encabezado comienza el archivo en huffman
		
		// Cargo el archivo en codigo huffman en la lista archivo huhhman
		arcHuffman = new ArrayList<Character>();
		for(int i = 0; i < secuencia.length-posicion; i++)
		{
			arcHuffman.add(secuencia[i + posicion]);
		}
	}
 
	// Obtener la cantidad de elementos del archivo
	public int getCantidad()
	{
	  return cantidad;
  	}
	
	// Obtener un hashmap de las ocurrencias de cada valor
	public HashMap<Integer, Integer> getOcurrenciaDeValores()
	{
		
		HashMap <Integer, Integer> aux=new HashMap <Integer, Integer>(); 
		
	    for (Map.Entry<Integer, Integer> iterador: this.ocurrenciaValores.entrySet()) 
	    {
	   		aux.put (iterador.getKey(), iterador.getValue()); 
	   	}
	    
    	return aux; 
	}
	
	// Obtener una lista donde esta codificado el archivo
	public ArrayList<Character> getArcHuffman()
	{
		
		ArrayList<Character> aux=new ArrayList<Character>();
		
		for (int i = 0; i < arcHuffman.size(); i++) 
		{
			aux.add(this.arcHuffman.get(i)); 
		}
		
		return aux; 
	}
	
	// Metodo en el cual dado un arreglo que conformaba el archivo comprimido se obtiene una semisecuencia del encabezado de dicho arreglo
	private int encontrarSemiSecuencias(char[] secuencia)
	{
		/* El arreglo secuencia contiene un encabezado donde se guarda la cantidad de elementos y distribucion de valores del archivo
		   Cada uno de los valores est� codificado en bits, y para separarlos se usa un flag
		   Dicho flag de separaci�n es el siguiente 01111110
		   Adem�s si en el valor se encontraban cinco 1s seguidos, entonces se le agregaba un 0, para que nunca se lleguen a seis 1's seguidos,
		   de modo que no se lo pueda confundir con un flag
		   El final del encabezado se delimita con dos flags seguidos */
		
		// Aqui se guarda la secuencia en bits para pasarla a entero
		ArrayList<Character> semisecuencia = new ArrayList<Character>(); 
		
		boolean corte = false;
		// Se cuentan las secuencias de 1's para encontrar flags
		int contador = 0;

		// Voy a iterar por la secuencia hasta que encuentre el flag
		while(!corte)
		{
			
			// Si contador es cinco(hubo cinco 1's seguidos)
			if(contador == 5)
			{
				
				// Si este es un 1 corto, quiere decir que hubo seis 1's seguidos
				if(secuencia[posicion] == '1')
				{
					contador = 0;
					
					// Corte se vuelve verdadero porque hay un flag
					corte = true; 
					
					// Siempre voy a cortar con 6 bits de mas, 011111, que los fui agregando al resultado
					
					// Sumo dos en posicion para que luego comience justo despues del flag
					posicion = posicion + 2; 
				}
				
				// Si es 0 entonces no tengo que agregar nada ya que es un bit de transparencia
				else
				{
					// Pongo el contador en cero
					contador = 0;
					// No agrego el valor porque es un cero ya que los cinco 1's son parte del resultado
					posicion++;
				}
			}
			// Si contador no es cinco 
			else
			{ 
				// Agrego a mi semisecuencia el valor que esta en la secuencia
				semisecuencia.add(secuencia[posicion]);
				
				// Si el dato que agregue es 1 entonces aumento el contador
				if(secuencia[posicion] == '1')
				{
					contador++;
				}
				
				// Si el dato es 0 entonces reinicio el contador
				else
				{
					contador = 0;
				}
				posicion++;
			}
		}
		// Con la sucencia obtenida en bits obtengo el entero que representa
		return obtenerEntero(semisecuencia);
	}

	// Obtengo el entero a partir del numero a nivel bit
	private int obtenerEntero(ArrayList<Character> semisecuencia)
	{
		
		int num = 0; 
		int exponente = 0;
		
		// Me muevo en la secuencia desde el final al inicio
		// Los ultimos 6 valores de la semisecuencia no se cuentan porque son parte del flag, es 011111
		for(int i = (semisecuencia.size() -7); i >= 0; i--)
		{ 
			
			// Paso el  numero de bit a entero
			if(semisecuencia.get(i) == '1')
			{
				num = num +(int) Math.pow(2, exponente);
			}
			exponente++;
		}
		// Retorno el valor entero
		return num;
	}
  
	// Metodo que controla si se est� parado en el final del encabezado
	private boolean finalEncabezado(char[] secuencia)
	{ 
	// Si los siguientes 8 digitos son 01111110, aumenta la posicion 8 veces y retorna true
	// Es decir que encuentra el final del encabezado y se posiciona al inico del codigo de huffman
		
		int contador = posicion;
		// Si comienza en 0 sigue avanzando en la secuencia
		if(secuencia[contador] == '0')
		{
			contador++;
			
			// Me muevo en los siguientes valores qque deben ser cinco 1's
			for(int i = 1; i <= 6; i++)
			{
				
				// Si encuentra un cero entonces no se llego al final del encabezado
				if(secuencia[contador] == '0')
				{
					return false;
				}
				
				// Si no es cero entonces aumento el contador
				contador++;
			}
			
			// Si se recorrieron los cinco 1's tiene que haber un cero
			if(secuencia[contador] == '0')
			{
				// Encontr� el final del encabezado, entonces pone posicion al comienzo del archivo huffman
				posicion = contador + 1; 
				// Retorna true ya que se termino el encabezado
				return true;
			}
		}
		// No se llego al final del encabezado
		return false;
	}
}