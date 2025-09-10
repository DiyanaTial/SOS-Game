package sprint0.parta;

public class Factorial {

    public int factorial(int num){

        int factorial = 1;
        for (int i = 1; i <= num; i++){
            factorial *= i;
        }

        return factorial;
    }
}
