package ymactors;

import com.yarhoslav.ymactors.core.ActorSystem;
import com.yarhoslav.ymactors.core.actors.EmptyActor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.currentTimeMillis;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;
import com.yarhoslav.ymactors.core.messages.BroadCastMsg;

/**
 *
 * @author YarhoslavME
 */
public class YMActors {

    static final long inicio = currentTimeMillis();
    static final ActorSystem universe = new ActorSystem("TEST");
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
        //TODO: Compare performance with AKKA
        try {
            universe.start();
            status.start();

            for (int i = 0; i < 100000; i++) {
                ContadorActor ca = (ContadorActor) universe.newActor(ContadorActor.class, "CONTADOR" + i);
                ca.setContador(1000);
            }

            ActorRef tmpActor = universe.findActor("/CONTADOR0");
            System.out.println("/CONTADOR0/"+tmpActor.getName());
            tmpActor.getContext().newActor(ContadorActor.class, "OTRO");
            System.out.println("/CONTADOR100/"+universe.findActor("/CONTADOR100").getName());
            System.out.println("/OTRO/"+universe.findActor("/OTRO").getName());
            tmpActor = universe.findActor("/CONTADOR0/OTRO");
            tmpActor.getContext().newActor(ContadorActor.class, "OTRO");
            tmpActor.getContext().newActor(ContadorActor.class, "PERRO");
            tmpActor = universe.findActor("/CONTADOR0/OTRO");
            System.out.println("/CONTADOR0/OTRO/"+tmpActor.getContext().findActor("OTRO").getName());
            System.out.println("/CONTADOR0/OTRO/"+tmpActor.getContext().findActor("PERRO").getName());

            universe.findActor("/CONTADOR0").tell("contar", EmptyActor.getInstance());
            universe.tell(new BroadCastMsg("contar", EmptyActor.getInstance()), EmptyActor.getInstance());

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
