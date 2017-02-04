package ymactors;

import com.yarhoslav.ymactors.core.DefaultActorHandler;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;

/**
 *
 * @author YarhoslavME
 */
public class ContadorActor extends DefaultActorHandler {
    
    Logger logger = LoggerFactory.getLogger(ContadorActor.class);
    private int contador;
    
    public ContadorActor(int pInicial) {
        contador = pInicial;
    }

    @Override
    public void process(Object msj, ActorRef pSender) {
        if (msj.equals("contar")) {
            contador--;
            if (contador <= 0) {
                  getMyself().tell(PoisonPill.getInstance(), getMyself());
            } else {
                getMyself().tell("contar", getMyself());
            }
        }
    }
    
}
