package de.bwaldvogel.liblinear;

import static de.bwaldvogel.liblinear.Linear.atof;
import static de.bwaldvogel.liblinear.Linear.atoi;
import static de.bwaldvogel.liblinear.Linear.printf;
import static de.bwaldvogel.liblinear.Linear.setDebugOutput;

import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.SolverType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Created by yaonengjun on 2017/10/20 下午7:21.
 */
public class MyPredictor {

  public static void main(String[] args) throws Exception {

    File file = new File("/Users/neyao/dev/machine_learning/liblinear/data/newmodel");
    Model model = Linear.loadModel(file);
//    model.
//    model.solverType = SolverType.L2R_L2LOSS_SVR;
    System.out.println(model);
    model.isProbabilityModel();
    System.out.println("model.isProbabilityModel() 1" + model.isProbabilityModel());
    model.isProbabilityModel();

//    predict(model);
    //doPredictOneLineOfString(model);
    //doPredictAllFromTxt(model);

    test(model);


  }

  public static void test(Model model) throws IOException {


    List<String> lines = FileUtils.readLines(new File("/Users/neyao/dev/machine_learning/liblinear/data/data_part" +
            ".txt"));
    System.out.println("lines: " + lines.size());
    int lineCounter = 1;

    model.solverType = SolverType.L2R_LR_DUAL;

    for (String line : lines) {
//      String[] strArray = line.split("\t");
      String[] strArray = StringUtils.split(line);

      Feature[] oneTestLine = new Feature[strArray.length-1];
      for (int i = 1; i < strArray.length; i++) {
        String[] ss = strArray[i].split(":");
        oneTestLine[i-1] = new FeatureNode(Integer.parseInt(ss[0]), 1.0);
      }

      System.out.println();
      System.out.println("line: "+ lineCounter++);
      System.out.println(Linear.predict(model, oneTestLine));

      double[] prob_estimates1 = new double[model.nr_class];
      System.out.print(Linear.predictProbability(model, oneTestLine, prob_estimates1));
      System.out.print("  ");
      for (int j = 0; j < model.nr_class; j++) {
        System.out.print(prob_estimates1[j]);
        System.out.print("  ");
      }
      System.out.println();

      double[] prob_estimates2 = new double[model.nr_class];
      System.out.print(Linear.predictValues(model, oneTestLine, prob_estimates2));
      System.out.print("  ");
      for (int j = 0; j < model.nr_class; j++) {
        System.out.print(prob_estimates2[j]);
        System.out.print("  ");
      }
      System.out.println();

    }

  }

  public static void predict(Model model) {

    Integer[] negitiveIndex = {423, 9326, 48188, 81021, 81610, 83777, 96372, 100397, 123890, 166904, 228242, 356179, 396167,
            408745, 417897, 449931, 502601, 502824, 521015, 522416, 532462, 612473, 613901, 676053, 691407, 699142, 719620, 732901, 776838, 890667, 890864, 946309, 962748, 1007925, 1048495, 1111362, 1199102, 1209822, 1213785, 1268328, 1270343, 1314345, 1349169, 1356477, 1406391, 1424970, 1476083, 1505505, 1519956, 1621054, 1658653, 1791474, 1801577, 1836527, 1949619, 1953388, 2045026, 2225969, 2234896, 2281249, 2308061, 2315431, 2319832, 2413921, 2419995, 2482310, 2497671, 2506036, 2622089, 2634419, 2674255, 2684699, 2693660, 2795858, 2984422, 3068849, 3075507, 3146627, 3163374, 3183051, 3265827};

    Integer[] positiveIndex = {13680, 115892, 201403, 233585, 247271, 287553, 287817, 310455, 313541, 350655, 475296, 481106,
            499874, 523463, 529427, 561599, 608485, 653263, 669351, 718995, 747161, 752687, 803648, 808373, 820217, 1040871, 1093807, 1119602, 1169672, 1261106, 1270114, 1355124, 1421515, 1424847, 1453347, 1528963, 1553626, 1672825, 1705023, 1719851, 1743773, 1819827, 1908653, 1910600, 1921119, 2085606, 2117622, 2144811, 2244713, 2269233, 2407375, 2501470, 2538442, 2573307, 2591918, 2592701, 2624670, 2637661, 2652093, 2870098, 2890624, 2934555, 2935186, 3001127, 3007567, 3066041, 3066529, 3101696, 3122666, 3284822, 3286075};

    FeatureNode[] featureNodes = new FeatureNode[positiveIndex.length];

    for (int i = 0; i < positiveIndex.length; i++) {
//      FeatureNode f = new FeatureNode(negitiveIndex[i], 1.0);
      FeatureNode f = new FeatureNode(positiveIndex[i], 1.0);
      featureNodes[i] = f;
    }



//    double[] values = new double[21];
//    double d = -1.0;
//    for (int i = 0; i <= 20; i++) {
//      values[i] = d;
//      d += 0.1;
//    }

    double[] prob_estimates = new double[model.nr_class];

    System.out.println(Linear.predict(model, featureNodes));
    System.out.println(Linear.predictProbability(model, featureNodes, prob_estimates));
    System.out.println(Linear.predictValues(model, featureNodes, prob_estimates));


  }


