package eu.sportsfusion.common.micro.quarkus.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.Capabilities;
import io.quarkus.deployment.Capability;

import eu.sportsfusion.common.configuration.control.ConfigurationService;
import eu.sportsfusion.common.jobcontrol.control.JobControl;
import eu.sportsfusion.common.validation.constraints.impl.ClientUniquePropertyValidator;
import eu.sportsfusion.common.validation.constraints.impl.UniquePropertyValidator;

class CommonMicroQuarkusProcessor {

    private static final String FEATURE = "common-micro-quarkus";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem registerJpaDependentBeans(Capabilities capabilities) {
        if (!capabilities.isPresent(Capability.HIBERNATE_ORM)) {
            return null;
        }

        AdditionalBeanBuildItem.Builder builder = AdditionalBeanBuildItem.builder()
                .addBeanClass(ConfigurationService.class)
                .addBeanClass(JobControl.class)
                .setUnremovable();

        // These validators require both JPA (EntityManager) and Hibernate Validator integration.
        if (capabilities.isPresent(Capability.HIBERNATE_VALIDATOR)) {
            builder.addBeanClass(UniquePropertyValidator.class)
                    .addBeanClass(ClientUniquePropertyValidator.class);
        }

        return builder.build();
    }
}
