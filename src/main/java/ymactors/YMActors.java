package ymactors;

import com.yarhoslav.ymactors.core.ActorSystem;
import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.messages.ErrorMsg;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.currentTimeMillis;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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

        yma.test3();

    }

    void test1() {
        //TODO: Compare performance with AKKA
        try {
            universe.start();
            status.start();
            
            ContadorActor ca = null;
            for (int i = 0; i < 100000; i++) {
                ca = (ContadorActor) universe.addActor(new ContadorActor(10000), "CONTADOR" + i);
                ca.start();
                ca.tell("contar", EmptyActor.getInstance());
            }
            
            System.out.println(ca.getName());
/*
            IActorRef tmpActor = universe.findActor("/CONTADOR0");
            System.out.println("/CONTADOR0/"+tmpActor.getName());
            tmpActor.getContext().newActor(new ContadorActor(5), "OTRO");
            System.out.println("/CONTADOR100/"+universe.findActor("/CONTADOR100").getName());
            System.out.println("/OTRO/"+universe.findActor("/OTRO").getName());
            tmpActor = universe.findActor("/CONTADOR0/OTRO");
            tmpActor.getContext().newActor(new ContadorActor(2), "OTRO");
            tmpActor.getContext().newActor(new ContadorActor(10), "PERRO");
            tmpActor = universe.findActor("/CONTADOR0/OTRO");
            System.out.println("/CONTADOR0/OTRO/"+tmpActor.getContext().findActor("OTRO").getName());
            System.out.println("/CONTADOR0/OTRO/"+tmpActor.getContext().findActor("PERRO").getName());*/

            //universe.findActor("/CONTADOR0").tell("contar", EmptyActor.getInstance());
            //universe.tell(new BroadCastMsg("contar", EmptyActor.getInstance()), EmptyActor.getInstance());

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
        
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            try {
                buf.readLine();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
    }

    void test3() {
        try {
            IActorMsg poison = PoisonPill.getInstance();
            System.out.println(poison.toString());
            poison = new ErrorMsg(new IllegalAccessException(), null);
            System.out.println(poison.toString());
            poison = new ErrorMsg(new IllegalAccessException(), null);
            System.out.println(poison.toString());
            poison = PoisonPill.getInstance();
            System.out.println(poison.toString());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    void test2() {
        //TODO: Compare performance with AKKA
        try {
            universe.start();
            status.start();
            
            ContadorActor ping = (ContadorActor)universe.addActor(new ContadorActor(100000), "PING");
            ContadorActor pong = (ContadorActor)universe.addActor(new ContadorActor(100000), "PONG");
            
            ping.start();
            pong.start();
            
            ping.tell("contar", pong);

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
        
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            try {
                buf.readLine();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
    }
    
}
