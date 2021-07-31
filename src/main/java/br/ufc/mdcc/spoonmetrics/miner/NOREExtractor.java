package br.ufc.mdcc.spoonmetrics.miner;

import br.ufc.mdcc.spoonmetrics.model.Dataset;
import br.ufc.mdcc.spoonmetrics.model.Measure;
import br.ufc.mdcc.spoonmetrics.model.Metric;
import br.ufc.mdcc.spoonmetrics.util.Util;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtThrow;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class NOREExtractor extends AbstractProcessor<CtClass<?>> {
	@Override
	public void process(CtClass<?> element) {

		if (Util.isValid(element)) {
			String qualifiedName = element.getQualifiedName();

			long quantidade = 0;
			for (CtMethod<?> m : element.getAllMethods()) {
				if (m.getBody() != null
						&& m.getElements(
								new TypeFilter<CtThrow>(CtThrow.class)) != null
						&& m.getElements(new TypeFilter<CtThrow>(CtThrow.class))
								.size() > 0) {
					quantidade += m
							.getElements(new TypeFilter<CtThrow>(CtThrow.class))
							.size();
				}
			}

			Dataset.store(qualifiedName, new Measure(Metric.NOREE, quantidade));
		}
	}
}
