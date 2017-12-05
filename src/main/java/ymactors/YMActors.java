package ymactors;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.system.ActorSystem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.currentTimeMillis;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import me.yarhoslav.ymactors.core.actors.NullActor;

/**
 *
 * @author YarhoslavME
 */
public class YMActors {

    static final long inicio = currentTimeMillis();
    static final ActorSystem user = new ActorSystem("TEST");
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

                System.out.println(" Tiempo:" + (currentTimeMillis() - inicio));
                System.out.println(user.estadistica());
            }
        }
    };
    static final Thread status = new Thread(hilo);

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException If any error occurs
     */
    public static void main(String[] args) throws InterruptedException {
        YMActors yma = new YMActors();
        yma.test2();
    }

    void test1() {
        //TODO: Compare performance with AKKA
        try {
            status.start();

            IActorRef contador = user.createActor(new ContadorActor(10), "contador");
            contador.tell("contar", contador);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            try {
                buf.readLine();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            user.shutdown();
            status.interrupt();
        }

        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            buf.readLine();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    void test2() {
        //TODO: Compare performance with AKKA
        try {
            status.start();

            int TOTALACTORES = 500;
            System.out.println("CREANDO ACTORES: " + TOTALACTORES);

            ContadorActor contador;
            for (int i = 0; i < TOTALACTORES; i++) {
                contador = new ContadorActor(1000000);
                IActorRef x = user.createActor(contador, "contador" + i);
                //System.out.println(x.id());
                //for (int j = 0; j < 10; j++) {
                //    IActorRef y = contador.context().createMinion(new ContadorActor(10), "contador" + j);
                //    System.out.println(y.id());
                //}
            }

            System.out.println("INICIANDO MENSAJES!!!!!");
            IActorRef otro;
            for (int i = 0; i < TOTALACTORES; i++) {
                try {
                    otro = user.findActor("/contador" + i);
                    System.out.println(otro.id());
                    otro.tell("contar", NullActor.INSTANCE);
                } catch (IllegalArgumentException e) {
                    System.out.println(e);
                }
            }
            System.out.println("YA SE ENVIARON TODOS LOS MENSAJES!!!!!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            try {
                buf.readLine();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            user.shutdown();
            System.out.println("EJECUCION FINALIZADA!!!!!");
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
        //TODO: Compare performance with AKKA
        try {
            status.start();

            int TOTALACTORES = 5;
            System.out.println("CREANDO ACTORES: " + TOTALACTORES);

            ContadorActor contador;
            for (int i = 0; i < TOTALACTORES; i++) {
                contador = new ContadorActor(10);
                IActorRef x = user.createActor(contador, "contador" + i);
                System.out.println(x.id());
                for (int j = 0; j < 3; j++) {
                    IActorRef y = contador.context().createMinion(new ContadorActor(10), "contador" + j);
                    System.out.println(y.id());
                }
            }

            System.out.println("Checheo de FindActor");
            System.out.println(user.findActor("/contador0").id());
            System.out.println(user.findActor("/contador0/contador1").id());
            System.out.println(user.findActor(user.name() + "://" + "contador0/contador1").id());
            System.out.println(user.findActor("userspace/" + "contador0/contador1").id());

            System.out.println(user.findActor("/contador2").id());
            System.out.println(user.findActor("/contador2/contador1").id());
            System.out.println(user.findActor(user.name() + "://" + "contador2/contador1").id());
            System.out.println(user.findActor("userspace/" + "contador0/contador1").id());

            System.out.println(user.findActor("/contador1").id());
            System.out.println(user.findActor("/contador1").id());
            System.out.println(user.findActor(user.name() + "://" + "contador1").id());
            System.out.println(user.findActor("userspace/" + "contador1").id());

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            try {
                buf.readLine();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            user.shutdown();
            status.interrupt();
        }
/*
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            buf.readLine();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
*/
    }

}
