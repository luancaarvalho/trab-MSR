package br.ufc.mdcc.spoonmetrics.miner;

import java.util.HashSet;
import java.util.Set;

import br.ufc.mdcc.spoonmetrics.model.Dataset;
import br.ufc.mdcc.spoonmetrics.model.Measure;
import br.ufc.mdcc.spoonmetrics.model.Metric;
import br.ufc.mdcc.spoonmetrics.util.Util;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class LCOMExtractor extends AbstractProcessor<CtClass<?>> {

	private Set<String> fieldNamesSet = new HashSet<String>();

	@Override
	public void process(CtClass<?> element) {

		if (Util.isValid(element)) {

			double lackOfCohesionOfMethods = 0.0;
			String qualifiedName = element.getQualifiedName();
			if (!element.getMethods().isEmpty()) {
				for (CtField<?> field : element.getFields()) {
					this.fieldNamesSet.add(field.getReference().getSimpleName());
				}

				CtMethod<?>[] methods = element.getMethods().toArray(new CtMethod<?>[element.getMethods().size()]);

				for (int i = 0; i < methods.length - 1; i++) {
					Set<String> left = usedField(methods[i]);
					for (int j = i + 1; j < methods.length; j++) {
						Set<String> right = usedField(methods[j]);
						right.retainAll(left);
						if (right.isEmpty()) {
							lackOfCohesionOfMethods++;
						}
					}
				}
			}
			Dataset.store(qualifiedName, new Measure(Metric.LCOM, lackOfCohesionOfMethods));
		}
	}

	private Set<String> usedField(CtMethod<?> method) {
		Set<String> usedFields = new HashSet<>();
		for (CtFieldAccess<?> access : method.getElements(new TypeFilter<CtFieldAccess<?>>(CtFieldAccess.class))) {
			if (this.fieldNamesSet.contains(access.getVariable().getSimpleName())) {
				usedFields.add(access.getVariable().getSimpleName());
			}
		}
		return usedFields;
	}
}
