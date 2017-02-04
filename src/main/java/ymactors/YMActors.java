package ymactors;

import com.yarhoslav.ymactors.core.ActorsUniverse;
import com.yarhoslav.ymactors.core.actors.EmptyActor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.currentTimeMillis;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;

/**
 *
 * @author YarhoslavME
 */
public class YMActors {

    static final long inicio = currentTimeMillis();
    static final ActorsUniverse universe = new ActorsUniverse("TEST");
    static final Runnable hilo = new Runnable() {
        public AtomicBoolean parar = new AtomicBoolean(false);

        @Override
        public void run() {
            while (!parar.get()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    parar.set(true);
                }

                System.out.println(universe.getEstadistica() + " Tiempo:" + (currentTimeMillis() - inicio));
            }
        }
    };
    static final Thread status = new Thread(hilo);

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException If any error occurs
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here

        YMActors yma = new YMActors();

        yma.test1();

    }

    void test1() {
        try {
            universe.start();
            status.start();

            for (int i = 0; i < 1; i++) {
                ActorRef ca = universe.createActor("CONTADOR" + i, new ContadorActor(2));
            }

            ActorRef tmpActor = universe.findActor("/CONTADOR0");
            tmpActor.getContext().createActor("OTRO", null);
            System.out.println(universe.findActor("/CONTADOR100").getName());
            System.out.println(universe.findActor("/OTRO").getName());
            tmpActor = universe.findActor("/CONTADOR100/OTRO");
            tmpActor.getContext().createActor("OTRO", null);
            tmpActor.getContext().createActor("PERRO", null);
            tmpActor = universe.findActor("/CONTADOR100/OTRO");
            System.out.println(tmpActor.getContext().findActor("OTRO").getName());
            System.out.println(tmpActor.getContext().findActor("PERRO").getName());

            universe.tell("contar", EmptyActor.getInstance());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            try {
                buf.readLine();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            universe.ShutDownNow();
            status.interrupt();
        }
    }
}
