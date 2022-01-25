package com.csicit.ace.gateway.config;

import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.support.AbstractConfigurable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * This class is BETA and may be subject to change in a future release.
 *
 * @param <C> {@link AbstractConfigurable} subtype
 */
public abstract class AceAbstractGatewayFilterFactory<C> extends AbstractConfigurable<C>
        implements GatewayFilterFactory<C>, ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @SuppressWarnings("unchecked")
    public AceAbstractGatewayFilterFactory() {
        super((Class<C>) Object.class);
    }

    public AceAbstractGatewayFilterFactory(Class<C> configClass) {
        super(configClass);
    }

    protected ApplicationEventPublisher getPublisher() {
        return this.publisher;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public static class NameConfig {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}