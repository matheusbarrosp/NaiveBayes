/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import naivebayes.NaiveBayes;
import naivebayes.TrainningSet;
import naivebayes.Vector;
import naivebayes.crossvalidation.CrossValidation;
import naivebayes.crossvalidation.metrics.CrossvalidationResult;
import util.Utils;
import window.MainWindow;

/**
 *
 * @author elderjr
 */
public class Main {

    public static void main(String[] args) throws IOException{
        new MainWindow().setVisible(true);
    }
}
