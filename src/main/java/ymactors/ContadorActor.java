package ymactors;

import me.yarhoslav.ymactors.core.messages.PoisonPill;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author YarhoslavME
 */
public class ContadorActor extends SimpleExternalActorMind {

    Logger logger = LoggerFactory.getLogger(ContadorActor.class);
    private int contador;

    public ContadorActor(int pContador) {
        contador = pContador;
    }

    @Override
    public void process() throws Exception {
        if (context().envelope().message().equals("contar")) {
            contador--;
            logger.debug("Contador {} cuenta {}", context().name(), contador);
            if (contador <= 0) {
                context().myself().tell(PoisonPill.INSTANCE, context().myself());
                logger.debug("Contador {} cuenta {} - finalizando", context().name(), contador);
            } else {
                context().myself().tell("contar", context().myself());
            }
        }
    }

}
