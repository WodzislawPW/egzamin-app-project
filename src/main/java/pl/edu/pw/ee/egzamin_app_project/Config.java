package pl.edu.pw.ee.egzamin_app_project;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class Config
{
    private static final String FILE_PATH = "src/main/resources/pl/edu/pw/ee/egzamin_app_project/config.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    File file;

    private boolean darkMode;
    private boolean randomQuestionOrder;
    private boolean exportForPrint;

    public Config(){}

    public Config(boolean temp)
    {
        file = new File(FILE_PATH);
        Config tempConfig;

        try
        {
            if (file.exists())
            {
                tempConfig = mapper.readValue(file, new TypeReference<Config>() {});
                darkMode = tempConfig.isDarkMode();
                randomQuestionOrder = tempConfig.isRandomQuestionOrder();
                exportForPrint = tempConfig.isExportForPrint();
            }
            else
            {
                darkMode = false;
                randomQuestionOrder = true;
                exportForPrint = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            darkMode = false;
            randomQuestionOrder = true;
            exportForPrint = true;
            //return;
        }
    }

    public void saveConfig()
    {
        try
        {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showConfig()
    {
        System.out.println("Dark mode: " + darkMode);
        System.out.println("Random:    " + randomQuestionOrder);
        System.out.println("Print:     " + exportForPrint);
    }


    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public boolean isRandomQuestionOrder() {
        return randomQuestionOrder;
    }

    public void setRandomQuestionOrder(boolean randomQuestionOrder) {
        this.randomQuestionOrder = randomQuestionOrder;
    }

    public boolean isExportForPrint() {
        return exportForPrint;
    }

    public void setExportForPrint(boolean exportForPrint) {
        this.exportForPrint = exportForPrint;
    }
}
