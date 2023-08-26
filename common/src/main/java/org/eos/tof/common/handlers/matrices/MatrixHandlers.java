package org.eos.tof.common.handlers.matrices;

import org.eos.tof.common.handlers.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Handler for processing the banner after pulling on matrices.
 *
 * @author Eos
 */
@Configuration(value = "matrices")
@ComponentScan("org.eos.tof.common.handlers.matrices")
public class MatrixHandlers {

    /**
     * A chain of handlers for matrices for pulling on the banner.
     *
     * @param matrixPityHandler  Handler for handling pity on matrices.
     * @param matrixPullHandler  Handler for handling pulling on matrices.
     * @param matrixTokenHandler Handler for handling post pull statistics on matrices.
     * @return The handler chain.
     */
    @Bean
    public Handler matrixHandlers(final PityHandler matrixPityHandler,
                                  final PullHandler matrixPullHandler,
                                  final TokenHandler matrixTokenHandler) {
        return Handler.link(matrixPityHandler, matrixPullHandler, matrixTokenHandler);
    }
}
