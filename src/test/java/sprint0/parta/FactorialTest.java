package sprint0.parta;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FactorialTest {

    @Test
    public void factorialTest(){

        Factorial factorial = new Factorial();
        int actualResult = factorial.factorial(10);

        assertEquals(3628800, actualResult);
    }
}