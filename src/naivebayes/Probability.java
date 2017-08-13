package naivebayes;

class Probability {

    public long word;
    public double pos;
    public double neu;
    public double neg;

    public Probability(long word) {
        this.word = word;
        this.pos = 0;
        this.neu = 0;
        this.neg = 0;
    }

}
