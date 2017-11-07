package ymactors;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author YarhoslavME
 */
public class ContadorActor extends BaseActor {

    Logger logger = LoggerFactory.getLogger(ContadorActor.class);
    private int contador;
    
    public ContadorActor(int pContador) {
        contador = pContador;
    }

    @Override
    public void process(Object msj, IActorRef pSender) {
        if (msj.equals("contar")) {
            contador--;
            if (contador <= 0) {
                tell(PoisonPill.getInstance(), this);
            } else {
                tell("contar", this);
            }
        }
    }

}
