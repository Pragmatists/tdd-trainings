package tdd.seams;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class Ping {

    /**
     * Stwierdza czy usluga pod podanym adresem url jest dostepna. Usluga jest uwazana za 
     * dostepna jezeli w tresci odpowiedzi HTTP zawarty jest status <code>OK</code>.
     * 
     * @param url adres uslugi do sprawdzenia
     * @return  <code>true</code> w przypadku gdy usluga zwraca status <code>OK</code>,
     *          <code>false</code> w przeciwnym przypadku
     */

    public boolean ping(String url) {
        
        InputStream response = null;

        try{
            
            response = new URL(url).openStream();
            String responseText = IOUtils.toString(response);
            return responseText.contains("Status: OK");
            
        } catch(Exception e){
         
            e.printStackTrace();
            return false;
            
        } finally{
            
            IOUtils.closeQuietly(response);
        }
    }
    
}
