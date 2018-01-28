package eks.verwaltung.veranstalter;

import java.util.ArrayList;
import de.thkoeln.eksc.osgi.entitaetsklassen.Veranstalter;


/**
 * Singleton Klasse fuer das Speichern von Veranstaltern
 * 
 * @author Benjamin Jakob
 * @author Phillip Braun
 */
public class AlleVeranstalter {
    
    private static AlleVeranstalter instance;
    private final ArrayList<Veranstalter> veranstalter;
    
    private AlleVeranstalter(){
        veranstalter = new ArrayList<>();
    }
    
    public static AlleVeranstalter getInstance(){
        if(instance == null)
            instance = new AlleVeranstalter();
        return instance;
    }

    public ArrayList<Veranstalter> getVeranstalter() {
        return veranstalter;
    }
    
    public boolean addVeranstalter(Veranstalter v){
        return veranstalter.add(v);
    }
    
    public Veranstalter getVeranstalterById(int id){
        for(Veranstalter v: veranstalter){
            if(v.getVeranstalternr() == id)
                return v;
        }
        return null;
    }
}
