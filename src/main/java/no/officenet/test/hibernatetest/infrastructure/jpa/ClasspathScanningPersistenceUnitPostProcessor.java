package no.officenet.test.hibernatetest.infrastructure.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.util.Assert;

import javax.persistence.Converter;

public class ClasspathScanningPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(ClasspathScanningPersistenceUnitPostProcessor.class);

	private final String basePackage;

	/**
	 * Creates a new {@link ClasspathScanningPersistenceUnitPostProcessor} using the given base package as scan base.
	 *
	 * @param basePackage must not be {@literal null} or empty.
	 */
	public ClasspathScanningPersistenceUnitPostProcessor(String basePackage) {
		Assert.hasText(basePackage);
		this.basePackage = basePackage;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor#postProcessPersistenceUnitInfo(org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo)
	 */
	public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {

		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(Converter.class));

		for (BeanDefinition definition : provider.findCandidateComponents(basePackage)) {

			LOG.debug("Registering classpath-scanned entity %s in persistence unit info!", definition.getBeanClassName());
			pui.addManagedClassName(definition.getBeanClassName());
		}

	}

}
