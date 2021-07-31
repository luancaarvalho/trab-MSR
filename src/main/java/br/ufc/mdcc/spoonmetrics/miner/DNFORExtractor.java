package br.ufc.mdcc.spoonmetrics.miner;

import br.ufc.mdcc.spoonmetrics.model.Dataset;
import br.ufc.mdcc.spoonmetrics.model.Measure;
import br.ufc.mdcc.spoonmetrics.model.Metric;
import br.ufc.mdcc.spoonmetrics.util.Util;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtFor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.filter.TypeFilter;

public class DNFORExtractor extends AbstractProcessor<CtClass<?>> {
	@Override
	public void process(CtClass<?> element) {

		if (Util.isValid(element)) {
			String qualifiedName = element.getQualifiedName();

			double greaterDepthOfNested = 0.0;

			for (CtFor forstmt : element
					.getElements(new TypeFilter<CtFor>(CtFor.class))) {
				double value = depthOfNestedFor(forstmt) + 1;
				if (value > greaterDepthOfNested) {
					greaterDepthOfNested = value;
				}
			}

			Dataset.store(qualifiedName,
					new Measure(Metric.DNFOR, greaterDepthOfNested));
		}
	}

	private double depthOfNestedFor(CtFor forstmt) {
		double greaterDepthOfNested = 0.0;
		for (CtFor ctFor : forstmt.getBody()
				.getElements(new TypeFilter<CtFor>(CtFor.class))) {
			double value = depthOfNestedFor(ctFor) + 1D;
			if (value > greaterDepthOfNested) {
				greaterDepthOfNested = value;
			}
		}
		return greaterDepthOfNested;
	}
}
