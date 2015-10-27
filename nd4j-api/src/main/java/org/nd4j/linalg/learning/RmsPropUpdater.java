package org.nd4j.linalg.learning;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

/**
 *
 * RMS Prop updates:
 *
 * http://www.cs.toronto.edu/~tijmen/csc321/slides/lecture_slides_lec6.pdf
 * http://cs231n.github.io/neural-networks-3/#ada
 *
 * @author Adam Gibson
 */
@Data
@NoArgsConstructor
public class RmsPropUpdater implements GradientUpdater {
    private INDArray lastGradient;
    private double rmsDecay = 0.5;
    private double learningRate = 1e-1;
    private double epsilon = 1e-8;
    private double momentum = 0.5;

    public RmsPropUpdater(double learningRate, double rmsDecay){
    	this.learningRate = learningRate;
    	this.rmsDecay = rmsDecay;
    }

    @Override
    public INDArray getGradient(INDArray gradient, int iteration) {
        if(lastGradient == null)
            lastGradient = Nd4j.zeros(gradient.shape());
        lastGradient.muli(rmsDecay).addi(gradient.mul(gradient).muli(1 - rmsDecay));
        // lr * gradient / sqrt(cache + 1e-8)
        INDArray ret = gradient.mul(learningRate).divi(Transforms.sqrt(lastGradient.add(epsilon)));
        
        return ret;
    }
}
