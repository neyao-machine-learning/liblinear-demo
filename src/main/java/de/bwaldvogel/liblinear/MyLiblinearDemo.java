package de.bwaldvogel.liblinear;

import java.io.File;

/**
 * 教程参见：https://www.evernote.com/shard/s128/nl/2147483647/457d10f1-22b8-4295-bcf7-069e721e24ab/
 * Created by yaonengjun on 2017/10/22 下午4:31.
 */
public class MyLiblinearDemo {

  public static void main(String[] args) throws Exception {
    //loading train data
    Feature[][] featureMatrix = new Feature[5][];
    Feature[] featureMatrixRow1 = { new FeatureNode(2, 0.1), new FeatureNode(3, 0.2) };
    Feature[] featureMatrixRow2 = { new FeatureNode(2, 0.1), new FeatureNode(3, 0.3), new FeatureNode(4, -1.2)};
    Feature[] featureMatrixRow3 = { new FeatureNode(1, 0.4) };
    Feature[] featureMatrixRow4 = { new FeatureNode(2, 0.1), new FeatureNode(4, 1.4), new FeatureNode(5, 0.5) };
    Feature[] featureMatrixRow5 = { new FeatureNode(1, -0.1), new FeatureNode(2, -0.2), new FeatureNode(3, 0.1), new FeatureNode(4, -1.1), new FeatureNode(5, 0.1) };
    featureMatrix[0] = featureMatrixRow1;
    featureMatrix[1] = featureMatrixRow2;
    featureMatrix[2] = featureMatrixRow3;
    featureMatrix[3] = featureMatrixRow4;
    featureMatrix[4] = featureMatrixRow5;
    //loading target value
    double[] targetLabelsColumn = {1,-1,1,-1,0};

    Problem problem = new Problem();
    problem.l = 5; // number of training examples：训练样本数
    problem.n = 5; // number of features：特征维数
    problem.x = featureMatrix; // feature nodes：特征数据
    problem.y = targetLabelsColumn; // target values：类别

    SolverType solver = SolverType.L2R_LR; // -s 0   输出标签值
//    SolverType solver = SolverType.L2R_L2LOSS_SVR; // -s 11   输出具体的值
    double C = 1.0;    // cost of constraints violation
    double eps = 0.01; // stopping criteria

    Parameter parameter = new Parameter(solver, C, eps);
    Model model = Linear.train(problem, parameter);
    File modelFile = new File("/Users/neyao/dev/machine_learning/liblinear/tmp/my_liblinear_demo/model.txt");

    model.save(modelFile);
    // load model or use it directly
    model = Model.load(modelFile);

    Feature[] testNode = { new FeatureNode(1, 0.4), new FeatureNode(3, 0.3) };//test node
    double prediction = Linear.predict(model, testNode);

    double[] values = new double[21];
    double d = -1.0;
    for (int i = 0 ; i <= 20; i++) {
      values[i] = d;
      d += 0.1;
    }
    double predictValues = Linear.predictValues(model, testNode, values);
    System.out.println("prediction result: "+prediction);
    System.out.println("predictValues result: "+predictValues);
  }
}
