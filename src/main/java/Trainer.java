import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Train;

import java.io.IOException;

/**
 * Created by yaonengjun on 2017/10/20 下午7:09.
 */
public class Trainer {

  public static void main(String[] args) throws Exception {
//    Train.main(args);
    Train.main(new String[] {"-C", "/Users/neyao/workspace/machine_learning/liblinear-java/src/test/resources/iris.scale"});
  }

  public void traning() {

  }
}
