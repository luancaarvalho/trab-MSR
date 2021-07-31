package br.ufc.mdcc.spoonmetrics.miner;

import br.ufc.mdcc.spoonmetrics.model.Dataset;
import br.ufc.mdcc.spoonmetrics.model.Measure;
import br.ufc.mdcc.spoonmetrics.model.Metric;
import br.ufc.mdcc.spoonmetrics.util.Util;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

public class NOPRAExtractor extends AbstractProcessor<CtClass<?>> {

	@Override
	public void process(CtClass<?> element) {

		if (Util.isValid(element)) {
			String qualifiedName = element.getQualifiedName();
			double numberOfPrivateAttributes = element.getFields().stream()
					.filter(p -> p.isPrivate()).count();
			Dataset.store(qualifiedName,
					new Measure(Metric.NOPRA, numberOfPrivateAttributes));
		}
	}
}