  public static void doPredictAllFromTxt(Model model) throws IOException {

    Writer writer = new StringWriter();

    String path = "/Users/neyao/dev/machine_learning/liblinear/data/data.txt";
    BufferedReader reader = new BufferedReader(new FileReader(path));

    System.out.println("----------------");
    Predict.doPredict(reader, writer, model);
    System.out.println("----------------");

  }

  public static void doPredictOneLineOfString(Model model) throws IOException {

    StringBuilder sb = new StringBuilder();
    Writer writer = new StringWriter();
    sb.append(1).append(" 13680:1 115892:1 201403:1 233585:1 247271:1 287553:1 287817:1 310455:1 313541:1 350655:1 475296:1 481106:1 499874:1 523463:1 529427:1 561599:1 608485:1 653263:1 669351:1 718995:1 747161:1 752687:1 803648:1 808373:1 820217:1 1040871:1 1093807:1 1119602:1 1169672:1 1261106:1 1270114:1 1355124:1 1421515:1 1424847:1 1453347:1 1528963:1 1553626:1 1672825:1 1705023:1 1719851:1 1743773:1 1819827:1 1908653:1 1910600:1 1921119:1 2085606:1 2117622:1 2144811:1 2244713:1 2269233:1 2407375:1 2501470:1 2538442:1 2573307:1 2591918:1 2592701:1 2624670:1 2637661:1 2652093:1 2870098:1 2890624:1 2934555:1 2935186:1 3001127:1 3007567:1 3066041:1 3066529:1 3101696:1 3122666:1 3284822:1 3286075:1").append("\n");
//    sb.append(-1).append(" 2:-71.555   9:88223").append("\n");

    BufferedReader reader = new BufferedReader(new StringReader(sb.toString()));

    System.out.println("----------------");
    Predict.doPredict(reader, writer, model);
    System.out.println("----------------");

  }

  private static Random random = new Random(12345);

  public static Model createRandomModel() {
    Model model = new Model();
    model.solverType = SolverType.L1R_LR;
    model.bias = 2;
    model.label = new int[]{1, Integer.MAX_VALUE, 2};
    model.w = new double[model.label.length * 300];
    for (int i = 0; i < model.w.length; i++) {
      // precision should be at least 1e-4
      model.w[i] = Math.round(random.nextDouble() * 100000.0) / 10000.0;
    }

    // force at least one value to be zero
    model.w[random.nextInt(model.w.length)] = 0.0;
    model.w[random.nextInt(model.w.length)] = -0.0;

    model.nr_feature = model.w.length / model.label.length - 1;
    model.nr_class = model.label.length;
    return model;
  }
}
