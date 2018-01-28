package eks.verwaltung.veranstalter;

import java.util.Hashtable;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;
import de.thkoeln.eksc.osgi.veranstalterverwaltung.VeranstalterVerwaltung;

/**
 * Klasse implementiert ein bundle zur Verwaltung von Veranstaltern.
 * 
 * @author Benjamin Jakob
 * @author Phillip Braun
 */
public class Activator implements BundleActivator {
    
    @Override
    public void start(BundleContext context) throws Exception {
        
        Hashtable<String, String> props = new Hashtable<>();
        props.put("Verwaltung", "Veranstalter");
        
        context.registerService(
                VeranstalterVerwaltung.class.getName(), 
                new VeranstalterVerwaltungImpl(context), 
                props
        );
        
        System.out.println("Started Service: VeranstalterVerwaltung");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // NOTE: The service is automatically unregistered.
        System.out.println("Stopped Service: VeranstalterVerwaltung");
    }
}
