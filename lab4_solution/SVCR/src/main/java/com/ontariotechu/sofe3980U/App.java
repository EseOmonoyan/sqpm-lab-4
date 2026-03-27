package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 */
public class App
{
    public static void main(String[] args)
    {
        String[] files = {"model_1.csv", "model_2.csv", "model_3.csv"};

        double[] mseVals  = new double[3];
        double[] maeVals  = new double[3];
        double[] mareVals = new double[3];
        double epsilon = 1e-10;

        for (int f = 0; f < files.length; f++) {
            FileReader filereader;
            List<String[]> allData;
            try {
                filereader = new FileReader(files[f]);
                CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
                allData = csvReader.readAll();
            } catch (Exception e) {
                System.out.println("Error reading the CSV file: " + files[f]);
                return;
            }

            double sumSE = 0, sumAE = 0, sumARE = 0;
            int n = allData.size();

            for (String[] row : allData) {
                double y_true = Double.parseDouble(row[0]);
                double y_pred = Double.parseDouble(row[1]);
                double diff = y_true - y_pred;
                sumSE  += diff * diff;
                sumAE  += Math.abs(diff);
                sumARE += Math.abs(diff) / (Math.abs(y_true) + epsilon);
            }

            mseVals[f]  = sumSE  / n;
            maeVals[f]  = sumAE  / n;
            mareVals[f] = sumARE / n;

            System.out.println("for " + files[f]);
            System.out.printf("\tMSE =%.5f%n",  mseVals[f]);
            System.out.printf("\tMAE =%.7f%n",  maeVals[f]);
            System.out.printf("\tMARE =%.8f%n", mareVals[f]);
        }

        // Find best model for each metric (lowest error)
        int bestMSE = 0, bestMAE = 0, bestMARE = 0;
        for (int i = 1; i < 3; i++) {
            if (mseVals[i]  < mseVals[bestMSE])  bestMSE  = i;
            if (maeVals[i]  < maeVals[bestMAE])  bestMAE  = i;
            if (mareVals[i] < mareVals[bestMARE]) bestMARE = i;
        }

        System.out.println("According to MSE, The best model is "  + files[bestMSE]);
        System.out.println("According to MAE, The best model is "  + files[bestMAE]);
        System.out.println("According to MARE, The best model is " + files[bestMARE]);
    }
}
