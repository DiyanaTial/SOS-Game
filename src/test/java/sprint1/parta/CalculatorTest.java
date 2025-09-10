package sprint1.parta;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    @Test
    public void addTest(){

        Calculator calculator = new Calculator();
        int actualResult = calculator.add(10,20);

        assertEquals(30,actualResult);
    }
}