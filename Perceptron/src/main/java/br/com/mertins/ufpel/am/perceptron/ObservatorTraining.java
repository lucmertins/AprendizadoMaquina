package br.com.mertins.ufpel.am.perceptron;

import java.time.Duration;

/**
 *
 * @author mertins
 */
public interface ObservatorTraining {

    default void register(Duration duration, int epoca, double[] errEpoca) {
    }
}
