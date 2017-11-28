package ymactors;

import com.yarhoslav.ymactors.core.messages.PoisonPill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;

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
        logger.info("Contador {} cuenta {}", context().name(), contador);
        if (context().envelope().message().equals("contar")) {
            contador--;
            logger.debug("Contador {} cuenta {}", context().name(), contador);
            if (contador <= 0) {
                context().myself().tell(PoisonPill.INSTANCE, context().myself());
            } else {
                context().myself().tell("contar", context().myself());
            }
        }
    }

}
