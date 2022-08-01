package EnigmaMachine;

public class RotorOutputData {
        private final int outputIndex;
        private final boolean advanceNextRotor;
        public RotorOutputData(int outputIndex, boolean advanceNextRotor) {
            this.outputIndex= outputIndex;
            this.advanceNextRotor = advanceNextRotor;
        }

    public boolean isAdvanceNextRotor() {
        return advanceNextRotor;
    }

    public int getOutputIndex() {
        return outputIndex;
    }
}

