package Tp1;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DecodificadorRLC {
    String contenido;

    public DecodificadorRLC(){
        this.contenido="";
        }


    public String getContenido(File comprimido)throws FileNotFoundException //Obtiene el contenido del archivo comprimido
    {
        Scanner scanner = new Scanner(comprimido);
        String cadena="";
        while (scanner.hasNext()) {
            cadena = scanner.nextLine();
            this.contenido += cadena;
        }
        scanner.close();
        return contenido;
    }


    public void decodificar(String ruta, FileWriter fw) throws IOException {     //Decodifica el archivo recibido 
        
    	File comprimido = new File(ruta);
    	String contenido=this.getContenido(comprimido);
        String resultado = "";
        int repeticiones=0;

        for (int i=0; i < contenido.length()-2; i++) {    //Recorre el contenido hasta encontrar un caracter separador
            if(contenido.charAt(i)!='&'){
                resultado += contenido.charAt(i);

            }else
                {
                repeticiones=Integer.parseInt(contenido.substring(i+1, i+3));    //Obtiene la cantidad de veces que debe escribirse el valor

                //Escritura en el archivo
                for (int k=0; k<repeticiones; k++) {
                    fw.write(resultado + '\n');
                }

                resultado="";
                i=i+2;

            }
        }
    }
}