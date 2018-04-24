package com.mc.shiyinqiao.myapplication.tensorflow;

import android.content.res.AssetManager;

import java.io.IOException;

/**
 * @author MC
 * @version v0.8
 * @since 18/4/23
 */

public class TFBridge {

    public static final int CLASS = 0;
    public static final int DETECT = 1;

    public static String MODEL_FILE;
    public static String LABEL_FILE;
    public static int INPUT_SIZE;

    public static Classifier getClassifier(AssetManager am, int type) throws IOException {
        if (type == 1) {
            MODEL_FILE = TensorFlowObjectDetectionAPIModel.MODEL_FILE;
            LABEL_FILE = TensorFlowObjectDetectionAPIModel.LABEL_FILE;
            INPUT_SIZE = TensorFlowObjectDetectionAPIModel.INPUT_SIZE;
            return TensorFlowObjectDetectionAPIModel.create(am, MODEL_FILE, LABEL_FILE, INPUT_SIZE);
        } else {
            MODEL_FILE = TensorFlowImageClassifier.MODEL_FILE;
            LABEL_FILE = TensorFlowImageClassifier.LABEL_FILE;
            INPUT_SIZE = TensorFlowImageClassifier.INPUT_SIZE;
            return TensorFlowImageClassifier.create(
                    am,
                    TensorFlowImageClassifier.MODEL_FILE,
                    TensorFlowImageClassifier.LABEL_FILE,
                    TensorFlowImageClassifier.INPUT_SIZE,
                    TensorFlowImageClassifier.IMAGE_MEAN,
                    TensorFlowImageClassifier.IMAGE_STD,
                    TensorFlowImageClassifier.INPUT_NAME,
                    TensorFlowImageClassifier.OUTPUT_NAME);
        }
    }
}
