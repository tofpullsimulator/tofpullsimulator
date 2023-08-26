package org.eos.tof.common.items;

import java.util.random.RandomGenerator;

import org.eos.tof.common.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {Config.class})
class MatrixTest {

    @MockBean
    private RandomGenerator rng;

    @Test
    void shouldCreateARandomStandardMatrix() {
        when(rng.nextInt(anyInt())).thenReturn(0);
        var result = new Matrix(rng);

        Assertions.assertEquals(Matrix.OPTIONS_LIST[0], result.getName());
        Assertions.assertEquals(Matrix.Pieces.BRAIN, result.getPosition());
        Assertions.assertTrue(result.isStandard());
    }

    @Test
    void shouldCreateMatrixWithoutBlockingPiece() {
        when(rng.nextInt(anyInt())).thenReturn(0);
        var result = new Matrix("Yu Lan", rng, null);

        Assertions.assertEquals("Yu Lan", result.getName());
        Assertions.assertEquals(Matrix.Pieces.BRAIN, result.getPosition());
        Assertions.assertFalse(result.isStandard());
    }

    @Test
    void shouldCreateMatrixWithBlockingPiece() {
        when(rng.nextInt(anyInt())).thenReturn(0, 1);
        var result = new Matrix("Yu Lan", rng, Matrix.Pieces.BRAIN);

        Assertions.assertEquals("Yu Lan", result.getName());
        Assertions.assertEquals(Matrix.Pieces.HANDS, result.getPosition());
        Assertions.assertFalse(result.isStandard());
    }

    @Test
    void shouldGetHumanReadablePiece() {
        Assertions.assertEquals("Brain", Matrix.Pieces.BRAIN.toString());
    }
}
