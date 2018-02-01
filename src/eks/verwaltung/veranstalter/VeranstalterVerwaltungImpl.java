package eks.verwaltung.veranstalter;

import java.util.ArrayList;

import de.thkoeln.eksc.osgi.entitaetsklassen.Reise;
import de.thkoeln.eksc.osgi.entitaetsklassen.Veranstalter;
import de.thkoeln.eksc.osgi.reiseverwaltung.ReiseVerwaltung;
import de.thkoeln.eksc.osgi.veranstalterverwaltung.VeranstalterVerwaltung;
import de.thkoeln.eksc.osgi.zentraledienste.VerwaltungNummern;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * Klasse fuer die Verwaltung von Veranstaltern.
 * Implementation des vorgegebenen Interfaces VeranstalterVerwaltung
 * 
 * @author Benjamin Jakob
 * @author Phillip Braun
 */
public class VeranstalterVerwaltungImpl implements VeranstalterVerwaltung {

    private final AlleVeranstalter alleVeranstalter;
    private final BundleContext context;

    public VeranstalterVerwaltungImpl(BundleContext context){
        alleVeranstalter = AlleVeranstalter.getInstance();
        this.context = context;
    }

    @Override
    public int neuerVeranstalter(String name, String adresse) {

        int veranstalterId = -1;
        ServiceReference refs;

        refs = context.getServiceReference(VerwaltungNummern.class);
        if(refs != null) {
            VerwaltungNummern verwaltungnummern = (VerwaltungNummern)context.getService(refs);
            veranstalterId = verwaltungnummern.getNeueVeranstalterNr();
            
            Veranstalter v = new Veranstalter();
            v.setVeranstalternr(veranstalterId);
            v.setName(name);
            v.setAdresse(adresse);
            
            alleVeranstalter.addVeranstalter(v);
        } else {
            System.out.println("Fehler in Veranstalterverwaltung: Service 'VerwaltungNummern' nicht gefunden!");
        }
        return veranstalterId;
    }

    
    @Override
    public void veranstaltetReise(Veranstalter v, int reisenr) {
        if(v!= null){
            v.addReise(reisenr);
            System.out.println(
                    "Reise: " + reisenr
                    + " wurde dem Veranstalter: " + v.getName() 
                    + " hinzugefuegt."
            );
        } else {
            System.out.println("Veranstalter = null!!");
        }
    }

    @Override
    public Veranstalter getVeranstalter(int veranstalternr) {
        return alleVeranstalter.getVeranstalterById(veranstalternr);
    }

    @Override
    public ArrayList<Veranstalter> alleVeranstalter() {
        return alleVeranstalter.getVeranstalter();
    }

    @Override
    public ArrayList<Reise> alleVeranstalteteReisen(Veranstalter v) {
        ServiceReference refs;
        ArrayList<Reise> reisen = null;

        refs = context.getServiceReference(ReiseVerwaltung.class);
        if(refs != null) {
            ReiseVerwaltung reiseVerwaltung = (ReiseVerwaltung) context.getService(refs);
            ArrayList<Reise> alleReisen = reiseVerwaltung.alleReisen();
            reisen = new ArrayList<>();
            for(Reise r: alleReisen){
                if(v.getReisen().contains(r.getReisenummer()))
                    reisen.add(r);
            }
        } else {
            System.out.println("Fehler in Veranstalterverwaltung: Service 'ReiseVerwaltung' nicht gefunden!");
        }
        return reisen;
    }
}
