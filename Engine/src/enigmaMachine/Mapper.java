package enigmaMachine;

public interface Mapper<T,K> {
     K getMappedOutput(T input);
     void addMappedInputOutput(T input,K output);
}
