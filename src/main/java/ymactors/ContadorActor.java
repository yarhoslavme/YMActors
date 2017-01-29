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
    
    static final Logger LOGGER = getLogger(ContadorActor.class.getName());
    private int contador;
    
    public ContadorActor(int pInicial) {
        contador = pInicial;
    }

    @Override
    public void process(Object msj) {
        if (msj.equals("contar")) {
            contador--;
            if (contador <= 0) {
                //TODO: Remove this line
                LOGGER.log(Level.INFO, "{0} time to die. PoisonPill taken.", getMyself().getName());
                  this.getMyself().tell(PoisonPill.getInstance());
            } else {
                this.getMyself().tell("contar", getMyself());
            }
        }
    }
    
}
