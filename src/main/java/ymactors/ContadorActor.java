package ymactors;

import com.yarhoslav.ymactors.core.DefaultActorHandler;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void process(Object msj, IActorRef pSender) {
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
