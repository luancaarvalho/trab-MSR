package br.ufc.mdcc.spoonmetrics.miner;

import br.ufc.mdcc.spoonmetrics.model.Dataset;
import br.ufc.mdcc.spoonmetrics.model.Measure;
import br.ufc.mdcc.spoonmetrics.model.Metric;
import br.ufc.mdcc.spoonmetrics.util.Util;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtIf;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.filter.TypeFilter;

public class DNIFExtractor extends AbstractProcessor<CtClass<?>> {
	@Override
	public void process(CtClass<?> element) {

		if (Util.isValid(element)) {
			String qualifiedName = element.getQualifiedName();

			double greaterDepthOfNested = 0.0;

			for (CtIf ifstmt : element
					.getElements(new TypeFilter<CtIf>(CtIf.class))) {
				double value = depthOfNestedIf(ifstmt) + 1;
				if (value > greaterDepthOfNested) {
					greaterDepthOfNested = value;
				}
			}

			Dataset.store(qualifiedName,
					new Measure(Metric.DNIF, greaterDepthOfNested));
		}
	}

	private double depthOfNestedIf(CtIf ifstmt) {
		double greaterDepthOfNested = 0.0;
		for (CtIf element : ifstmt.getThenStatement()
				.getElements(new TypeFilter<CtIf>(CtIf.class))) {
			double value = depthOfNestedIf(element) + 1D;
			if (value > greaterDepthOfNested) {
				greaterDepthOfNested = value;
			}
		}
		return greaterDepthOfNested;
	}
}
