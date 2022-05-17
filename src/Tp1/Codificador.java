package Tp1;


import java.util.HashMap;
import java.util.List;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;



public class Codificador {
	public Codificador() {}
	 
	public HashMap<Integer,Double> getFrecuencias(senial s){
		HashMap<Integer,Double> map = new HashMap<Integer,Double>();
		ArrayList<Integer> array = s.getArray();
		for (int i=0; i<array.size(); i++) {
			if (map.containsKey(array.get(i))) {
				map.replace(array.get(i), map.get(array.get(i)) + (double)  1);
			}else {
				map.put(array.get(i), (double) 1);
			}		
		}
		return map;
	}
	
	public HashMap<Integer,Double> getDistribuciones(senial s){
		HashMap<Integer,Double> map = this.getFrecuencias(s);
		int size = s.getSizeArray();
		for (HashMap.Entry<Integer, Double> entry : map.entrySet()) {
		    entry.setValue(entry.getValue()/size);
		}
		return map;
	}
	
	public String RLC (senial s){
		 
		ArrayList<Integer> array = s.getArray();

        String resultado = "";
        int contador = 1;
        String valorActual = String.valueOf(array.get(0));
        String aux="";
 
        for (int i = 0; i < array.size()-1; i++) {
          
            if (valorActual.equals(String.valueOf(array.get(i+1)))){
            	contador ++;
            	
            }else{
            	
            	if(contador<10) {
            		aux = 0 + String.valueOf(contador);
            		resultado += valorActual + "&" + aux ;
            	}else{
            		resultado += valorActual + "&" + contador ;
            	}
            	
             contador = 1;
            	
            }
           
            
        valorActual = String.valueOf(array.get(i+1));
        }
        
        if(contador<10) {
    		aux = 0 + String.valueOf(contador);
    		resultado += valorActual + "&" + aux ;
    	}else{
    		resultado += valorActual + "&" + contador ;
    	}
       
        
        
        return resultado;
    }
	
	
	public Codificador(int cantidad, HashMap<Integer, Double> hashMap, ArrayList<Character> arcHuffman, String ruta)
	{
	
		//Aqui van guardados todos lo datos a nivel bits, con sus determinados flags delimitadores
		List<Character> compresion = new ArrayList<Character>();
		/* La lista contiene un encabezado donde se guarda la cantidad de elementos y distribucion de valores del archivo
		   Cada uno de los valores esta codificado en bits, y para separarlos se usa un flag
		   Dicho flag de separacion es el siguiente 01111110
		   Ademas si en el valor se encuentran cinco 1s seguidos, entonces se le agrega un 0, para que nunca se lleguen a seis 1's seguidos,
		   de modo que no se lo pueda confundir con un flag
		   El final del encabezado se delimita con dos flags seguidos
		   Luego del encabezado se agrega el archivo codificado en huffman. */
	
		// Guardo la cantidad de elementos del archivo
		guardar(cantidad, compresion);
		

		// Guardo los valores con sus ocurrencias del archivo
		for (Map.Entry <Integer, Double> iterador: hashMap.entrySet())
		{
			// Guardo el valor
			guardar(iterador.getKey(), compresion);
			// Y luego las ocurrencias del mismo
			guardar(iterador.getValue().intValue(), compresion);
		}

		// Agrego un ultimo delimitador 01111110 para establecer el final del encabezado
		compresion.add('0');
		compresion.add('1');
		compresion.add('1');
		compresion.add('1');
		compresion.add('1');
		compresion.add('1');
		compresion.add('1');
		compresion.add('0');
		// De modo que el flag del encabezado sea 0111111001111110
    
		// Un arreglo de char para en el que ira el encabezado seguido del archivo en codigo huffman
		char[] secuencia = new char[(compresion.size() + arcHuffman.size())];

		// Guardo la cabecera del archivo
		for(int i = 0; i < compresion.size(); i++)
		{
			secuencia[i] = compresion.get(i);
		}

		// Guardo el codigo de huffman
		for(int i = (compresion.size()); i < (compresion.size() + arcHuffman.size()); i++)
		{
			secuencia[i] = arcHuffman.get(i-compresion.size());
		}

		// Paso toda la secuencia a bytes con el metodo EncodeSecuence y la guardo en un archivo
		try 
		{
			// Obtener bytes que contienen la secuencia original codificada
			byte[] byteArray = ConvertByteListToPrimitives(ByteEncodingHelper.EncodeSequence(secuencia));
			/* Esta secuencia va a contener el archivo huffman luego del encabezado
			   Al guardar la secuencia de 1's y 0's en bytes, esta secuencia tiene que ser multiplo de ocho
			   Pero si no es asi se agregaran 0's al final para completar el byte
			   Luego cuando se recorra el archivo huffman descomprido para decodificarlo, se movera la cantidad de veces dada por 
			   la cantidad de elementos del archivo, de modo que nunca se leeran los 0's agregados para completar el ultimo byte de la secuencia */

			// Guardar la compresion en un archivo binario en la ruta especificada			
			FileOutputStream fos = new FileOutputStream(ruta);
			fos.write(byteArray);
			fos.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	// Pasa un numero a binario devolviendo una lista de 1's y 0's
	private List<Integer> aBinario(int num)
	{
		int dividendo = num;
		int mod;
		List<Integer> binario = new ArrayList<Integer>();
		if(dividendo == 0) 
		{
			binario.add(0);
		}
		while(dividendo > 1) 
		{
			mod = dividendo % 2;
			dividendo = dividendo/2;
			binario.add(mod);
		}
		if(dividendo == 1) 
		{
			binario.add(1);
		}
		return binario;
	}
						
	// Guarda un entero en una lista de caracreres a nivel bit junto con su delimitador
	private void guardar(int num, List<Character> compresion)
	{
		// Pasa el numero a binario
		List<Integer> bin = aBinario(num);
		
		// Para agregar un 0 luego de poner 5 1's seguidos
		int contador = 0; 
		
		// Se mueve en la lista del numero en binario
		for(int i = (bin.size() - 1); i >= 0; i--)
		{
			// Si el numero es un 1
			if(bin.get(i) == 1)
			{
				
				// Sumo cantidad de 1's seguidos
				contador++; 
				
				// Agrego un 1 a la lista de compresion
				compresion.add('1');
				
				// Si contador es cinco, significa que hubo cinco 1's seguidos, por lo que se agrega un 0 para que no se confunda con un flag 
				if(contador == 5)
				{
					compresion.add('0');
					//Se reinicia el contador
					contador = 0;
				}
			}
			
			// Si el numero es un 0, entonces se agrega un cero a la compresion y se reinicia el contador
			else
			{
				compresion.add('0');
				contador = 0;
			}
		}
		
		//Agrego 01111110 como delimitador
		compresion.add('0');
		compresion.add('1');
		compresion.add('1');
		compresion.add('1');
		compresion.add('1');
		compresion.add('1');
		compresion.add('1');
		compresion.add('0');
	}

	// Convierte una lista de bytes a un arreglo de bytes
	private static byte[] ConvertByteListToPrimitives(List<Byte> input)
	{
		byte[] ret = new byte[input.size()];
		for (int i = 0; i < ret.length; i++) 
		{
			ret[i] = input.get(i);
		}
		return ret;
	}
	
}
