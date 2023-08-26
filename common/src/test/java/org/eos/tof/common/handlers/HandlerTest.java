package org.eos.tof.common.handlers;

import org.eos.tof.common.Banner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Spy;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HandlerTest {

    private static class MockHandler extends Handler {

        @Override
        public boolean check(final Banner banner) {
            return checkNext(banner);
        }
    }

    @Spy
    private Handler first = new MockHandler();
    @Spy
    private Handler second = new MockHandler();
    @Spy
    private Handler last = new MockHandler();

    private Handler handler;

    @BeforeEach
    void setUp() {
        handler = Handler.link(first, second, last);
    }

    @Test
    void shouldHandleTheChain() {
        var result = handler.check(null);
        Assertions.assertTrue(result);

        verify(first).checkNext(null);
        verify(second).checkNext(null);
        verify(last).checkNext(null);
    }
}
