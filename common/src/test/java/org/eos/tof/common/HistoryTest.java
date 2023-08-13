package org.eos.tof.common;

import org.eos.tof.common.items.Item;
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.items.SSRare;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = {History.class})
class HistoryTest {

    @Autowired
    private History history;

    @AfterEach
    void tearDown() {
        history.reset();
    }

    @Test
    void shouldPreformBasicTasks() {
        List<Item> items = history.get();
        Assertions.assertTrue(items.isEmpty());

        var item = new SSRare();
        history.add(item);

        items = history.get();
        Assertions.assertFalse(items.isEmpty());
    }

    @Test
    void shouldGetTheLastAddedItem() {
        var first = new SRare();
        var second = new SSRare();
        history.add(first);
        history.add(second);

        var last = history.getLast();
        Assertions.assertEquals(second, last);
    }

    @Test
    void shouldGetNullWhenNoHistory() {
        history.reset();
        Assertions.assertNull(history.getLast());
    }
}
