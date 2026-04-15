package pl.edu.pw.ee.egzamin_app_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuestionService
{
    private static final String FILE_PATH = "src/main/resources/pl/edu/pw/ee/egzamin_app_project/questions.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    File file;
    List<Question> questions;
    String keyword = "";

    public QuestionService()
    {
        file = new File(FILE_PATH);

        try
        {
            if (file.exists())
            {
                questions = mapper.readValue(file, new TypeReference<List<Question>>() {});
            }
            else
            {
                questions = new ArrayList<>();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            questions = new ArrayList<>();
            //return;
        }
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public List<Question> getQuestions()
    {
        questions.sort(Comparator.comparing( (Question q) -> !q.toString().toLowerCase().contains(keyword.toLowerCase()) ));
        return questions;
    }

    public void saveQuestion(Question newQuestion)
    {
        try
        {
            questions.add(newQuestion);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, questions);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteCategory(Question removedQuestion)
    {
        try
        {
            questions.remove(removedQuestion);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, questions);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
