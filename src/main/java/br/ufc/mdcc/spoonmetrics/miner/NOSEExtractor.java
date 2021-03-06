package br.ufc.mdcc.spoonmetrics.miner;

import br.ufc.mdcc.spoonmetrics.model.Dataset;
import br.ufc.mdcc.spoonmetrics.model.Measure;
import br.ufc.mdcc.spoonmetrics.model.Metric;
import br.ufc.mdcc.spoonmetrics.util.Util;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

public class NOSEExtractor extends AbstractProcessor<CtClass<?>> {
	@Override
	public void process(CtClass<?> element) {

		if (Util.isValid(element)) {
			String qualifiedName = element.getQualifiedName();

			long quantidade = element.getMethods().stream()
					.filter(m -> !m.getThrownTypes().isEmpty()).count();

			Dataset.store(qualifiedName, new Measure(Metric.NOSEE, quantidade));
		}
	}
}
