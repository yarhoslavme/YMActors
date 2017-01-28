package ymactors;

import com.yarhoslav.ymactors.core.DefaultActorHandler;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author YarhoslavME
 */
public class ContadorActor extends DefaultActorHandler {
    private static final Logger LOGGER = getLogger(ContadorActor.class.getName());
    private int contador;
    
    public ContadorActor(int pInicial) {
        contador = pInicial;
    }

    @Override
    public void process(Object msj) {
        if (msj.equals("contar")) {
            contador--;
            if (contador <= 0) {
                  this.getMyself().tell(PoisonPill.getInstance(), getMyself());
            } else {
                this.getMyself().tell("contar", getMyself());
            }
        }
    }
    
}
