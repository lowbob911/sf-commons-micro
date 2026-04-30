package eu.sportsfusion.common;

import java.util.Optional;

import org.eclipse.microprofile.config.ConfigProvider;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author Anatoli Pranovich
 */
@ApplicationScoped
public class DiscoveryService {

    public String discoverService(String name) {
        Optional<String> addr = ConfigProvider.getConfig().getOptionalValue("sf.discovery." + name + ".service.address", String.class);

        if (addr.isEmpty() || StringUtils.isBlank(addr.get())) {
            throw new RuntimeException("Service '" + name + "' cannot be found!");
        }

        return addr.get();
    }
}
