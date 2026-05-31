package pl.edu.pw.ee.egzamin_app_project;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config
{
    private static final String FILE_PATH = "src/main/resources/pl/edu/pw/ee/egzamin_app_project/config.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    File file;

    private boolean darkMode;
    private boolean randomQuestionOrder;

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
            }
            else
            {
                darkMode = false;
                randomQuestionOrder = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            darkMode = false;
            randomQuestionOrder = true;
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
}
