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

    /**
     * Konstruktor des Klasse VeranstalterVerwaltungImpl.
     * 
     * @param context Bundlecontext um auf die bundels Reiseverwaltung
     *                und VerwaltungNummern zugreifen zu koennen  
     */
    public VeranstalterVerwaltungImpl(BundleContext context){
        alleVeranstalter = AlleVeranstalter.getInstance();
        this.context = context;
    }

    
    /**
     * Erzeugt ein neues Objekt der Klasse Veranstalter.
     * Hierzu muss auf das Interface Verwaltung Nummern
     * zugegriffen werden.
     *
     * @param name Name der neuen Veranstalters
     * @param adresse Adresse des neuen Veranstalters
     *
     * @return Nummer des neuen Veranstalters
     */
    @Override
    public int neuerVeranstalter(String name, String adresse) {

        int veranstalterId = -1;
        ServiceReference[] refs;

        try {
            refs = context.getServiceReferences(
                    VerwaltungNummern.class.getName(), 
                    "(Verwaltung=Nummern)");

            if(refs != null) {
                VerwaltungNummern verwaltungnummern = (VerwaltungNummern)context.getService(refs[0]);
                veranstalterId = verwaltungnummern.getNeueVeranstalterNr();

                Veranstalter v = new Veranstalter();
                v.setVeranstalternr(veranstalterId);
                v.setName(name);
                v.setAdresse(adresse);

                alleVeranstalter.addVeranstalter(v);
            } else {
                System.out.println("Fehler in Veranstalterverwaltung: Service 'VerwaltungNummern' nicht gefunden!");
            }
        } catch (InvalidSyntaxException ex){
            System.out.println("Fehler in Veranstalterverwaltung:");
            System.out.println(ex.getMessage());
        }
        return veranstalterId;
    }

    
    /**
     * Durch Aufruf der Methode wird angezeigt, dass ein Veranstalter
     * eine bestimmte Reise anbietet.
     *
     * @param v Veranstalter, der die Reise mit der Nummer reisenr
     * anbietet
     * @param reisenr Nummer der Reise, die der Veranstalter v anbietet.
     */
    @Override
    public void veranstaltetReise(Veranstalter v, int reisenr) {
        v.addReise(reisenr);
        System.out.println(
                "Reise: " + reisenr
                + " wurde dem Veranstalter: " + v.getName() 
                + " hinzugefuegt."
        );
    }

    
    /**
     * Liefert das Veranstalter-Objekt zu einer gegebenen
     * Veranstalternummer
     *
     * @param veranstalternr Nummer des gesuchten Veranstalters
     *
     * @return Veranstalter-Objekt mit der Nummer veranstalternr
     */
    @Override
    public Veranstalter getVeranstalter(int veranstalternr) {
        return alleVeranstalter.getVeranstalterById(veranstalternr);
    }

    
    /**
     * Liefert alle bestehenden Veranstalter
     *
     * @return Liste der bestehenden Veranstalter
     */
    @Override
    public ArrayList<Veranstalter> alleVeranstalter() {
        return alleVeranstalter.getVeranstalter();
    }

    
    /**
     * Liefert die Liste aller Reisen, die der übergebene Veranstalter
     * anbietet.
     * Hierzu muss auf das Interface ReiseVerwaltung zugegriffen werden.
     *
     * @param v Veranstalter, dessen Reisen gesucht werden
     *
     * @return Liste der Reisen des Veranstalters v
     */
    @Override
    public ArrayList<Reise> alleVeranstalteteReisen(Veranstalter v) {
        ServiceReference[] refs;
        ArrayList<Reise> reisen = null;

        try {
            refs = context.getServiceReferences(ReiseVerwaltung.class.getName(), "Verwaltung=Reise");

            if(refs != null) {
                ReiseVerwaltung reiseVerwaltung = (ReiseVerwaltung) context.getService(refs[0]);
                ArrayList<Reise> alleReisen = reiseVerwaltung.alleReisen(); 
                reisen = new ArrayList<>();
                for(Reise r: alleReisen){
                    if(v.getReisen().contains(r.getReisenummer()))
                        reisen.add(r);
                }
            } else {
                System.out.println("Fehler in Veranstalterverwaltung: Service 'ReiseVerwaltung' nicht gefunden!");
            }
        } catch (InvalidSyntaxException ex) {
            System.out.println("Fehler in Veranstalterverwaltung:");
            System.out.println(ex.getMessage());
        }
        return reisen;
    }
}
