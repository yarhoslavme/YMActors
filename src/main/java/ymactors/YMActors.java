package ymactors;

import com.yarhoslav.ymactors.core.ActorsContainer;
import com.yarhoslav.ymactors.core.DefaultActorHandler;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import static com.yarhoslav.ymactors.core.messages.PoisonPill.getInstance;
import com.yarhoslav.ymactors.utils.Constants;
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
    static final ActorsContainer ac = new ActorsContainer("TEST");
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

                System.out.println(ac.getEstadistica() + " Tiempo:" + (currentTimeMillis() - inicio));
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

        ac.start();
        YMActors yma = new YMActors();

        //yma.pingpong();
        yma.test1();

    }

    void pingpong() {
        System.out.println("PRUEBA DE PING-PONG");
        try {
            status.start();
            int maxactores = 200000;
            IActorRef[] pong = new IActorRef[maxactores];
            IActorRef[] ping = new IActorRef[maxactores];
            for (int i = 0; i < maxactores; i++) {
                pong[i] = ac.createActor("pong" + i, new DefaultActorHandler() {
                    //Poner referencia a AtomicIntenger
                    int contador = 0;

                    @Override
                    public void process(Object msj) {
                        if (contador++ >= 1000) {
                            //System.out.println(getActorRef().getName() + " - " + contador.toString());
                            this.getMyself().tell(getInstance(), null);
                        }
                        if ("ping".equals(msj)) {
                            this.getMyself().getSender().tell("pong", this.getMyself());
                        }
                    }
                });
                ping[i] = ac.createActor("ping" + i, new DefaultActorHandler() {
                    int contador = 0;

                    @Override
                    public void process(Object msj) {
                        /*
                         long threadId = Thread.currentThread().getId();
                         if (getActorRef().getName().equals("ping0")) {
                         System.out.println("Hilo:" + threadId);
                         }*/
                        if (contador++ >= 1000) {
                            //System.out.println(getActorRef().getName() + " - " + contador.toString());
                            this.getMyself().tell(getInstance(), null);
                        }
                        if ("pong".equals(msj)) {
                            this.getMyself().getSender().tell("ping", this.getMyself());
                        }
                    }
                });
                pong[i].tell("ping", ping[i]);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            try {
                buf.readLine();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            ac.ShutDownNow();
            status.interrupt();
        }
    }

    void test1() {
        try {
            status.start();

            for (int i = 0; i < 100000; i++) {
                IActorRef ca = ac.createActor("CONTADOR" + i, new ContadorActor(1000));
                //ca.tell("contar", null);
            }

            IActorRef tmp = (IActorRef) ac.getContext().get(Constants.ADDR_BROADCAST);
            tmp.tell("contar", null);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            try {
                buf.readLine();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            ac.ShutDownNow();
            status.interrupt();
        }
    }
}
