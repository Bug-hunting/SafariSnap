package com.example.Safari_Snap.Activity;

import android.app.Activity;
import android.graphics.Bitmap;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class Classifier {

    private final List<String> labels;
    private final Interpreter tflite;
    private TensorImage tensorImage;
    private final TensorBuffer probabilityBuffer;
    private final TensorProcessor processor;

    private static final float IMAGE_STD = 1.0f;
    private static final float IMAGE_MEAN = 0.0f;

    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;

    private final int imageResizeX;
    private final int imageResizeY;

    public static String animal_detect;
    private static final int MAX_SIZE = 6;


    public Classifier(Activity activity) throws IOException {
        //load tensor flow model and label
        MappedByteBuffer tfliteModel = FileUtil.loadMappedFile(activity, "safari_model.tflite");
        labels = FileUtil.loadLabels(activity, "Label.txt");

        //create an interpreter
        tflite = new Interpreter(tfliteModel, null);

        int[] input_imageShape = tflite.getInputTensor(0).shape();
        int[] output_imageShape = tflite.getOutputTensor(0).shape();

        DataType input_DataType = tflite.getInputTensor(0).dataType();
        DataType output_DataType = tflite.getOutputTensor(0).dataType();

        imageResizeX = input_imageShape[1];
        imageResizeY = input_imageShape[2];

        tensorImage = new TensorImage(input_DataType); // creates the input tensor
        probabilityBuffer = TensorBuffer.createFixedSize(output_imageShape, output_DataType); //creates the output tensor

        // Creates the post processor for the output probability.
        processor = new TensorProcessor.Builder().add(new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD))
                .build();

    }

    //function that classifies the image. Input = bitmap | Output = List
    public List<Recognition> recognize_Image(final Bitmap bitmap) {
        List<Recognition> recognitions = new ArrayList<>(); //creating a list to record results

        tensorImage = loadImage(bitmap); //loading image

        //run the model
        tflite.run(tensorImage.getBuffer(), probabilityBuffer.getBuffer().rewind());

        //stores the data in map
        Map<String, Float> label_probability = new TensorLabel(labels, processor.process(probabilityBuffer)).getMapWithFloatValue();

        // saves the max value in map
        float max_value_in_map = (Collections.max(label_probability.values()));

        for (Map.Entry<String, Float> entry : label_probability.entrySet()) {
            //add the data to map
            recognitions.add(new Recognition(entry.getKey(), entry.getValue()));

            //comparing max value to the each element and saving label in animal detect
            if (entry.getValue() == max_value_in_map) {
                animal_detect = entry.getKey(); //storing the name of max
            }

        }
        return recognitions.subList(0, MAX_SIZE);
    }

    private TensorImage loadImage(Bitmap bitmap) {
        // Loads bitmap into a TensorImage.
        tensorImage.load(bitmap);

        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());

        // Creates processor for the TensorImage.
        // pre processing steps are applied here
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                .add(new ResizeOp(imageResizeX, imageResizeY, ResizeOp.ResizeMethod.BILINEAR))
                .add(new NormalizeOp(IMAGE_MEAN, IMAGE_STD))
                .build();

        return imageProcessor.process(tensorImage);
    }


    public class Recognition implements Comparable {

        private String name;
        private float confidence;


        public Recognition(String name, float confidence) {
            this.name = name;
            this.confidence = confidence;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Recognition{" +
                    "name='" + name + '\'' +
                    ", confidence=" + confidence +
                    '}';
        }

        @Override
        public int compareTo(Object o) {
            return Float.compare(((Recognition) o).confidence, this.confidence);
        }

    }


}
