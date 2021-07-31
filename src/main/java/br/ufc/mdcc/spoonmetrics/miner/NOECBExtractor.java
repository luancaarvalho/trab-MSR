package br.ufc.mdcc.spoonmetrics.miner;

import br.ufc.mdcc.spoonmetrics.model.Dataset;
import br.ufc.mdcc.spoonmetrics.model.Measure;
import br.ufc.mdcc.spoonmetrics.model.Metric;
import br.ufc.mdcc.spoonmetrics.util.Util;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtComment;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class NOECBExtractor extends AbstractProcessor<CtClass<?>> {
	@Override
	public void process(CtClass<?> element) {

		if (Util.isValid(element)) {
			String qualifiedName = element.getQualifiedName();

			long quantidade = 0;
			for (CtMethod<?> m : element.getMethods()) {
				if (m.getBody() != null && m.getBody().getElements(
						new TypeFilter<CtCatch>(CtCatch.class)) != null) {
					for (CtCatch catchstmt : m.getBody().getElements(
							new TypeFilter<CtCatch>(CtCatch.class))) {
						if (catchstmt.getBody().getStatements().stream()
								.filter(s -> !(s instanceof CtComment))
								.count() == 0) {
							quantidade++;
						}
					}
				}
			}

			Dataset.store(qualifiedName, new Measure(Metric.NOECB, quantidade));
		}
	}

}
