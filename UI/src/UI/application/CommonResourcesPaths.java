package UI.application;

import java.net.URL;

public class CommonResourcesPaths {

        private static final String BASE_PACKAGE = "/UI/application/";

        public final static String APP_FXML_INCLUDE_RESOURCE = "/UI/application/AllMachine.fxml";
        public final static String MACHINE_CONF_RESOURCE= "/UI/application/MachineConfTab/MachineConfig.fxml";
        public final static String SINGLE_CODE_STATISTICS_RESOURCE= "UI/application/encryptTab/statisticsComponent/singleCodeStatistics/singleCodeStatisticsView.fxml";
        public final static String SIMPLE_CODE_FORMAT= "UI/application/generalComponents/codeFormat/SimpleCode/simpleCodeVersion.fxml";
        public static final String CANDIDATE_SINGLE_TILE = BASE_PACKAGE + "/DmTab/CandidatesStatus/singleCandidate/singleCandidate.fxml";
        public static final URL MAIN_FXML_RESOURCE = CommonResourcesPaths.class.getResource(CommonResourcesPaths.CANDIDATE_SINGLE_TILE);

}
