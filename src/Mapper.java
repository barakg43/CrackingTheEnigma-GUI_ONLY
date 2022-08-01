package EnigmaMachine;

public interface Mapper<T> {
     T getMappedOutput(T input);
     void addMappedInputOutput(T input,T output);
}
