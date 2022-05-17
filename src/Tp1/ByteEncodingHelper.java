package Tp1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ByteEncodingHelper 
{
	private static int bufferLength = 8;
	
	// Guardo la secuencia de chars en una lista de bytes
	public static List<Byte> EncodeSequence(char[] sequence) 
	{
		List<Byte> result = new ArrayList<Byte>();

		byte buffer = 0;
		int bufferPos = 0;

		int i = 0;
		// Te moves en la secuencia para ir pasandola a una secuencia de bytes
		while (i < sequence.length) 
		{
			// La operacion de corrimiento pone un '0'
			buffer = (byte) (buffer << 1);
			bufferPos++;
			
			// Agrega un 1 si en la secuncia de chars se indicaba
			if (sequence[i] == '1') 
			{
				buffer = (byte) (buffer | 1);  // 0 0 0 0 0 0 0 1
			}
			
			// Si el byte esta completo, se agrega el byte a la secuencia y se lo reinicia
			if (bufferPos == bufferLength) 
			{
				result.add(buffer);
				buffer = 0;
				bufferPos = 0;
			}
			i++;
		}
		
		// Si la secuencia de chars no es multiplo de ocho entonces se debe completar el ultimo byte con 0's y guardarlo en la secuencia
		if(bufferPos > 0)
		{ 
			// Si la cantidad de la secuencia no es multiplo de 8, entonces relleno con ceros hasta completar el byte.
			for(int k = bufferPos + 1; k <= bufferLength; k++)
			{
				//Relleno con ceros, es decir con corrimiento a izquierda.
				buffer = (byte) (buffer << 1);
			}
			result.add(buffer);
		}
		//Retorna la lita de bytes
		return result;
	}

	// Carga el archivo dado por la ruta y lo pasa de bytes a un arreglo de chars
	public static char[] DecodeSequence(String inputFilepath) 
	{
		char[] restoredSequence = new char[0];
		
		try 
		{
			// Lee el archivo y lo guarda en un arreglo de bytes
			byte[] inputSequence = Files.readAllBytes(new File(inputFilepath).toPath());
			
			// Obtengo el tama�o del nuevo arreglo donde guardare la secuencia de chars de 1's y 0's
			// Es el tama�o de la secuencia de byes multiplicado por ocho(8 bits por byte)
			int sequenceLength = inputSequence.length * 8 ; 
			
			// La creo con ese tama�o
			restoredSequence = new char[sequenceLength]; 
			
			int globalIndex = 0;			
			byte mask = (byte) (1 << (bufferLength - 1)); // mask: 10000000
			int bufferPos = 0;
			
			// Indice en la lista de bytes (secuencia codificada)
			int i = 0; 
			//Me muevo en en arreglo de byes
			while (globalIndex < sequenceLength) 
			{
				
				byte buffer = inputSequence[i];	
				//Me muevo por el byte
				while (bufferPos < bufferLength) 
				{
					
					//Guardo el bit en el arreglo de chars
					if ((buffer & mask) == mask)  // 10000000
					{  
						restoredSequence[globalIndex] = '1';
					} 
					else 
					{
						restoredSequence[globalIndex] = '0';
					}
					
					buffer = (byte) (buffer << 1);
					bufferPos++;
					globalIndex++;
					
					if (globalIndex == sequenceLength) 
					{
						break;
					}
				}
				
				i++;
				bufferPos = 0;
			}
			
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		//Retorno el arreglo de chars
		return restoredSequence;
	}
}