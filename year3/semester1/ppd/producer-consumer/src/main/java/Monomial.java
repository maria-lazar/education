public class Monomial {
    private int coefficient;
    private int exponent;

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public int getExponent() {
        return exponent;
    }

    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    public Monomial(int coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    @Override
    public String toString() {
        return "Monomial{" +
                "coefficient=" + coefficient +
                ", exponent=" + exponent +
                '}';
    }
}
