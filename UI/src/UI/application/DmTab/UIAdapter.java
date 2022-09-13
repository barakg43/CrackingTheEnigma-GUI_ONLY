package UI.application.DmTab;

import dtoObjects.DmDTO.CandidateDTO;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private Consumer<TaskFinishDataDTO> introduceNewCandidates;
    private Consumer<CandidateDTO> updateExistingWord;
    private Runnable updateDistinct;
    private Consumer<Integer> updateTotalProcessedWords;

    public UIAdapter(Consumer<TaskFinishDataDTO> introduceNewCandidates, Consumer<CandidateDTO> updateExistingWord, Runnable updateDistinct, Consumer<Integer> updateTotalProcessedWords) {
        this.introduceNewCandidates = introduceNewCandidates;
        this.updateExistingWord = updateExistingWord;
        this.updateDistinct = updateDistinct;
        this.updateTotalProcessedWords = updateTotalProcessedWords;
    }

    public void addNewCandidates(TaskFinishDataDTO histogramData) {

                 introduceNewCandidates.accept(histogramData);
                 updateDistinct.run();


    }

    public void updateExistingWord(CandidateDTO histogramData) {
        Platform.runLater(
                () -> updateExistingWord.accept(histogramData)
        );
    }

    public void updateTotalProcessedWords(int delta) {
        Platform.runLater(
                () -> updateTotalProcessedWords.accept(delta)
        );
    }

}
